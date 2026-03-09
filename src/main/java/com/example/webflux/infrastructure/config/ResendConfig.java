package com.example.webflux.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.resend.Resend;

@Configuration
public class ResendConfig {

    @Bean
    public Resend resend(
            @Value("${spring.resend.api-key}") String apikey) {
        return new Resend(apikey);
    }
}
