package com.decagon.rewardyourteacherapi.controller;


import com.decagon.rewardyourteacherapi.dto.SenderTransferDto;
import com.decagon.rewardyourteacherapi.dto.TransferDto;
import com.decagon.rewardyourteacherapi.entity.Transaction;
import com.decagon.rewardyourteacherapi.response.PaymentResponse;
import com.decagon.rewardyourteacherapi.response.TransactionResponse;
import com.decagon.rewardyourteacherapi.serviceImpl.TransactionServiceImpl;
import com.decagon.rewardyourteacherapi.utils.PaymentRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;


/**
 * @author ifeoluwa on 18/09/2022
 * @project
 */

//   /api/t
@RestController
@RequestMapping(value = "/api/users")
public class TransactionController {
    private final TransactionServiceImpl transactionService;
    private PaymentResponse paymentResponse = new PaymentResponse();


    @Autowired
    public TransactionController(TransactionServiceImpl transactionService) {
        this.transactionService = transactionService;
    }
    @PostMapping(value = "/deposit")
    public ResponseEntity<?> deposit(@RequestBody PaymentRequest paymentRequest) throws Exception {

        paymentResponse = transactionService.initDeposit(paymentRequest);
        return new ResponseEntity<>(paymentResponse, HttpStatus.OK);
    }
    @GetMapping(value = "/callback")
    public void payStackResponse(HttpServletResponse response) throws Exception {
        transactionService.verifyTransaction(paymentResponse.getData().getReference());
        response.sendRedirect("http://localhost:3000/studentdashboard");
    }
    @GetMapping("/transactions")
    public ResponseEntity<TransactionResponse> getTransactionHistory(Authentication authentication, HttpSession session, Model model) {
        String email = (String) session.getAttribute("loggedUserEmail");
        List<Transaction> userTransaction = transactionService.getTransactionHistory(email);
        model.addAttribute("userTransaction", userTransaction);
        return new ResponseEntity<>(new TransactionResponse("success", userTransaction), HttpStatus.OK);
    }
    @PostMapping("/transfers/{senderId}/{receiverId}")
    public ResponseEntity<TransferDto> sendTeacherReward(@PathVariable("senderId") Long senderId, @PathVariable("receiverId") Long receiverId, @RequestBody SenderTransferDto senderTransferDto) throws Exception {

        return new ResponseEntity<>(transactionService.rewardTeacher(senderId,receiverId, senderTransferDto),HttpStatus.OK);
    }
}
