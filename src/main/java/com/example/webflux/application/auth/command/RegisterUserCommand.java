package com.example.webflux.application.auth.command;

public record RegisterUserCommand(String username, String password, String email) {

}
