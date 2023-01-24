package com.decagon.rewardyourteacherapi.dto;

import com.decagon.rewardyourteacherapi.enums.SchoolType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TeacherEditDto {

        private String name;

        private String email;

        private String password;

        private String school;
        private String about;
        private String phoneNumber;

        private int yearsOfTeaching;

        private List<String> subjectsList;

        private SchoolType schoolType;

    }
