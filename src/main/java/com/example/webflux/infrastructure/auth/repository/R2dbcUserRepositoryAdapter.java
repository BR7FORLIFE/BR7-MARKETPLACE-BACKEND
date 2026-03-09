package com.example.webflux.infrastructure.auth.repository;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.example.webflux.domain.auth.models.UserModelDomain;
import com.example.webflux.domain.auth.ports.UserDomainRepositoryPort;
import com.example.webflux.infrastructure.auth.mapper.UserMapper;
import com.example.webflux.infrastructure.auth.persistence.UserModelEntity;
import com.example.webflux.infrastructure.auth.repository.postgres.R2dbcPostgresUserRepository;

import reactor.core.publisher.Mono;

@Repository
public class R2dbcUserRepositoryAdapter implements UserDomainRepositoryPort {

        private final R2dbcPostgresUserRepository userRepository;

        public R2dbcUserRepositoryAdapter(R2dbcPostgresUserRepository r2dbcPostgresUserRepository) {
                this.userRepository = r2dbcPostgresUserRepository;
        }

        @Override
        public Mono<UserModelDomain> findByUserId(UUID id) {
                return userRepository.findById(id)
                                .map(UserMapper::toDomain);
        }

        @Override
        public Mono<UserModelDomain> findByUsername(String username) {
                return userRepository.findByUsername(username)
                                .map(UserMapper::toDomain);
        }

        @Override
        public Mono<UserModelDomain> findByEmail(String email) {
                return userRepository.findByEmail(email)
                                .map(UserMapper::toDomain);
        }

        @Override
        public Mono<UserModelDomain> save(UserModelDomain userModelDomain) {
                return userRepository.existsById(userModelDomain.getId())
                                .flatMap(exists -> {
                                        UserModelEntity entity = UserMapper.toEntity(userModelDomain);

                                        if (exists) {
                                                entity.markNotNew();
                                        }

                                        return userRepository.save(entity)
                                                        .map(UserMapper::toDomain);
                                });
        }
}