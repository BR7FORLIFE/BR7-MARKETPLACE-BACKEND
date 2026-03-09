package com.example.webflux.application.auth.usecases;

import java.util.UUID;

import com.example.webflux.application.auth.command.LoginUserCommand;
import com.example.webflux.application.auth.command.LoginUserCommandResult;
import com.example.webflux.application.auth.command.RegisterUserCommand;
import com.example.webflux.application.auth.command.RegisterUserCommandResult;
import com.example.webflux.application.auth.command.VerifiedUserCommandResult;

import reactor.core.publisher.Mono;

public interface AuthUseCase {
    Mono<RegisterUserCommandResult> executeRegister(RegisterUserCommand cmd);

    Mono<VerifiedUserCommandResult> verifyUser(UUID userId);

    Mono<LoginUserCommandResult> executeLogin(LoginUserCommand cmd);
}
