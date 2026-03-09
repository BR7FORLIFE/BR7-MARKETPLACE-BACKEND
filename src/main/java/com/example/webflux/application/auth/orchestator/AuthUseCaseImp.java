package com.example.webflux.application.auth.orchestator;

import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.webflux.application.Authorization.command.AssigmentUserRolCommand;
import com.example.webflux.application.Authorization.usecases.AuthorizationRolUseCase;
import com.example.webflux.application.auth.command.LoginUserCommand;
import com.example.webflux.application.auth.command.LoginUserCommandResult;
import com.example.webflux.application.auth.command.RegisterUserCommand;
import com.example.webflux.application.auth.command.RegisterUserCommandResult;
import com.example.webflux.application.auth.command.VerifiedUserCommandResult;
import com.example.webflux.application.auth.exceptions.AuthStatusEmailVerifiedException;
import com.example.webflux.application.auth.exceptions.IncorrectPasswordException;
import com.example.webflux.application.auth.exceptions.UserAlreadyRegisterException;
import com.example.webflux.application.auth.exceptions.UserNotFoundException;
import com.example.webflux.application.auth.exceptions.VerifiedUserException;
import com.example.webflux.application.auth.ports.UserJwtPort;
import com.example.webflux.application.auth.ports.UserSecurityPort;
import com.example.webflux.application.auth.usecases.AuthUseCase;
import com.example.webflux.application.emailVerificationToken.commands.SendEmailCommand;
import com.example.webflux.application.emailVerificationToken.usecases.EmailVerifiedTokenUseCase;
import com.example.webflux.application.refreshToken.usecases.RefreshTokenUseCase;
import com.example.webflux.domain.auth.models.UserAuthStatus;
import com.example.webflux.domain.auth.models.UserModelDomain;
import com.example.webflux.domain.auth.ports.UserDomainRepositoryPort;

import reactor.core.publisher.Mono;

@Service
public class AuthUseCaseImp implements AuthUseCase {

    private final UserDomainRepositoryPort userPort;
    private final UserSecurityPort userSecurityPort;
    private final UserJwtPort jwtPort;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenUseCase refreshTokenUseCase;
    private final EmailVerifiedTokenUseCase emailUseCase;
    private final AuthorizationRolUseCase authorizationRolUseCase;

    public AuthUseCaseImp(PasswordEncoder passwordEncoder, UserDomainRepositoryPort port,
            UserSecurityPort userSecurityPort, UserJwtPort userJwtPort, RefreshTokenUseCase refreshTokenUseCase,
            EmailVerifiedTokenUseCase emailVerifiedTokenUseCase, AuthorizationRolUseCase authorizationRolUseCase) {
        this.passwordEncoder = passwordEncoder;
        this.userPort = port;
        this.userSecurityPort = userSecurityPort;
        this.jwtPort = userJwtPort;
        this.refreshTokenUseCase = refreshTokenUseCase;
        this.emailUseCase = emailVerifiedTokenUseCase;
        this.authorizationRolUseCase = authorizationRolUseCase;
    }

    @Override
    public Mono<VerifiedUserCommandResult> verifyUser(UUID userId) {
        return userPort.findByUserId(userId)
                .map(user -> new VerifiedUserCommandResult(user))
                .switchIfEmpty(Mono.error(new VerifiedUserException()));
    }

    @Override
    public Mono<RegisterUserCommandResult> executeRegister(RegisterUserCommand cmd) {

        return userPort.findByEmail(cmd.email())
                .hasElement()
                .flatMap(exists -> {
                    if (exists) {
                        return Mono
                                .<RegisterUserCommandResult>error(new UserAlreadyRegisterException());
                    }

                    String passwordHash = passwordEncoder.encode(cmd.password());
                    UserModelDomain userModel = UserModelDomain.register(cmd.username(), cmd.email(), passwordHash);

                    return userPort.save(userModel)
                            .flatMap(saved -> {

                                AssigmentUserRolCommand assignmentCommand = new AssigmentUserRolCommand(saved.getId(),
                                        "USER");

                                SendEmailCommand emailCommand = new SendEmailCommand(saved.getId(), saved.getEmail());

                                return authorizationRolUseCase.assigmentRolUser(assignmentCommand)
                                        .then(emailUseCase.sendEmail(emailCommand))
                                        .map(emailMessage -> new RegisterUserCommandResult(
                                                saved.getId(),
                                                saved.getUsername(),
                                                emailMessage.message()));
                            });

                });
    }

    @Override
    public Mono<LoginUserCommandResult> executeLogin(LoginUserCommand cmd) {
        return userSecurityPort.findByEmail(cmd.email())
                .switchIfEmpty(Mono.error(new UserNotFoundException()))
                .flatMap(user -> {
                    if (!passwordEncoder.matches(cmd.password(), user.password())) {
                        return Mono.error(new IncorrectPasswordException());
                    }

                    if (UserAuthStatus.valueOf(user.authStatus()) != UserAuthStatus.ACTIVE) {
                        return Mono.error(new AuthStatusEmailVerifiedException());
                    }

                    return jwtPort.generateAccessToken(user)
                            .flatMap(accessToken -> refreshTokenUseCase.createRefreshToken(user.userId())
                                    .map(refreshRaw -> new LoginUserCommandResult(accessToken, refreshRaw)));
                });
    }

}
