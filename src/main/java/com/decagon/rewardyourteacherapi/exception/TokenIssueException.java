package com.decagon.rewardyourteacherapi.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class TokenIssueException extends RuntimeException {

    private HttpStatus status;
    private String message;

    @Override
    public String getMessage() {
        return message;
    }
}