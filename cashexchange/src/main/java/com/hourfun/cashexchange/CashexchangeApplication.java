package com.hourfun.cashexchange;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.telegram.telegrambots.ApiContextInitializer;

@EnableCaching
@SpringBootApplication
public class CashexchangeApplication {

	public static void main(String[] args) {
		ApiContextInitializer.init();
		SpringApplication.run(CashexchangeApplication.class, args);
	}

}
