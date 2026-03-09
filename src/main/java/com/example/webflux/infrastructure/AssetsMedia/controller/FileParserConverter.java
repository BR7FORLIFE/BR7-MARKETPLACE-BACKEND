package com.example.webflux.infrastructure.AssetsMedia.controller;

import java.io.File;
import java.io.IOException;

import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;

@Component
public class FileParserConverter {

    public Mono<File> toTempFile(FilePart filePart) {
        try {
            File tempFile = File.createTempFile("upload-", filePart.filename());
            return filePart.transferTo(tempFile).thenReturn(tempFile);

        } catch (IOException e) {
            return Mono.error(e);
        }
    }
}
