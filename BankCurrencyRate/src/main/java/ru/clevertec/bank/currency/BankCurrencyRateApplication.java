package ru.clevertec.bank.currency;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class BankCurrencyRateApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankCurrencyRateApplication.class, args);
	}

}
