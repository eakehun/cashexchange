package com.hourfun.cashexchange.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hourfun.cashexchange.service.TelegramSender;

@Component
public class NotificationManager {
	
	private Logger logger = LoggerFactory.getLogger(NotificationManager.class);


	@Autowired
	private TelegramSender telegramSender;

	public void sendNotification() { 
		logger.info("#### send Notification.");
		String contents = generatedMessage();
		telegramSender.sendTelegram(contents);


	}
	
	private String generatedMessage() { 
		StringBuilder sb = new StringBuilder(); 
		sb.append("[Notification]").append(System.getProperty("line.separator")) 
		.append("[Name] : ").append("Tester").append(System.getProperty("line.separator")) 
		.append("[Message] : ").append("테스트 메시지 !!"); return sb.toString(); 
	}
}
