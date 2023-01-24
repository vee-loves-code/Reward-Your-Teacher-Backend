package com.decagon.rewardyourteacherapi.service;


import com.decagon.rewardyourteacherapi.dto.SenderTransferDto;
import com.decagon.rewardyourteacherapi.dto.TransferDto;
import com.decagon.rewardyourteacherapi.entity.Transaction;
import com.decagon.rewardyourteacherapi.response.APIResponse;
import com.decagon.rewardyourteacherapi.response.PaymentResponse;
import com.decagon.rewardyourteacherapi.utils.PaymentRequest;
import com.decagon.rewardyourteacherapi.utils.VerifyTransactionResponse;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.List;
import java.security.Principal;

@Service
public interface TransactionService {


    PaymentResponse initDeposit(PaymentRequest paymentRequest) throws Exception;
    VerifyTransactionResponse verifyTransaction(String reference) throws Exception;
//    Transaction createTransaction(TransactionDTO transactionDTO);
    List<Transaction> getTransactionHistory(String userEmail);


    TransferDto rewardTeacher(Long studentId, Long teacherId, SenderTransferDto senderTransferDto) throws Exception;




}
