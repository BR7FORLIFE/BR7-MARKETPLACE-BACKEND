package com.example.webflux.application.emailVerificationToken.ports;

import com.example.webflux.application.emailVerificationToken.model.SendEmailParams;

import reactor.core.publisher.Mono;

public interface SendEmailPort {
    Mono<String> sendEmail(SendEmailParams params);
}
