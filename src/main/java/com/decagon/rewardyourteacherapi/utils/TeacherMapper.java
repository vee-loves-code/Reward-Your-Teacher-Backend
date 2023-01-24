package com.decagon.rewardyourteacherapi.utils;

import com.decagon.rewardyourteacherapi.dto.TeacherResponseDto;
import com.decagon.rewardyourteacherapi.entity.Teacher;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TeacherMapper {


    public TeacherResponseDto teacherEntityToTeacherResponseDtoMapper(Teacher teacher) {
        TeacherResponseDto teacherResponseDto = new TeacherResponseDto();
        teacherResponseDto.setId(teacher.getId());
        teacherResponseDto.setName(teacher.getName());
        teacherResponseDto.setEmail(teacher.getEmail());
        teacherResponseDto.setAbout(teacher.getAbout());
        teacherResponseDto.setPhoneNumber(teacher.getPhoneNumber());
        teacherResponseDto.setSchool(teacher.getSchool());
        teacherResponseDto.setYearsOfExperience(teacher.getYearsOfTeaching());
        teacherResponseDto.setPosition(teacher.getPosition());
        teacherResponseDto.setPeriodOfTeaching(teacher.getPeriodOfTeaching());
        return teacherResponseDto;
    }




}
