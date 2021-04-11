package com.hourfun.cashexchange;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.telegram.telegrambots.ApiContextInitializer;

@EnableCaching
@SpringBootApplication
//@EnableAsync
public class CashexchangeApplication {

	public static void main(String[] args) {
		ApiContextInitializer.init();
		SpringApplication.run(CashexchangeApplication.class, args);
	}
	
	@PostConstruct
    void started() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
    }

}
