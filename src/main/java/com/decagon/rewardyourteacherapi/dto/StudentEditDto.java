package com.decagon.rewardyourteacherapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StudentEditDto {

        private String name;

        private String email;

        private String school;

    }
