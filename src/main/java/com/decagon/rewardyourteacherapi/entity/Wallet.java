package com.decagon.rewardyourteacherapi.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "wallet")
public class Wallet extends BaseClass {

    private String walletAddress = UUID.randomUUID().toString();

    private BigDecimal balance;

    private long userId;

}
