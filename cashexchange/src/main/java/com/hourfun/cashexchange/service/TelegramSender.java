package com.hourfun.cashexchange.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.hourfun.cashexchange.model.telegram.TelegramMessage;
import com.hourfun.cashexchange.util.JsonUtils;


@Service
public class TelegramSender {
	private static final Logger logger = LoggerFactory.getLogger(TelegramSender.class);

	@Value("${notification.telegram.enabled}")
	private boolean telegramEnabled;

	@Value("${notification.telegram.bot.token}")
	private String token;

	@Value("${notification.telegram.chat.id}")
	private String chatId;

	public void sendTelegram(String contents) {
		if (telegramEnabled) {
			// make a request URL using telegram bot api
			String url = "https://api.telegram.org/bot" + token + "/sendMessage";
			try {
				TelegramMessage telegramMessage = new TelegramMessage();
				telegramMessage.setChatId(chatId);
				telegramMessage.setText(contents);

				String param = JsonUtils.toJson(telegramMessage);

				RestTemplate restTemplate = new RestTemplate();
				HttpHeaders headers = new HttpHeaders();
				headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE); // send the post request
				HttpEntity<String> entity = new HttpEntity<>(param, headers);
				restTemplate.postForEntity(url, entity, String.class);
			} catch (Exception e) {
				logger.error("Unhandled exception occurred while send Telegram.", e);
			}

		}

	}
}
