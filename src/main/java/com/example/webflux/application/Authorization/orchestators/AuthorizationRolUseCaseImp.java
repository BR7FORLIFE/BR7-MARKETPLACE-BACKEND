package com.example.webflux.application.Authorization.orchestators;

import org.springframework.stereotype.Service;

import com.example.webflux.application.Authorization.command.AssigmentUserRolCommand;
import com.example.webflux.application.Authorization.command.AssigmentUserRolCommandResult;
import com.example.webflux.application.Authorization.exceptions.AssigmentRolUserException;
import com.example.webflux.application.Authorization.exceptions.RolNotFoundException;
import com.example.webflux.application.Authorization.usecases.AuthorizationRolUseCase;
import com.example.webflux.domain.Authorization.models.rols.RolsUsersDomain;
import com.example.webflux.domain.Authorization.ports.rols.RolPort;
import com.example.webflux.domain.Authorization.ports.rols.RolUserRepositoryPort;

import reactor.core.publisher.Mono;

@Service
public class AuthorizationRolUseCaseImp implements AuthorizationRolUseCase {

    private final RolUserRepositoryPort rolUserPort;
    private final RolPort rolPort;

    public AuthorizationRolUseCaseImp(RolUserRepositoryPort rolUserPort, RolPort rolPort) {
        this.rolUserPort = rolUserPort;
        this.rolPort = rolPort;
    }

    @Override
    public Mono<AssigmentUserRolCommandResult> assigmentRolUser(AssigmentUserRolCommand cmd) {
        return rolPort.findByName(cmd.role())
                .switchIfEmpty(Mono.error(new RolNotFoundException()))
                .flatMap(rol -> {

                    return rolUserPort.existsByUserIdAndRol(cmd.userId(), rol.getUserRol())
                            .flatMap(exists -> {
                                if (exists) {
                                    return Mono.error(new AssigmentRolUserException());
                                }

                                RolsUsersDomain domain = RolsUsersDomain.createNew(rol.getId(), cmd.userId());

                                return rolUserPort.assigmentRolByUser(domain)
                                        .thenReturn(new AssigmentUserRolCommandResult("Role assigment succesfull!"));
                            });
                });
    }
}
