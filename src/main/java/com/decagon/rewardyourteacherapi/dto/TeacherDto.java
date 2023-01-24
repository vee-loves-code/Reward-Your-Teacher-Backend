package com.decagon.rewardyourteacherapi.dto;

import com.decagon.rewardyourteacherapi.enums.Roles;
import com.decagon.rewardyourteacherapi.enums.SchoolType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TeacherDto {
    private Long id;
    private String name;
    private String email;
    private Roles role;
    private String password;
    private String school;
    private Integer yearsOfTeaching;
    private String about;
    private String position;
    private String periodOfTeaching;
    private List<String> subjectsList;
    private SchoolType schoolType;

}
