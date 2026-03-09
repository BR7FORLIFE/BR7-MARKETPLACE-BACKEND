package com.example.webflux.application.Authorization.usecases;

import com.example.webflux.application.Authorization.command.AssigmentUserRolCommand;
import com.example.webflux.application.Authorization.command.AssigmentUserRolCommandResult;

import reactor.core.publisher.Mono;

public interface AuthorizationRolUseCase {
    Mono<AssigmentUserRolCommandResult> assigmentRolUser(AssigmentUserRolCommand cmd);
}
