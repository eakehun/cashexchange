package com.hourfun.cashexchange.model.telegram;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class TelegramMessage {

	@JsonProperty("chat_id")
	private String chatId; 
	private String text;

}
