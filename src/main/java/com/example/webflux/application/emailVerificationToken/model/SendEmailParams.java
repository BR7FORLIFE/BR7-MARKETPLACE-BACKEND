package com.example.webflux.application.emailVerificationToken.model;

public record SendEmailParams(String emailFrom, String emailTo, String subject, String html) {

}
