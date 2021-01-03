package com.hourfun.cashexchange.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ObserverService {
	private static final Logger logger = LoggerFactory.getLogger(ObserverService.class);
	
	@Autowired
	TelegramSender sender;
	
	public void observerMessageLogging(String message) {
		logger.error(message);
		sender.sendTelegram("macro -- " + message);
	}

}
