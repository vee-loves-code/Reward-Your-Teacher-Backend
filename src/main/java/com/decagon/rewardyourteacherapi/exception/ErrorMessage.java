package com.decagon.rewardyourteacherapi.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorMessage {
    private Date timestamp;
    private String message;
    private String details;

    public ErrorMessage(String message) {
        this.message = message;
    }
}
