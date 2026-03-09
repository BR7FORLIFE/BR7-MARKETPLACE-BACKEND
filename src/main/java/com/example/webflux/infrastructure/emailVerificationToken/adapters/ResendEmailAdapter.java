package com.example.webflux.infrastructure.emailVerificationToken.adapters;

import org.springframework.stereotype.Component;

import com.example.webflux.application.emailVerificationToken.model.SendEmailParams;
import com.example.webflux.application.emailVerificationToken.ports.SendEmailPort;
import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Component
public class ResendEmailAdapter implements SendEmailPort {

    private final Resend resend;

    public ResendEmailAdapter(Resend resend) {
        this.resend = resend;
    }

    @Override
    public Mono<String> sendEmail(SendEmailParams params) {
        return Mono.fromCallable(() -> {

            CreateEmailOptions options = CreateEmailOptions.builder()
                    .from(params.emailFrom())
                    .to(params.emailTo())
                    .subject(params.subject())
                    .html(params.html())
                    .build();

            CreateEmailResponse response = resend.emails().send(options);

            return response.getId(); 

        }).subscribeOn(Schedulers.boundedElastic())
                .onErrorMap(ResendException.class,
                        ex -> new RuntimeException("Error sending email: " + ex.getMessage()));
    }
}
