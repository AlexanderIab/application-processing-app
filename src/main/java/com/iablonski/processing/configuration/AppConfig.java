package com.iablonski.processing.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:dadata.properties")
public class AppConfig {
}