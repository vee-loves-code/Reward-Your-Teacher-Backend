package com.decagon.rewardyourteacherapi.controller;

import com.decagon.rewardyourteacherapi.dto.*;
import com.decagon.rewardyourteacherapi.entity.Notification;
import com.decagon.rewardyourteacherapi.response.ResponseAPI;
import com.decagon.rewardyourteacherapi.service.UserService;
import com.decagon.rewardyourteacherapi.serviceImpl.MailService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping(value = "/api")
public class UserController {

    private final UserService userService;

    private final MailService mailService;


    @Autowired
    public UserController(UserService userService, MailService mailService) {
        this.userService = userService;
        this.mailService = mailService;
    }

    @PostMapping(value = "/teachers/registration")
    public ResponseEntity<Object> teacherRegistration(@RequestBody TeacherDto teacherDto) throws MessagingException {

        ResponseAPI<TeacherDto> teacherResponse = userService.TeacherSignUp(teacherDto);

        UserDto userDTO = new UserDto();
        userDTO.setName(teacherDto.getName());
        userDTO.setEmail(teacherDto.getEmail());

        String email = userDTO.getEmail();
        String mailSubject = "Welcome to RewardYourTeacher";
        String emailBody = "Hello " + userDTO.getName() + "\n"
                + "You have successfully registered on my RewardYourTeacher Application, " +
                "where you get rewarded by your teacher students.";

//        mailService.sendEmail(email, mailSubject, emailBody);


        return new ResponseEntity<>("Registration successful" + "\n" + "welcome " + userDTO.getName(), CREATED);

    }

    @PostMapping(value = "/students/registration")
    public ResponseEntity<Object> studentRegistration(@RequestBody StudentDto studentDto) throws MessagingException {

        ResponseAPI<StudentDto> studentResponse = userService.StudentSignUp(studentDto);

        UserDto userDTO = new UserDto();
        userDTO.setName(studentDto.getName());
        userDTO.setEmail(studentDto.getEmail());

        String email = userDTO.getEmail();
        String mailSubject = "Welcome to RewardYourTeacher";
        String emailBody = "Hello " + userDTO.getName() + "\n"
                + "You have successfully registered on my RewardYourTeacher Application, " +
                "where you get to reward your teacher by sending funds to their wallet.";

//        mailService.sendEmail(email, mailSubject, emailBody);

        return new ResponseEntity<>("Registration successful" + "\n" + "welcome " + userDTO.getName(), CREATED);

    }

    @GetMapping(value = "/view/teacher/{id}")
    public ResponseEntity<Object> viewParticularTeacher(@PathVariable("id") long id) {
        return new ResponseEntity<>(userService.viewTeacher(id), OK);
    }

    @GetMapping(value = "/search/teacher/{name}")
    public ResponseEntity<Object> searchForTeacher(@PathVariable("name") String name) {
        return new ResponseEntity<>(userService.searchForTeacher(name), OK);
    }

    @GetMapping(value = "/retrieve/teachers/{page}/{size}")
    public ResponseEntity<Object> retrieveTeacher(
            @PathVariable("page") int page,
            @PathVariable("size") int size) {
        return ResponseEntity.ok().body(userService.retrieveTeacher(page, size));
    }

    @GetMapping(value = "/retrieve/teachers/{schoolName}/{pageNo}/{pageSize}")
    public ResponseEntity<List<TeacherResponseDto>> retrieveAllTeachersInASchool(@PathVariable("schoolName") String school,
                                                                                 @PathVariable("pageNo") int pageNo,
                                                                                 @PathVariable("pageSize") int pageSize) {

        List<TeacherResponseDto> teachers = userService.retrieveAllTeachersBySchool(school, pageNo, pageSize);
        return new ResponseEntity<>(teachers, OK);
    }


    @GetMapping(value = "/retrieve-balance")
    public ResponseEntity<?> currentUserBalance() {
        return new ResponseEntity<>(userService.userWalletBalance(), OK);
    }

    @GetMapping(value = "/retrieve-debits")
    public ResponseEntity<?> totalDebits() {
        return new ResponseEntity<>(userService.totalMoneySent(), OK);
    }


    @GetMapping(value = "/retrieve/notifications/{userId}")
    public ResponseEntity<Page<Notification>> retrieveUserNotifications(@PathVariable("userId") Long id) {
        Page<Notification> userNotifications = userService.retrieveNotifications(id);
        return new ResponseEntity<>(userNotifications, OK);
    }

    @GetMapping(value = "/student/get-student-profile/{studentId}")
    @ApiOperation(value = "Returns the existing student's profile to the UI for editing")
    public ResponseEntity<Object> getExistingStudentProfile(@PathVariable("studentId") Long studentId) {
        return userService.getExistingStudentProfile(studentId);
    }

    @GetMapping("/teacher/get-teacher-profile/{teacherId}")
    @ApiOperation(value = "Returns the existing teacher's profile to the UI for editing")
    public ResponseEntity<Object> getExistingTeacherProfile(@PathVariable("teacherId") Long teacherId) {
        return userService.getExistingTeacherProfile(teacherId);
    }

    @PostMapping("/student/update-student-profile/{studentId}")
    @ApiOperation(value = "Updates student's profile")
    public ResponseEntity<Object> updateStudentProfile(@RequestBody StudentEditDto studentEditDto,
                                                            @PathVariable Long studentId) {
        return userService.updateStudent(studentEditDto,studentId);
    }

    @PostMapping("/teacher/update-teacher-profile/{teacherId}")
    @ApiOperation(value = "Updates teacher's profile")
    public ResponseEntity<Object> updateTeacherProfile(@RequestBody TeacherEditDto teacherEditDto,
                                                            @PathVariable("teacherId") Long teacherId) {
        return userService.updateTeacher(teacherEditDto, teacherId);
    }

    @PostMapping("/teacher-appreciates-student/{teacherId}/{studentId}")
    @ApiOperation(value = "Sends an appreciation notification to a student for a reward")
    public ResponseEntity<Object> teacherAppreciatesStudentForReward(@PathVariable("teacherId") Long teacherId,
                                                                     @PathVariable("studentId") Long studentId) throws MessagingException {
        return userService.teacherAppreciatesStudentForReward(teacherId,studentId);
    }

    @GetMapping("/user/{email}")
    public String getUserByEmail(@PathVariable("email") String email) {
        return userService.getUserRole(email);
    }

}
