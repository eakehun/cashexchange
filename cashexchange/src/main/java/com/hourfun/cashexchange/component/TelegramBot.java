package com.hourfun.cashexchange.component;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.hourfun.cashexchange.service.CaptchaService;

@Component
public class TelegramBot extends TelegramLongPollingBot {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(TelegramBot.class);

	private final String BOT_NAME = "makepin_test_bot"; // Bot Name
	
	@Value("${notification.telegram.bot.token}")
	private String token;

	@Value("${notification.telegram.chat.id}")
	private String chatId;
	
	@Autowired
	CaptchaService captchaService;
//    
//    @Value("${notification.telegram.enabled}")
//	private boolean telegramEnabled;
//
//	@Value("${notification.telegram.bot.token}")
//	private String token;
//
//	@Value("${notification.telegram.chat.id}")
//	private String chatId;

	@SuppressWarnings("unused")
	@Override
	public void onUpdateReceived(Update update) {
		String content = update.getMessage().getText();

		if (content.startsWith("/captcha")) {
			String captchaAnswer = content.replace("/captcha ", "");
		}

	}

	@Override
	public String getBotUsername() {
		// TODO Auto-generated method stub
		return BOT_NAME;
	}

	@Override
	public String getBotToken() {
		// TODO Auto-generated method stub
		return token;
	}

//	@PostConstruct
//	public void registerBot(){
//	     TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
//	     try {
//	       telegramBotsApi.registerBot(this);
//	     } catch (TelegramApiException e) {
//	    	 logger.error(e.getMessage());
//	     }
//	}

	
	public Message sendPhoto(SendPhoto photo) throws TelegramApiException {
		return execute(photo);
	}
}
