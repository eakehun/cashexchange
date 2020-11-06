package com.hourfun.cashexchange.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.hourfun.cashexchange.model.Mail;
import com.hourfun.cashexchange.model.Users;

@Service
public class MailService {
	@Autowired
    private JavaMailSender mailSender;
	
	@Autowired
    private SpringTemplateEngine templateEngine;
    private static final String FROM_ADDRESS = "cs<cs@makepin.co.kr>";

    public void mailSend() throws MessagingException {
    	Mail mail = new Mail();
    	mail.setTitle("test");
    	mail.setAddress("zest1333@gmail.com");
    	mail.setMessage("aaaa");
    	MimeMessage message = mailSender.createMimeMessage();
    	MimeMessageHelper helper = new MimeMessageHelper(message,true);
    	
    	helper.setSubject(mail.getTitle());
    	helper.setFrom(FROM_ADDRESS);
    	helper.setTo(mail.getAddress());
    	
    	Context context = new Context();
    	context.setVariable("contents", "aaaaaaa");
    	String html = templateEngine.process("mail", context);
    	helper.setText(html,true);
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(mail.getAddress());
//        message.setFrom(MailService.FROM_ADDRESS);
//        message.setSubject(mail.getTitle());
//        message.setText(mail.getMessage());

        mailSender.send(message);
    }
    
    public void welcomeMailSend(Users users) {
    	
    }
}