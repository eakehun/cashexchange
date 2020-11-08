package com.hourfun.cashexchange.service;

import java.util.Date;

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

    
    public void ontToOneInquirySend(String sendAddress,Date registDate) throws MessagingException {
    	Mail mail = new Mail();
    	mail.setTitle("1:1문의 답변안내");
    	mail.setAddress(sendAddress);
    	mail.setMessage("등록 날짜는 "+registDate);
    	MimeMessage message = mailSender.createMimeMessage();
    	MimeMessageHelper helper = new MimeMessageHelper(message,true);
    	
    	helper.setSubject(mail.getTitle());
    	helper.setFrom(FROM_ADDRESS);
    	helper.setTo(mail.getAddress());
    	
    	Context context = new Context();
    	context.setVariable("contents", mail.getMessage());
    	String html = templateEngine.process("mail", context);
    	helper.setText(html,true);

        mailSender.send(message);
    }
    
    
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
    
    public void welcomeMailSend(String address, String name) throws MessagingException {
    	String title = "회원가입 환영";
    	
    	MimeMessage message = mailSender.createMimeMessage();
    	MimeMessageHelper helper = new MimeMessageHelper(message,true);
    	
    	helper.setSubject(title);
    	helper.setFrom(FROM_ADDRESS);
    	helper.setTo(address);
    	
    	Context context = new Context();
    	context.setVariable("name", name);
    	
    	String html = templateEngine.process("welcome", context);
    	helper.setText(html,true);
    	
    	mailSender.send(message);
    	
    }
}
