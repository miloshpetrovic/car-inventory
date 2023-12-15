package com.smg.carinventory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableRetry
public class CarInventoryApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarInventoryApplication.class, args);
	}

}
