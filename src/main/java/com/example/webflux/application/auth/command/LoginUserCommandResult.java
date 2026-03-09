package com.example.webflux.application.auth.command;

public record LoginUserCommandResult(String accessToken, String refreshRaw) {

}
