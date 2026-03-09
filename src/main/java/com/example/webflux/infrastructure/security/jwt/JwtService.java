package com.example.webflux.infrastructure.security.jwt;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class JwtService {

    private final RSAPrivateKey privateKey; // <- firmar JWT
    private final RSAPublicKey publicKey; // <- validar JWT

    private String issuer = "BR7FORLIFE.com"; // el dueño o el dominio que emite el token
    private Integer accessTokenSeconds = 60 * 60 * 60;

    public JwtService(KeyPair keyPair) {
        this.publicKey = (RSAPublicKey) keyPair.getPublic();
        this.privateKey = (RSAPrivateKey) keyPair.getPrivate();
    }

    // Genera el Jwt Con nimbus jose ademas se generan aleatoriamente las keys
    public Mono<String> generateAccessToken(UserDetails userDetails) {
        return Mono.fromCallable(() -> {
            Instant timestampCurrent = Instant.now();
            Instant timestampFuture = timestampCurrent.plusSeconds(this.accessTokenSeconds);

            Date currentDate = Date.from(timestampCurrent);
            Date expirationDate = Date.from(timestampFuture);

            /**
             * Caracteristicas de Nimbus JOSE
             * 
             * - Crea el JWT por sus 3 piezas fundamentales (Header, Payload, Signature)
             */

            // Payload del token -> Nimbus JOSE
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .issuer(this.issuer)
                    .subject(userDetails.getUsername())
                    .issueTime(currentDate)
                    .expirationTime(expirationDate)
                    .claim("ROLS", userDetails.getAuthorities()
                            .stream()
                            .map(GrantedAuthority::getAuthority)
                            .toList())
                    .jwtID(UUID.randomUUID().toString())
                    .build();

            // Header del token -> Nimbus JOSE
            JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.RS256)
                    .type(JOSEObjectType.JWT)
                    .build();

            // Unimos las partes header - payload !Sin firmar
            SignedJWT signedJWT = new SignedJWT(header, claimsSet);

            // Firmamos segun el metodo de encryptacion definido (RSA256)
            RSASSASigner signer = new RSASSASigner(this.privateKey);
            signedJWT.sign(signer);

            // Devolvemos el Jwt serializado en BASE64
            return signedJWT.serialize();
        }).subscribeOn(Schedulers.boundedElastic());
    }

    /**
     * Etapas de validaciones de JWT
     * 
     * 1. Validacion cryptografica (clave publica y clave privada)
     * 2. Tiempos (tiempo de expiracion emitido en el token)
     * 3. JWT ID (para hacer revocaciones)
     */

    public Mono<JWTClaimsSet> validateAccessToken(String token) {
        return Mono.fromCallable(() -> {
            // parseamos el token en sus 3 partes (header, payload, signature)
            SignedJWT signedJWT = SignedJWT.parse(token);

            /**
             * Validacion Cryptografica
             */

            // objeto encargado de verificar la firma
            JWSVerifier jwsVerifier = new RSASSAVerifier(this.publicKey);

            // validamos si conserva su integridad
            if (!signedJWT.verify(jwsVerifier)) {
                throw new JOSEException("invalid signature");
            }

            // obtenemos las claims
            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();

            // Validacion por tiempos
            Date date = new Date();

            if (claims.getExpirationTime() == null || date.after(claims.getExpirationTime())) {
                throw new Exception("Token expired!");
            }

            return claims;
        }).subscribeOn(Schedulers.boundedElastic());
    }

    // retornaar el Public Key
    public RSAPublicKey getPublicKey() {
        return this.publicKey;
    }
}
