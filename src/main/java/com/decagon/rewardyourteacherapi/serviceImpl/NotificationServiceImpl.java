package com.decagon.rewardyourteacherapi.serviceImpl;

import com.decagon.rewardyourteacherapi.dto.NotificationDto;
import com.decagon.rewardyourteacherapi.dto.UserDto;
import com.decagon.rewardyourteacherapi.entity.Notification;
import com.decagon.rewardyourteacherapi.entity.Transaction;
import com.decagon.rewardyourteacherapi.entity.User;
import com.decagon.rewardyourteacherapi.enums.TransactionType;
import com.decagon.rewardyourteacherapi.exception.ResourceNotFound;
import com.decagon.rewardyourteacherapi.repository.NotificationRepository;
import com.decagon.rewardyourteacherapi.repository.TransactionRepository;
import com.decagon.rewardyourteacherapi.repository.UserRepository;
import com.decagon.rewardyourteacherapi.response.ResponseAPI;
import com.decagon.rewardyourteacherapi.service.NotificationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.mail.MessagingException;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper;
    private final TransactionRepository transactionRepository;
    private final MailService mailService;
    UserDto sender = new UserDto();
    UserDto receiver = new UserDto();

    @Autowired
    public NotificationServiceImpl(NotificationRepository notificationRepository, ModelMapper mapper, UserRepository userRepository, TransactionRepository transactionRepository, MailService mailService) {
        this.notificationRepository = notificationRepository;
        this.mapper = mapper;
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.mailService = mailService;
    }

    @Override
    public NotificationDto saveNotification(NotificationDto notificationDto) {
        //convert notificationDto to notification
        Notification notification = convertToEntity(notificationDto);

        //save notification
        Notification savedNotification = notificationRepository.save(notification);

        //convert savedNotification to notificationDto and return it

        return convertToDto(savedNotification);
    }

    @Override
    public ResponseAPI<NotificationDto> depositNotification(Long transactionId) throws MessagingException {
        Optional<Transaction> tgTransaction = transactionRepository.findById(transactionId);
        NotificationDto notificationDto = new NotificationDto();

        if (tgTransaction.isPresent() && (ObjectUtils.containsConstant(TransactionType.values(), "CREDIT"))) {
            notificationDto.setMessageBody("You have funded your wallet with N" + tgTransaction.get().getAmount());
            notificationDto.setCreateDate(LocalDateTime.now());
            notificationDto.setUser(tgTransaction.get().getUser());
            saveNotification(notificationDto);

            UserDto userDTO = new UserDto();
            userDTO.setEmail(tgTransaction.get().getUser().getEmail());
            userDTO.setName(tgTransaction.get().getUser().getName());

            String email = userDTO.getEmail();
            String mailSubject = "Deposit Notification";
            String emailBody = "Hello " + userDTO.getName() + "\n"
                    + "You have successfully funded your wallet with N" + tgTransaction.get().getAmount() + "\n";

            mailService.sendEmail(email, mailSubject, emailBody);

        }else{
            throw new ResourceNotFound("tgTransaction", "transactionId", transactionId);
        }

        return new ResponseAPI<>("Notification sent successfully", LocalDateTime.now(), notificationDto);
    }

    @Override
    public void TransferNotification(Long senderTransactionId, Long receiverTransactionId) throws MessagingException {
        NotificationDto receiverNotificationDto = receiverNotification(senderTransactionId);
        NotificationDto senderNotificationDto = senderNotification(receiverTransactionId);
        receiverNotificationDto.setMessageBody(receiverNotificationDto.getMessageBody() + senderNotificationDto.getUser().getName());
        senderNotificationDto.setMessageBody(senderNotificationDto.getMessageBody() + receiverNotificationDto.getUser().getName());
        saveNotification(receiverNotificationDto);
        saveNotification(senderNotificationDto);

    }

    private NotificationDto senderNotification(Long senderTransactionId) throws MessagingException {
        Optional<Transaction> senderTransaction = transactionRepository.findById(senderTransactionId);
        NotificationDto notificationDto = new NotificationDto();

        if (senderTransaction.isPresent() && (ObjectUtils.containsConstant(TransactionType.values(), "DEBIT"))) {
            notificationDto.setMessageBody("You have sent N" + senderTransaction.get().getAmount() + " to ");
            notificationDto.setCreateDate(LocalDateTime.now());
            notificationDto.setUser(senderTransaction.get().getUser());
            sender.setEmail(senderTransaction.get().getUser().getEmail());
            sender.setName(senderTransaction.get().getUser().getName());

            String email = senderTransaction.get().getUser().getEmail();
            String mailSubject = TransactionType.DEBIT + "ALERT!!!";
            String emailBody = "Hello " + senderTransaction.get().getUser().getName() + "\n"
                    + "You have successfully sent N" + senderTransaction.get().getAmount() + " to " + receiver.getName() + "\n";

            mailService.sendEmail(email, mailSubject, emailBody);
        }else{
            throw new ResourceNotFound("senderTransaction", "senderTransactionId", senderTransactionId);
        }

        return notificationDto;
    }

    private NotificationDto receiverNotification(Long receiverTransactionId) throws MessagingException {
        Optional<Transaction> receiverTransaction = transactionRepository.findById(receiverTransactionId);
        NotificationDto notificationDto = new NotificationDto();


        if (receiverTransaction.isPresent() && (ObjectUtils.containsConstant(TransactionType.values(), "CREDIT"))) {
            notificationDto.setMessageBody("You have received N" + receiverTransaction.get().getAmount() + " from ");
            notificationDto.setCreateDate(LocalDateTime.now());
            notificationDto.setUser(receiverTransaction.get().getUser());

            receiver.setEmail(receiverTransaction.get().getUser().getEmail());
            receiver.setName(receiverTransaction.get().getUser().getName());
            String email = receiverTransaction.get().getUser().getEmail();
            String mailSubject = TransactionType.CREDIT + "ALERT!!!";
            String emailBody = "Hello " + receiverTransaction.get().getUser().getName() + "\n"
                    + "You have received N" + receiverTransaction.get().getAmount() + " from " + sender.getName() + "\n";

            mailService.sendEmail(email, mailSubject, emailBody);
        }else{
            throw new ResourceNotFound("receiverTransaction", "receiverTransactionId", receiverTransactionId);
        }
        return notificationDto;
    }


    public NotificationDto convertToDto(Notification notification) {
        return mapper.map(notification, NotificationDto.class);
    }

    public Notification convertToEntity(NotificationDto notificationDto) {
        return mapper.map(notificationDto, Notification.class);
    }
}
