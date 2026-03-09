package com.example.webflux.application.auth.command;

import com.example.webflux.domain.auth.models.UserModelDomain;

public record VerifiedUserCommandResult(UserModelDomain user) {

}
