package com.iablonski.processing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ApplicationProcessingAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApplicationProcessingAppApplication.class, args);
    }

}