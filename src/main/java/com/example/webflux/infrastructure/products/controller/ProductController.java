package com.example.webflux.infrastructure.products.controller;

import java.time.Instant;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.webflux.application.products.commands.GetAllProductsCommand;
import com.example.webflux.application.products.commands.GetProductByIdCommand;
import com.example.webflux.application.products.dto.response.GetAllProductsResponseDto;
import com.example.webflux.application.products.dto.response.GetProductByIdResponseDto;
import com.example.webflux.application.products.usecases.ProductUseCases;
import com.example.webflux.infrastructure.security.CustomUserDetails;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/products")
public class ProductController {

        private final ProductUseCases productUseCases;

        public ProductController(ProductUseCases productUseCases) {
                this.productUseCases = productUseCases;
        }

        // metodo para obtener todos los productos del negocio (ADMINISTRADORES) y
        // ademas si es un usuario
        // obtener sus respectivos productos
        @GetMapping
        public Mono<ResponseEntity<GetAllProductsResponseDto>> getAllProducts(
                        @RequestParam(required = false, defaultValue = "10") int limit,
                        @RequestParam(required = false, defaultValue = "0") int offset,
                        @RequestParam(required = false, defaultValue = "DESC") String orden,
                        @RequestParam(required = false) Instant updateAt,
                        Authentication authentication) {
                CustomUserDetails details = (CustomUserDetails) authentication.getPrincipal();
                UUID userId = details.getUserId();

                GetAllProductsCommand cmd = new GetAllProductsCommand(userId, limit, offset, orden, updateAt);

                return productUseCases.getAllProducts(cmd)
                                .map(res -> ResponseEntity.ok().body(new GetAllProductsResponseDto(res.products(),
                                                res.hasMore(), res.limit(), res.offset())));
        }

        @GetMapping("/{id}")
        public Mono<ResponseEntity<GetProductByIdResponseDto>> getProductById(@PathVariable(name = "id") UUID productId,
                        Authentication authentication) {

                CustomUserDetails details = (CustomUserDetails) authentication.getPrincipal();
                UUID userId = details.getUserId();

                GetProductByIdCommand cmd = new GetProductByIdCommand(productId, userId);

                return productUseCases.getProductById(cmd)
                                .map(res -> ResponseEntity.ok().body(new GetProductByIdResponseDto(res.product())));
        }
}
