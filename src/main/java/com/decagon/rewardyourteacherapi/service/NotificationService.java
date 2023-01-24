package com.decagon.rewardyourteacherapi.service;



import com.decagon.rewardyourteacherapi.dto.NotificationDto;
import com.decagon.rewardyourteacherapi.response.ResponseAPI;

import javax.mail.MessagingException;

public interface NotificationService {

    NotificationDto saveNotification(NotificationDto notificationDto);
    ResponseAPI<NotificationDto> depositNotification(Long transactionId) throws MessagingException;
   void TransferNotification(Long senderTransactionId, Long receiverTransactionId) throws MessagingException;

}
