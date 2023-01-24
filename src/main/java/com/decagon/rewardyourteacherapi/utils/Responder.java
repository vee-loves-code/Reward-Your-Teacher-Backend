package com.decagon.rewardyourteacherapi.utils;

import com.decagon.rewardyourteacherapi.response.APIResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

    @Service
    @AllArgsConstructor
    public class Responder<T> {

        public ResponseEntity<APIResponse> Okay(String message, T response){
            return  new  ResponseEntity<>(new APIResponse(message, true, response), HttpStatus.OK);
        }

        public ResponseEntity<APIResponse> NotFound(String message){
            return  new ResponseEntity<>(new APIResponse(message, false, null), HttpStatus.NOT_FOUND);
        }
    }
