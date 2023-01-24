package com.decagon.rewardyourteacherapi.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class StudentDto {

    private String name;

    private String email;

    private String password;


    private String school;
}
