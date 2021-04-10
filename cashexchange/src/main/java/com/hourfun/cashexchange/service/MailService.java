package com.hourfun.cashexchange.service;

import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.hourfun.cashexchange.model.Users;
import com.hourfun.cashexchange.util.DateUtils;

@Service
public class MailService {
	@Autowired
	private JavaMailSender mailSender;

	@Value("${cashexchange.service.name}")
	private String serviceName;

	@Value("${okcert.siteurl}")
	private String siteurl;

	@Autowired
	private SpringTemplateEngine templateEngine;
	private static final String FROM_ADDRESS = "<cs@makepin.co.kr>";
	
	public static int threadPoolCount = 20;

	private ScheduledExecutorService excutorService = Executors.newScheduledThreadPool(threadPoolCount); 

	
	public void ontToOneInquirySend(Users clinetUser, Date registDate) throws MessagingException {
		String title = "1:1문의 답변안내";

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);

		helper.setSubject(title);
		helper.setFrom(serviceName + FROM_ADDRESS);
		helper.setTo(clinetUser.getEmail());

		Context context = new Context();

		String replaceString = clinetUser.getName();

		String pattern = "";
		if (replaceString.length() <= 2) {
			pattern = "^(.)(.+)$";
		} else {
			pattern = "^(.)(.+)(.)$";
		}

		Matcher matcher = Pattern.compile(pattern).matcher(replaceString);

		if (matcher.matches()) {
			replaceString = "";

			for (int i = 1; i <= matcher.groupCount(); i++) {
				String replaceTarget = matcher.group(i);
				if (i == 2) {
					char[] c = new char[replaceTarget.length()];
					Arrays.fill(c, '*');

					replaceString = replaceString + String.valueOf(c);
				} else {
					replaceString = replaceString + replaceTarget;
				}

			}
		}

		String createDateString = DateUtils.changeDateToString(clinetUser.getCreateDate(), DateUtils.YYYYMMDD_DASH);
		String responseDateString = DateUtils.changeDateToString(registDate, DateUtils.YYYYMMDDHHmmss_DASH);

		context.setVariable("userName", replaceString);
		context.setVariable("responseDate", responseDateString);
		context.setVariable("createDate", createDateString);
		context.setVariable("siteurl", siteurl);

		String html = templateEngine.process("admin_makepin_mail", context);
		helper.setText(html, true);

		excutorService.submit(new Runnable() {
			@Override
			public void run() {
				try {
					mailSender.send(message);
				} catch (Exception e) {
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Exception occur while send a mail");
				}
			}
		});
	}

	public void welcomeMailSend(Users users) throws MessagingException {
		String title = serviceName + " 회원가입을 축하합니다!";

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);

		helper.setSubject(title);
		helper.setFrom(serviceName + FROM_ADDRESS);
		helper.setTo(users.getUserId());

		Context context = new Context();
		context.setVariable("email", users.getUserId());
		context.setVariable("name", users.getName());

		String createDateString = DateUtils.changeDateToString(users.getCreateDate(), DateUtils.YYYYMMDD_DASH);

		context.setVariable("createDate", createDateString);

		String html = templateEngine.process("makepin_welcome", context);
		helper.setText(html, true);

		excutorService.submit(new Runnable() {
			@Override
			public void run() {
				try {
					mailSender.send(message);
				} catch (Exception e) {
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Exception occur while send a mail");
				}
			}
		});
		

	}
}
