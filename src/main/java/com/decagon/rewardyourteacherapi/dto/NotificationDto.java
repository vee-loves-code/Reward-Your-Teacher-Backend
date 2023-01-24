package com.decagon.rewardyourteacherapi.dto;

import com.decagon.rewardyourteacherapi.entity.User;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
public class NotificationDto {

    @CreationTimestamp
    private LocalDateTime createDate;
    @UpdateTimestamp
    private LocalDateTime updateDate;

    private User user;
    private String messageBody;
}
