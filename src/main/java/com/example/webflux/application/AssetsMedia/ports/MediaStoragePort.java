package com.example.webflux.application.AssetsMedia.ports;

import java.io.File;
import java.util.Map;

import reactor.core.publisher.Mono;

public interface MediaStoragePort {
    Mono<Map<String, Object>> upload(File file, Map<String, Object> options);
}
