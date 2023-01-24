package com.decagon.rewardyourteacherapi.integrationtest;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@ActiveProfiles("test")
@SpringBootTest
@TestPropertySource("/application-test.properties")
@Rollback(false)
public class MailServiceTest {
}
