package com.example.webflux.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary(
            @Value("${spring.cloudinary.cloud-name}") String cloudname,
            @Value("${spring.cloudinary.api-key}") String apikey,
            @Value("${spring.cloudinary.api-secret}") String secret) {
        return new Cloudinary(ObjectUtils.asMap("cloud_name", cloudname, "api_key", apikey, "api_secret", secret));
    }

}