//package com.decagon.rewardyourteacherapi.repositoryTest;
//
//import com.decagon.rewardyourteacherapi.configuration.PasswordConfig;
//import com.decagon.rewardyourteacherapi.entity.*;
//import com.decagon.rewardyourteacherapi.enums.Roles;
//import com.decagon.rewardyourteacherapi.enums.TransactionType;
//import com.decagon.rewardyourteacherapi.repository.*;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.test.annotation.Rollback;
//
//import java.math.BigDecimal;
//import java.util.UUID;
//
///**
// * @author ifeoluwa on 19/09/2022
// * @project
// */
//
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@Rollback(value = true)
////@Transactional
//public class EntitiesRepoTest {
//
//    @Autowired
//    private UserRepository userRepository;
//    @Autowired
//    private MessageRepository messageRepository;
//    @Autowired
//    private SchoolRepository schoolRepository;
//    @Autowired
//    private SubjectsRepository subjectsRepository;
//    @Autowired
//    private TransactionRepository transactionRepository;
//    @Autowired
//    private WalletRepository walletRepository;
//
//    @Test
//    public void entitiesSaveTest() {
//        User user = new User();
//        School school = new School("British International School Lagos", "secondary",
//                "1, Landbridge Avenue, Oniru Private Estate, Victoria Island, Lagos Nigeria",
//                "ikeja", "Lagos");
//
//        Subjects subject = new Subjects("Data processing", user);
//        Wallet wallet = new Wallet();
//        wallet.setWalletAddress(UUID.randomUUID().toString());
//        wallet.setBalance(new BigDecimal(24.5));
//        Transaction transaction = new Transaction(TransactionType.CREDIT, 500L, user);
//
//        user.setRole(Roles.STUDENT);
//        user.setEmail("georges@gmail.com");
//        user.setName("George Best");
//        user.setPassword(new PasswordConfig().passwordEncoder().encode("passwor"));
//        user.setSchool(school.getSchoolName());
//        user.setWallet(wallet);
//
//        userRepository.save(user);
//        schoolRepository.save(school);
//        subjectsRepository.save(subject);
//        walletRepository.save(wallet);
//        transactionRepository.save(transaction);
//
//
//        Assertions.assertThat(transaction).isNotNull();
//        Assertions.assertThat(transaction.getId()).isGreaterThan(0);
//        Assertions.assertThat(transaction.getUser().getEmail()).isEqualTo("georges@gmail.com");
//
//
//    }
//
//}
