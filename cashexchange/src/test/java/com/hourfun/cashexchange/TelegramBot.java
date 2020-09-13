package com.hourfun.cashexchange;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class TelegramBot extends TelegramLongPollingBot{
	private final String BOT_NAME = "zest133_bot"; //Bot Name
    private final String AUTH_KEY = "1167978791:AAHYju1l7Cxxk-kE7tyzX7UuW-u4pEF-jzM"; //Bot Auth-Key
    private final String CHAT_ID = "-1001168777089"; //Chat ID
	private String link = "https://t.me/joinchat/AAAAAEWqH4ExEv7e9lH_VQ";
	
	@Override
	public void onUpdateReceived(Update update) {
		// TODO Auto-generated method stub
		SendMessage message = new SendMessage().enableHtml(true);
		System.out.println(update.getMessage().getText());
		System.out.println(update.getMessage().getChatId());
		message.setText("bbbbbb");
		message.setChatId(CHAT_ID);
		try {
			execute(message);
		} catch (TelegramApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		return AUTH_KEY;
	}

}
