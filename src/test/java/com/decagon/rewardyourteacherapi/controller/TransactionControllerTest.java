package com.decagon.rewardyourteacherapi.controller;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import com.decagon.rewardyourteacherapi.dto.SenderTransferDto;
import com.decagon.rewardyourteacherapi.dto.TransferDto;
import com.decagon.rewardyourteacherapi.response.PaymentResponse;
import com.decagon.rewardyourteacherapi.serviceImpl.TransactionServiceImpl;
import com.decagon.rewardyourteacherapi.utils.PaymentRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

@ContextConfiguration(classes = {TransactionController.class})
@ExtendWith(SpringExtension.class)
class TransactionControllerTest {

    @Autowired
    private TransactionController transactionController;

    @MockBean
    private TransactionServiceImpl transactionServiceImpl;


    /**
     * Method under test: {@link TransactionController#getTransactionHistory(Authentication, HttpSession, Model)}
     */
    @Test
    void testGetTransactionHistory() throws Exception {
        when(transactionServiceImpl.getTransactionHistory((String) any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/users/transactions");
        MockMvcBuilders.standaloneSetup(transactionController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"message\":\"success\",\"transactions\":[]}"));
    }

    /**
     * Method under test: {@link TransactionController#sendTeacherReward(Long, Long, SenderTransferDto)}
     */
    @Test
    void testSendTeacherReward() throws Exception {
        LocalDateTime fundTransferTime = LocalDateTime.of(1, 1, 1, 1, 1);
        when(transactionServiceImpl.rewardTeacher((Long) any(), (Long) any(), (SenderTransferDto) any())).thenReturn(
                new TransferDto("Receiver Name", fundTransferTime, BigDecimal.valueOf(42L), "Not all who wander are lost"));

        SenderTransferDto senderTransferDto = new SenderTransferDto();
        senderTransferDto.setAmountToSend(BigDecimal.valueOf(42L));
        String content = (new ObjectMapper()).writeValueAsString(senderTransferDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/users/transfers/{senderId}/{receiverId}", 123L, 123L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(transactionController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"receiverName\":\"Receiver Name\",\"fundTransferTime\":[1,1,1,1,1],\"amountTosend\":42,\"message\":\"Not all"
                                        + " who wander are lost\"}"));
    }


}

