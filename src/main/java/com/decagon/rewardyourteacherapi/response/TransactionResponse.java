package com.decagon.rewardyourteacherapi.response;

import com.decagon.rewardyourteacherapi.entity.Transaction;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @author ifeoluwa on 20/09/2022
 * @project
 */
@AllArgsConstructor
@Data
public class TransactionResponse {
    private String message;
    private List<Transaction> transactions;

}
