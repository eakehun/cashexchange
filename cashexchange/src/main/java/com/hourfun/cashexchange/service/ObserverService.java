package com.hourfun.cashexchange.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ObserverService {
	private static final Logger logger = LoggerFactory.getLogger(ObserverService.class);
	
	public void observerMessageLogging(String message) {
		logger.error(message);
	}

}
