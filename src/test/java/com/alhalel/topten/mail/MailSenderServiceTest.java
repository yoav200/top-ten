package com.alhalel.topten.mail;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MailSenderServiceTest {

    @Autowired
    private MailSenderService mailSenderService;

    @Test
    void sendEmail() {

        mailSenderService.sendTestEmail();
    }
}