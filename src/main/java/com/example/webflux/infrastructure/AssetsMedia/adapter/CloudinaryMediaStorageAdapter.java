package com.example.webflux.infrastructure.AssetsMedia.adapter;

import java.io.File;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.cloudinary.Cloudinary;
import com.example.webflux.domain.AssetsMedia.ports.MediaStoragePort;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Component
public class CloudinaryMediaStorageAdapter implements MediaStoragePort {

    private final Cloudinary cloudinary;

    public CloudinaryMediaStorageAdapter(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @SuppressWarnings("unchecked")
    public Mono<Map<String, Object>> upload(File file, Map<String, Object> options) {
        return Mono.fromCallable(() -> (Map<String, Object>) cloudinary.uploader().upload(file, options))
                .subscribeOn(Schedulers.boundedElastic());
    }
}
