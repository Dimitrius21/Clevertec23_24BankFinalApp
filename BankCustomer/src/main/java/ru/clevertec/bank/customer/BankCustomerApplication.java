package ru.clevertec.bank.customer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class BankCustomerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankCustomerApplication.class, args);
    }

}
