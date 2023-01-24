package com.decagon.rewardyourteacherapi.response;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class APIResponse<T> {
        private String message;
        private Boolean status;
        private T payload;
}

