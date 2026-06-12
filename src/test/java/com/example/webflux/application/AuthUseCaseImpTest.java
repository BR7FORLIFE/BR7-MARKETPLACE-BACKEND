package com.example.webflux.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.webflux.application.Authorization.usecases.AuthorizationRolUseCase;
import com.example.webflux.application.auth.command.LoginUserCommand;
import com.example.webflux.application.auth.command.LoginUserCommandResult;
import com.example.webflux.application.auth.command.RegisterUserCommand;
import com.example.webflux.application.auth.command.RegisterUserCommandResult;
import com.example.webflux.application.auth.model.AuthenticatedUser;
import com.example.webflux.application.auth.orchestator.AuthUseCaseImp;
import com.example.webflux.application.auth.ports.UserDomainRepositoryPort;
import com.example.webflux.application.auth.ports.UserJwtPort;
import com.example.webflux.application.auth.ports.UserSecurityPort;
import com.example.webflux.application.emailVerificationToken.commands.SendEmailCommandResult;
import com.example.webflux.application.emailVerificationToken.usecases.EmailVerifiedTokenUseCase;
import com.example.webflux.application.refreshToken.usecases.RefreshTokenUseCase;
import com.example.webflux.domain.auth.models.UserAuthStatus;
import com.example.webflux.domain.auth.models.UserModelDomain;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

//test unitario para la autenticacion
@ExtendWith(MockitoExtension.class)
public class AuthUseCaseImpTest {

        @Mock
        private UserDomainRepositoryPort userDomainRepositoryPort;

        @Mock
        private UserSecurityPort userSecurityPort;

        @Mock
        private UserJwtPort jwtPort;

        @Mock
        private PasswordEncoder passwordEncoder;

        @Mock
        private EmailVerifiedTokenUseCase emailUseCase;

        @Mock
        private AuthorizationRolUseCase authorizationRolUseCase;

        @Mock
        private RefreshTokenUseCase refreshTokenUseCase;

        @InjectMocks
        private AuthUseCaseImp authUseCaseImp;

        /*
         * 3 PASOS DE UN BUEN TEST
         * 
         * ARRANGE -> PREPARACION DE LOS DATOS Y ESTADOS INICIALES
         * ACT -> EJECUCION DE LO QUE QUIERES PROBAR
         * ASSERT + VEIFY -> RESULTADO FINAL O COMPORTAMIENTO FINAL
         * 
         */
        @DisplayName("Test para testear el registro de un usuario a la plataforma")
        @Test
        void registerUserTest() {
                // creamos el punto de entrada de la funcion register
                RegisterUserCommand registerUser = new RegisterUserCommand("juan", "123456", "juan@gmail.com");

                UUID userId = UUID.randomUUID();

                UserModelDomain user = UserModelDomain.createNew(
                                userId,
                                "juan",
                                UserAuthStatus.PENDING,
                                "juan@gmail.com",
                                "hash"); // simulamos el resultado final

                // aca definimos una accion donde nosotros suponemos que el usuario aun no esta
                // registrado
                when(userDomainRepositoryPort.findByEmail(registerUser.email()))
                                .thenReturn(Mono.empty());

                // aca definimos el encode de la contraseña y simulamos que se ha haseado
                when(passwordEncoder.encode(registerUser.password()))
                                .thenReturn("hash");

                // cuando guardemos el "usuario" retornamos el user creado anteriormente
                when(userDomainRepositoryPort.save(any()))
                                .thenReturn(Mono.just(user));

                // llamammos al caso de uso para asignar el rol, pero no lo necesitamos pues
                // dejamos el flujo
                when(authorizationRolUseCase.assigmentRolUser(any()))
                                .thenReturn(Mono.empty());

                // simulamos igual el flujo de envio de email
                when(emailUseCase.sendEmail(any()))
                                .thenReturn(Mono.just(new SendEmailCommandResult("email send")));

                // creamos el actuados que permitira ejecutar el register entero
                Mono<RegisterUserCommandResult> result = authUseCaseImp.executeRegister(registerUser);

                // creamos las aserciones o resultados esperados
                StepVerifier.create(result)
                                .expectNextMatches(res -> res.user_id().equals(userId) &&
                                                res.username().equals(user.getUsername()) &&
                                                res.message().equals("email send"))
                                .verifyComplete();

                // verificamos las interaciones, si se llamaron repositorios, con que parametros
                // cuantas veces ETC..
                verify(userDomainRepositoryPort).findByEmail(registerUser.email());
                verify(passwordEncoder).encode(registerUser.password());
                verify(userDomainRepositoryPort).save(any());
                verify(authorizationRolUseCase).assigmentRolUser(any());
                verify(emailUseCase).sendEmail(any());
        }

        @DisplayName("should login succesfull and return an accesstoken and refreshToken")
        @Test
        void shouldLoginSuccesfully() {
                // ARRANGE
                LoginUserCommand cmd = new LoginUserCommand("test@test.com", "12345");

                AuthenticatedUser user = new AuthenticatedUser(
                                UUID.fromString("f6aee105-f130-475a-a4db-7b8380dcb735"),
                                "bryan",
                                "test@test.com",
                                "hashed-password",
                                "ACTIVE",
                                Set.of("USER"));

                when(userSecurityPort.findByEmail(cmd.email())).thenReturn(Mono.just(user));

                when(passwordEncoder.matches(cmd.password(), user.password())).thenReturn(true);

                when(jwtPort.generateAccessToken(user)).thenReturn(Mono.just("access-token"));

                when(refreshTokenUseCase.createRefreshToken(user.userId())).thenReturn(Mono.just("refresh-token"));

                // ACT (Ejecutamos logica real con los mock y ARRENGE inicializados en este
                // test)
                Mono<LoginUserCommandResult> result = authUseCaseImp.executeLogin(cmd);

                // ASSERT + VERIFY
                StepVerifier.create(result)
                                .assertNext(res -> {
                                        assertAll(
                                                        () -> assertEquals("access-token", res.accessToken()),
                                                        () -> assertEquals("refresh-token", res.refreshRaw()));

                                }).verifyComplete();

                verify(userSecurityPort).findByEmail("test@test.com");
                verify(passwordEncoder).matches("12345", "hashed-password");
        }

        @DisplayName("should throw when the user not found")
        @Test
        void shouldThrowUserNotFoundException(){
                

        }
}
