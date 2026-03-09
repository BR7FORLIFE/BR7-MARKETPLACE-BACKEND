package com.example.webflux.application.AssetsMedia.usecases;

import com.example.webflux.application.AssetsMedia.command.UploadMediaCommand;
import com.example.webflux.application.AssetsMedia.command.UploadMediaCommandResult;

import reactor.core.publisher.Mono;

public interface AssetsMediaUseCase {
    Mono<UploadMediaCommandResult> uploadMedia(UploadMediaCommand cmd);
}
