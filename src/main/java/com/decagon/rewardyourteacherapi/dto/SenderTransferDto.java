package com.decagon.rewardyourteacherapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SenderTransferDto {
    private BigDecimal amountToSend;
}
