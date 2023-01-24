package com.decagon.rewardyourteacherapi.dto;

import com.decagon.rewardyourteacherapi.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
@AllArgsConstructor
public class TransferDto {
    private String receiverName;
    private LocalDateTime fundTransferTime;
    private BigDecimal amountTosend;
    private String message;
}
