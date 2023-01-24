package com.decagon.rewardyourteacherapi.controller;

import com.decagon.rewardyourteacherapi.OAuth.CustomOAuth2User;
import com.decagon.rewardyourteacherapi.dto.UserDto;
import com.decagon.rewardyourteacherapi.response.LoginResponse;
import com.decagon.rewardyourteacherapi.serviceImpl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.IOException;

@RestController
@RequestMapping("/api/auth")
public class OAuthController {
    private UserServiceImpl userService;

    @Autowired
    public OAuthController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @PostMapping("/social/login")
    public ResponseEntity<LoginResponse> socialLogin(@RequestBody UserDto userDto ) {
        Authentication userDetails =  SecurityContextHolder.getContext().getAuthentication();
        String token = userService.processOAuthUser(userDto, userDetails);
        return ResponseEntity.ok(new LoginResponse(token));
    }

}
