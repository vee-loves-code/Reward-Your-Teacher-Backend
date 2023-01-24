package com.decagon.rewardyourteacherapi.serviceImpl;

import com.decagon.rewardyourteacherapi.dto.SenderTransferDto;
import com.decagon.rewardyourteacherapi.dto.TransferDto;
import com.decagon.rewardyourteacherapi.dto.UserDto;
import com.decagon.rewardyourteacherapi.entity.*;
import com.decagon.rewardyourteacherapi.enums.TransactionType;
import com.decagon.rewardyourteacherapi.exception.ErrorMessage;
import com.decagon.rewardyourteacherapi.exception.ResourceNotFound;
import com.decagon.rewardyourteacherapi.exception.UserNotFoundException;
import com.decagon.rewardyourteacherapi.repository.*;
import com.decagon.rewardyourteacherapi.response.PaymentResponse;
import com.decagon.rewardyourteacherapi.service.TransactionService;
import com.decagon.rewardyourteacherapi.utils.AuthDetails;
import com.decagon.rewardyourteacherapi.utils.PaymentRequest;
import com.decagon.rewardyourteacherapi.utils.VerifyTransactionResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;


    private UserServiceImpl userService;
    private AuthDetails authDetails;
    private User user = new User();

    private MailService mailService;

    @Value("${secret_key}")
    private String PAYSTACK_SECRET_KEY;

    @Value("${paystack_url}")
    private String PAYSTACK_BASE_URL;

    @Value("${verification_url}")
    private String PAYSTACK_VERIFY_URL;
    private final NotificationServiceImpl notificationService;
    private final WalletRepository walletRepository;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository, MailService mailService, UserRepository userRepository, UserServiceImpl userService,
                                  NotificationServiceImpl notificationService, AuthDetails authDetails, WalletRepository walletRepository) {
        this.userRepository = userRepository;
        this.mailService = mailService;
        this.authDetails = authDetails;
        this.transactionRepository = transactionRepository;
        this.userService = userService;
        this.notificationService = notificationService;
        this.walletRepository = walletRepository;
    }


    @Override
    public PaymentResponse initDeposit(PaymentRequest paymentRequest) throws Exception {
        user = userRepository.findUserByEmail(authDetails.getAuthorizedUserEmail()).orElseThrow(() -> new UserNotFoundException(authDetails.getAuthorizedUserEmail()));

        PaymentResponse paymentResponse;
        paymentRequest.setAmount(paymentRequest.getAmount() * 100);
        paymentRequest.setEmail(user.getEmail());

        try {
            Gson gson = new Gson();
            StringEntity entity = new StringEntity(gson.toJson(paymentRequest));
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost post = new HttpPost(PAYSTACK_BASE_URL);
            post.setEntity(entity);
            post.addHeader("Content-type", "application/json");
            post.addHeader("Authorization", "Bearer " + PAYSTACK_SECRET_KEY);
            StringBuilder result = new StringBuilder();
            HttpResponse response = httpClient.execute(post);

            System.out.println("HERE: " + response.getStatusLine().getStatusCode());
            if (response.getStatusLine().getStatusCode() == HttpStatus.OK.value()) {

                BufferedReader responseReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                String line;
                while ((line = responseReader.readLine()) != null) {
                    System.out.println(line);
                    result.append(line);
                }
            } else {
                throw new AuthenticationException("Error Occurred while initializing transaction");
            }
            ObjectMapper mapper = new ObjectMapper();
            paymentResponse = mapper.readValue(result.toString(), PaymentResponse.class);

        } catch (IOException e) {
            throw new RuntimeException("error occurred while initializing transaction");
        }
        return paymentResponse;
    }

    @Override
    public VerifyTransactionResponse verifyTransaction(String reference) {

        Transaction transaction = new Transaction();
        VerifyTransactionResponse payStackResponse = null;
        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(PAYSTACK_VERIFY_URL + reference);
            request.addHeader("Content-type", "application/json");
            request.addHeader("Authorization", "Bearer " + PAYSTACK_SECRET_KEY);
            StringBuilder result = new StringBuilder();

            HttpResponse response = client.execute(request);

            System.out.println(response.getStatusLine().getStatusCode() + " " + response.getStatusLine().getReasonPhrase() + " HERE");
            if (response.getStatusLine().getStatusCode() == HttpStatus.OK.value()) {
                BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

                String line;
                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }

            } else {
                throw new Exception("Error Occurred while trying to verify transaction");
            }
            ObjectMapper mapper = new ObjectMapper();


            payStackResponse = mapper.readValue(result.toString(), VerifyTransactionResponse.class);

            if (payStackResponse == null || payStackResponse.getStatus().equals("false")) {
                throw new Exception("An error occurred while  verifying payment");
            } else if (payStackResponse.getData().getStatus().equals("success")) {

                transaction.setAmount(payStackResponse.getData().getAmount().divide(BigDecimal.valueOf(100)));
                transaction.setTransactionType(TransactionType.CREDIT);

                Optional<Wallet> wallet = walletRepository.findById(user.getWallet().getId());
                if (wallet.isPresent()) {
                    wallet.get().setBalance(wallet.get().getBalance().add(transaction.getAmount()));
                    walletRepository.save(wallet.get());
                } else
                    throw new ResourceNotFound("wallet", "id", wallet.get().getId());

                transaction.setUser(user);

                transactionRepository.save(transaction);

                notificationService.depositNotification(transaction.getId());
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return payStackResponse;
    }

    @Override
    public List<Transaction> getTransactionHistory(String email) {
        User user = userService.getUserByEmail(email);
        List<Transaction> transactionHistory = transactionRepository.findUserTransactionHistory(user.getId());
        return transactionHistory;
    }

    @Override
    public TransferDto rewardTeacher(Long senderId, Long receiverId, SenderTransferDto senderTransferDto) throws Exception {


        User sender = userRepository.findById(senderId).orElseThrow(UserNotFoundException::new);
        User receiver = userRepository.findById(receiverId).orElseThrow(UserNotFoundException::new);
        Transaction receiverTransaction = new Transaction();
        Transaction senderTransaction = new Transaction();

        Wallet senderWallet = sender.getWallet();
        Wallet receiverWallet = receiver.getWallet();

        BigDecimal senderWalletBal = senderWallet.getBalance();
        BigDecimal receiverWalletBal = receiverWallet.getBalance();


        if (!(senderWalletBal.compareTo(senderTransferDto.getAmountToSend()) > 0 && (senderWalletBal.compareTo(senderWalletBal.subtract(senderTransferDto.getAmountToSend())) > 0))) {
            throw new Exception("Cannot send due to insufficient Fund");

        }
        senderWallet.setBalance(senderWalletBal.subtract(senderTransferDto.getAmountToSend()));
        receiverWallet.setBalance(receiverWalletBal.add(senderTransferDto.getAmountToSend()));
        walletRepository.save(senderWallet);
        walletRepository.save(receiverWallet);
        receiverTransaction.setTransactionType(TransactionType.CREDIT);
        senderTransaction.setTransactionType(TransactionType.DEBIT);
        receiverTransaction.setAmount(senderTransferDto.getAmountToSend());
        senderTransaction.setAmount(senderTransferDto.getAmountToSend());
        senderTransaction.setUser(sender);
        receiverTransaction.setUser(receiver);
        transactionRepository.save(senderTransaction);
        transactionRepository.save(receiverTransaction);

        String senderEmail = sender.getEmail();
        String recieverEmail = receiver.getEmail();
        String senderMailSubject = "Debit Alert";
        String receiverMailSubject = "Credit Alert";
        String senderEmailBody = "Dear " + sender.getName() + "\n" + ", You sent " + senderTransferDto.getAmountToSend() + " to " + receiver.getName() + " successfully." + "\n" +
                "Your current balance is NGN" + senderWallet.getBalance() + ".";
        String receiverEmailBody = "Dear " + receiver.getName() + "\n" + " You recieved " + senderTransferDto.getAmountToSend() + " from " + sender.getName() + "\n" +
                "Your current balance is NGN" + receiverWallet.getBalance() + ".";

        mailService.sendEmail(senderEmail, senderMailSubject, senderEmailBody);
        mailService.sendEmail(recieverEmail, receiverMailSubject, receiverEmailBody);

        return new TransferDto(receiver.getName(), LocalDateTime.now(), senderTransferDto.getAmountToSend(), "Transaction Successful");

    }

}



