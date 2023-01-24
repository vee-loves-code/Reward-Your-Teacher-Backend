package com.decagon.rewardyourteacherapi.exception;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@NoArgsConstructor
@AllArgsConstructor
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException{
    private String user;
    private String userFieldName;
    private Long fieldValue;

    public UserNotFoundException(String user, String userFieldName, long fieldValue) {
        super(String.format("%s not found with %s : '%s'", user, userFieldName, fieldValue)); // EX Post not found with id :'1'
        this.user = user;
        this.userFieldName = userFieldName;
        this.fieldValue = fieldValue;
    }
    public UserNotFoundException(String user) {
        super(String.format("%s not found with %s : '%s'", user)); // EX Post not found with id :'1'
        this.user = user;

    }

}
