package com.hourfun.cashexchange;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.ApiContextInitializer;

@SpringBootApplication
public class CashexchangeApplication {

	public static void main(String[] args) {
		ApiContextInitializer.init();
		SpringApplication.run(CashexchangeApplication.class, args);
	}

}
