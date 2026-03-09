package com.example.webflux.application.emailVerificationToken.commands;

import java.util.UUID;

public record SendEmailCommand(UUID userId, String email) {

}
