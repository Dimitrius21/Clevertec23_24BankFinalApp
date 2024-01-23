package ru.clevertec.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@EnableConfigServer
@SpringBootApplication
public class BankConfigApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankConfigApplication.class, args);
	}

}
