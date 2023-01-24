//package com.decagon.rewardyourteacherapi.serviceImpl;
//import com.decagon.rewardyourteacherapi.dto.StudentDto;
//import com.decagon.rewardyourteacherapi.dto.TeacherDto;
//import com.decagon.rewardyourteacherapi.entity.*;
//import com.decagon.rewardyourteacherapi.enums.Roles;
//import com.decagon.rewardyourteacherapi.enums.SchoolType;
//import com.decagon.rewardyourteacherapi.repository.SchoolRepository;
//import com.decagon.rewardyourteacherapi.repository.SubjectRepository;
//import com.decagon.rewardyourteacherapi.repository.UserRepository;
//import com.decagon.rewardyourteacherapi.response.ResponseAPI;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.springframework.boot.test.context.SpringBootTest;
//import java.time.LocalDateTime;
//import java.time.Month;
//import java.util.ArrayList;
//import java.util.List;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//@SpringBootTest
//class UserServiceImplTest {
//    @Mock
//    UserRepository userRepository;
//    @Mock
//    SchoolRepository schoolRepository;
//    @Mock
//    SubjectRepository subjectRepository;
//    @InjectMocks
//    UserServiceImpl userServiceImpl;
//    private Teacher teacher;
//    private Student student;
//    private List<Subjects> subjectsList = new ArrayList<>();
//    private LocalDateTime time;
//    @BeforeEach
//    void setUp() {
//        time = LocalDateTime.of(2022, Month.SEPTEMBER, 22, 7, 2, 20, 3000);
//        teacher= new Teacher(1L, "TryGod", "trygodnwakwasi@gmail.com", "1234", Roles.TEACHER, "FGC", 25, SchoolType.SECONDARY, subjectsList);
//        student = new Student(2L, "Amanda", "amanda@gmail.com", "1212", Roles.STUDENT, "FGC");
//    }
//    @Test
//    void teacherSignUp() {
//        List<String> ListOfSubjects = new ArrayList<>();
//        TeacherDto teacherDto = new TeacherDto("TryGod", "trygodnwakwasi@gmail.com", "1234", "FGC", 25, ListOfSubjects, SchoolType.SECONDARY);
//        var actual = userServiceImpl.TeacherSignUp(teacherDto);
//        ResponseAPI<TeacherDto> teacherResponse = new ResponseAPI<>("sucess", time, teacherDto);
//        assertEquals(teacherResponse.getDto().getName(), actual.getDto().getName());
//        assertEquals(teacherResponse.getDto().getEmail(), actual.getDto().getEmail());
//        assertEquals(teacherResponse.getDto().getPassword(), actual.getDto().getPassword());
//        assertEquals(teacherResponse.getDto().getSchool(), actual.getDto().getSchool());
//        assertEquals(teacherResponse.getDto().getYearsOfTeaching(), actual.getDto().getYearsOfTeaching());
//        assertEquals(teacherResponse.getDto().getSubjectsList(), actual.getDto().getSubjectsList());
//        assertEquals(teacherResponse.getDto().getSchoolType(), actual.getDto().getSchoolType());
//    }
//    @Test
//    void studentSignUp() {
//        StudentDto studentDto = new StudentDto("Amanda", "amanda@gmail.com", "1212", "FGC");
//        var actual = userServiceImpl.StudentSignUp(studentDto);
//        ResponseAPI<StudentDto> studentResponse = new ResponseAPI<>("success", time, studentDto);
//        assertEquals(studentResponse.getDto().getName(), actual.getDto().getName());
//        assertEquals(studentResponse.getDto().getEmail(), actual.getDto().getEmail());
//        assertEquals(studentResponse.getDto().getPassword(), actual.getDto().getPassword());
//        assertEquals(studentResponse.getDto().getSchool(), actual.getDto().getSchool());
//    }
//}