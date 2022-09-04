package com.alhalel.topten.mail;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@AllArgsConstructor
public class MailSenderService {

    private JavaMailSender javaMailSender;

    void sendTestEmail() {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom("noreply@top-ten-ranker.com");
        msg.setTo("top.ten.ranker@gmail.com", "yoav200@yahoo.com");
        msg.setSubject("Testing from Spring Boot");
        msg.setText("Hello World \n Spring Boot Email");
        javaMailSender.send(msg);
    }

    public void sendEmail(ContactUsModel model) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(model.getEmail());
        msg.setTo("top.ten.ranker@gmail.com");
        msg.setSubject(model.getSubject());
        msg.setText(model.getMessage());
        javaMailSender.send(msg);

    }
}
