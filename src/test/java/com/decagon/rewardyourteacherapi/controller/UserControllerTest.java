package com.decagon.rewardyourteacherapi.controller;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.decagon.rewardyourteacherapi.dto.StudentDto;
import com.decagon.rewardyourteacherapi.dto.TeacherDto;
import com.decagon.rewardyourteacherapi.dto.UserDto;
import com.decagon.rewardyourteacherapi.enums.SchoolType;
import com.decagon.rewardyourteacherapi.response.ResponseAPI;
import com.decagon.rewardyourteacherapi.service.UserService;
import com.decagon.rewardyourteacherapi.serviceImpl.MailService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {UserController.class})
@ExtendWith(SpringExtension.class)
class UserControllerTest {
    @MockBean
    private MailService mailService;

    @Autowired
    private UserController userController;

    @MockBean
    private UserService userService;


    @Test
    void testStudentRegistration() throws Exception {
        LocalDateTime time = LocalDateTime.of(1, 1, 1, 1, 1);
        when(userService.StudentSignUp((StudentDto) any())).thenReturn(new ResponseAPI<>("Not all who wander are lost",
                time, new StudentDto("jane", "jane.doe@example.org", "iloveyou", "School")));
        doNothing().when(mailService).sendEmail((String) any(), (String) any(), (String) any());
        MockHttpServletRequestBuilder contentTypeResult = MockMvcRequestBuilders.post("/api/students/registration")
                .contentType(MediaType.APPLICATION_JSON);

        ObjectMapper objectMapper = new ObjectMapper();
        MockHttpServletRequestBuilder requestBuilder = contentTypeResult
                .content(objectMapper.writeValueAsString(new StudentDto()));
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Registration successful\nwelcome null"));
    }

    /**
     * Method under test: {@link UserController#teacherRegistration(TeacherDto)}
     */
    @Test
    void testTeacherRegistration() throws Exception {
        LocalDateTime time = LocalDateTime.of(1, 1, 1, 1, 1);
        when(userService.TeacherSignUp((TeacherDto) any()))
                .thenReturn(new ResponseAPI<>("Not all who wander are lost", time, new TeacherDto()));
        doNothing().when(mailService).sendEmail((String) any(), (String) any(), (String) any());

        TeacherDto teacherDto = new TeacherDto();
        teacherDto.setEmail("jane.doe@example.org");
        teacherDto.setName("Name");
        teacherDto.setPassword("iloveyou");
        teacherDto.setSchool("School");
        teacherDto.setSchoolType(SchoolType.PRIMARY);
        teacherDto.setSubjectsList(new ArrayList<>());
        teacherDto.setYearsOfTeaching(1);
        String content = (new ObjectMapper()).writeValueAsString(teacherDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/teachers/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Registration successful\nwelcome Name"));
    }


}

