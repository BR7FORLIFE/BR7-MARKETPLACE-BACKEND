package com.example.webflux.infrastructure.config;

import java.security.KeyPair;
import java.security.KeyPairGenerator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeyConfig {

    /**
     * 
     * @return
     * @throws Exception
     * 
     *                   Esta configuracion no funciona en entornos de produccion ya
     *                   que cada vez
     *                   que se recompila el proyecto las claves privadas y publicas
     *                   cambian
     */
    @Bean
    public KeyPair keyPair() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        return generator.generateKeyPair();
    }
}
