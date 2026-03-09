package com.example.webflux.application.emailVerificationToken.orchestator;

import java.time.Duration;
import java.time.Instant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.webflux.application.auth.exceptions.UserNotFoundException;
import com.example.webflux.application.emailVerificationToken.commands.SendEmailCommand;
import com.example.webflux.application.emailVerificationToken.commands.SendEmailCommandResult;
import com.example.webflux.application.emailVerificationToken.commands.VerifyEmailCommand;
import com.example.webflux.application.emailVerificationToken.commands.VerifyEmailCommandResult;
import com.example.webflux.application.emailVerificationToken.exceptions.InvalidTokenException;
import com.example.webflux.application.emailVerificationToken.model.SendEmailParams;
import com.example.webflux.application.emailVerificationToken.ports.SendEmailPort;
import com.example.webflux.application.emailVerificationToken.usecases.EmailVerifiedTokenUseCase;
import com.example.webflux.domain.auth.models.UserModelDomain;
import com.example.webflux.domain.auth.ports.UserDomainRepositoryPort;
import com.example.webflux.domain.emailVerificationToken.models.EmailVerificationTokenModel;
import com.example.webflux.domain.emailVerificationToken.ports.EmailVerificationTokenPort;
import com.example.webflux.infrastructure.config.HashService;

import reactor.core.publisher.Mono;
import reactor.util.function.Tuples;

@Service
public class EmailVerifyTokenUseCaseImp implements EmailVerifiedTokenUseCase {

        private final UserDomainRepositoryPort userPort; // capa de dominio
        private final EmailVerificationTokenPort emailPort; // capa de dominio
        private final HashService hashService; // config global

        // puertos hacia infraestructura
        private final SendEmailPort sendEmailPort;

        // constants
        @Value("${email-expiration.time}")
        private Duration emailExpirationTime; // tiempo de expiracion del token para verificar el email

        @Value("${app.email-url}")
        private String emailUrl; // frontend url para mostrar algo vistoso a la hora de verificar el token

        // parametros de configuracion para enviar los token a los email de los usuarios
        @Value("${spring.resend.issuer}")
        private String issuerEmail;

        public EmailVerifyTokenUseCaseImp(UserDomainRepositoryPort userPort, EmailVerificationTokenPort emailPort,
                        SendEmailPort sendEmailPort, HashService hashService) {
                this.userPort = userPort;
                this.emailPort = emailPort;
                this.hashService = hashService;
                this.sendEmailPort = sendEmailPort;
        }

        // Flujo para enviar el email del usuario con el token a verificar

        /**
         * 1. Generar el raw token
         * 2. Hashearlo
         * 3. Crear el modelo EmailVerificationTokenModel
         * 4. Guardarlo en la DB
         * 5. Contruir el link
         * 6. Enviar el email
         * 7. retornar resultado
         */
        @Override
        public Mono<SendEmailCommandResult> sendEmail(SendEmailCommand cmd) {
                // genramos el raw token con 32 bytes de entropia o 256 bits
                String rawToken = hashService.randomBase64(32); // <-- generamos el raw token con alta entropia

                // hasheamos el rawToken para guardarlo en la base de datos
                String hashToken = hashService.sha256(rawToken);

                // definimos el tiempo de creacion y expiracion del link para verificar el email
                Instant createAt = Instant.now();
                Instant expirationTime = Instant.now().plus(this.emailExpirationTime);

                // Creamos el modelo
                EmailVerificationTokenModel emailModel = EmailVerificationTokenModel.createNew(
                                cmd.userId(),
                                hashToken,
                                expirationTime,
                                null,
                                createAt);

                // creamos el link de verification de email
                String verificationLink = this.emailUrl + "?token=" + rawToken;

                // creamos el contenido o cuerpo del correo que se enviará al cliente
                String contentEmailHtml = """
                                <p>Click the link below to verify your email:</p>
                                <a href="%s">Verify your account</a>
                                """.formatted(verificationLink);

                // parametros o modelo que se usara para enviar el correo al usuario
                SendEmailParams params = new SendEmailParams(this.issuerEmail, cmd.email(),
                                "Confirm your email | BR7-MARKETPLACE", contentEmailHtml);

                // guardamos en la base de datos
                return emailPort.save(emailModel)
                                .then(sendEmailPort.sendEmail(params))
                                .map(result -> new SendEmailCommandResult(result));
        }

        /**
         * Flujo cuando el usuario le llega el correo con el link de verificacion
         */
        @Override
        public Mono<VerifyEmailCommandResult> verifyEmail(VerifyEmailCommand cmd) {

                // haseamos la raw token que nos llega para verificar
                String tokenHash = hashService.sha256(cmd.rawToken());

                // buscamos una el registro con el tokenHash
                return emailPort.findByTokenHash(tokenHash)
                                // sino encontramos ningun registro lanzamos un excepcion
                                .switchIfEmpty(Mono.error(new InvalidTokenException()))

                                /**
                                 * cuando obtengamos el modelo del token buscamos a su usario asociado
                                 * sino existe lanzamos una exception caso contrario
                                 * retornamos una tupla con el modelo del usuario y el token asociado a este
                                 */
                                .flatMap(token -> userPort.findByUserId(token.getUserId())
                                                .switchIfEmpty(Mono.error(new UserNotFoundException()))
                                                .map(user -> Tuples.of(token, user)))

                                .flatMap(tuple -> {
                                        // accedemos al primer elemento de la tupla que es EmailVerificationTokenModel
                                        EmailVerificationTokenModel token = tuple.getT1();

                                        // accedemos al segundo elemento de la tupla que es UserModelDomain
                                        UserModelDomain user = tuple.getT2();

                                        // creamos el instante actual para validar reglas de negocio
                                        Instant now = Instant.now();

                                        // logica de Dominio

                                        // marcamos el modelo de verificacion como consumido (token)
                                        EmailVerificationTokenModel consumedUser = token.consume(now);

                                        // marcamos como usuario activado a lo cual lo atribuimos email verificado
                                        UserModelDomain activateUser = user.activateUser();

                                        // guardamos el nuevo estado de validacion de email y actualizamos el estado del
                                        // usuario
                                        return emailPort.save(consumedUser)
                                                        .then(userPort.save(activateUser))
                                                        // retornamos el mensaje
                                                        .thenReturn(new VerifyEmailCommandResult(
                                                                        "User activate succesfull!"));
                                });
        }
}
