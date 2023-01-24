package com.decagon.rewardyourteacherapi.dto;

import lombok.Data;

@Data
public class TeacherResponseDto {
   private Long id;
   private String name;
   private String email;
   private String phoneNumber;
   private String about;
   private String school;
   private int yearsOfExperience;
   private String position;
   private String periodOfTeaching;

}
