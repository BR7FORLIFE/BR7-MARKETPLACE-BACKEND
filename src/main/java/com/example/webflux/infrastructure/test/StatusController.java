package com.example.webflux.infrastructure.test;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/status")
public class StatusController {
    @GetMapping
    public Mono<ResponseEntity<Map<String, String>>> status() {
        return Mono.just(ResponseEntity.ok().body(Map.of("status", "active")));
    }
}
