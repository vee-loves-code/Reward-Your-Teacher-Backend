package com.decagon.rewardyourteacherapi.service;

import com.decagon.rewardyourteacherapi.dto.*;
import com.decagon.rewardyourteacherapi.entity.Notification;
import com.decagon.rewardyourteacherapi.response.APIResponse;
import com.decagon.rewardyourteacherapi.response.ResponseAPI;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import javax.mail.MessagingException;
import java.math.BigDecimal;
import java.util.List;

public interface UserService {

    ResponseAPI<TeacherDto> TeacherSignUp(TeacherDto teacherDto);

    ResponseAPI<StudentDto> StudentSignUp(StudentDto studentDto);

    ResponseAPI<TeacherDto> viewTeacher(long id);

    ResponseAPI<List<TeacherDto>> searchForTeacher(String name);

    List<TeacherResponseDto> retrieveTeacher(int page, int size);

    ResponseAPI<BigDecimal> userWalletBalance();
    ResponseAPI<BigDecimal> totalMoneySent();

    List<TeacherResponseDto> retrieveAllTeachersBySchool(String schoolName, int pageNo, int pageSize);
    ResponseAPI<BigDecimal> userWalletBalance(Long id);

    Page<Notification> retrieveNotifications(Long userId);

    ResponseEntity<Object> getExistingStudentProfile(Long studentId);

    ResponseEntity<Object> getExistingTeacherProfile(Long teacherId);

    ResponseEntity<Object> updateStudent(StudentEditDto studentEditDto, Long studentId);

    ResponseEntity<Object> updateTeacher(TeacherEditDto teacherEditDto, Long teacherId);

    ResponseEntity<Object> teacherAppreciatesStudentForReward(Long teacherId, Long studentId) throws MessagingException;

    String getUserRole(String email);
}
