package com.takarub.AuthJwtTemplate.service;

import com.takarub.AuthJwtTemplate.dto.MailBody;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl {

    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String senderEmail;
    public void sendSimpleMessage(MailBody mailBody){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailBody.to());
        message.setText(mailBody.text());
        message.setFrom("th2aer1990@hotmail.com");
        message.setSubject(mailBody.subject());
        javaMailSender.send(message);
    }


}
