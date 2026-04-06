package com.example.webflux.infrastructure.config;

import java.util.Base64;

import org.springframework.stereotype.Component;

import com.example.webflux.application.listings.exceptions.InvalidCursorException;
import com.example.webflux.domain.listings.models.ListingCursor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class Base64Config {

    private final ObjectMapper mapper;

    public Base64Config(ObjectMapper objectMapper) {
        this.mapper = objectMapper;
    }

    public String encoderBase64(ListingCursor listingCursor) {
        try {
            String json = this.mapper.writeValueAsString(listingCursor);

            return Base64.getUrlEncoder().encodeToString(json.getBytes());
        } catch (JsonProcessingException e) {
            throw new InvalidCursorException();
        }
    };

    public ListingCursor decode(String rawBase) {
        try {
            String json = new String(Base64.getUrlDecoder().decode(rawBase));
            ObjectMapper mapper = new ObjectMapper();

            return mapper.readValue(json, ListingCursor.class);
        } catch (Exception e) {
            throw new InvalidCursorException();
        }
    }
}
