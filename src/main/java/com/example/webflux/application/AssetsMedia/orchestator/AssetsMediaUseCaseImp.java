package com.example.webflux.application.AssetsMedia.orchestator;

import org.springframework.stereotype.Service;

import com.example.webflux.application.AssetsMedia.command.UploadMediaCommand;
import com.example.webflux.application.AssetsMedia.command.UploadMediaCommandResult;
import com.example.webflux.application.AssetsMedia.usecases.AssetsMediaUseCase;
import com.example.webflux.domain.AssetsMedia.ports.MediaStoragePort;
import com.example.webflux.domain.listings.ports.ListingDomainRepositoryPort;
import reactor.core.publisher.Mono;

@Service
public class AssetsMediaUseCaseImp implements AssetsMediaUseCase {

    private final ListingDomainRepositoryPort listingPort;
    private final MediaStoragePort mediaStoragePort;

    public AssetsMediaUseCaseImp(ListingDomainRepositoryPort port, MediaStoragePort mediaStoragePort) {
        this.listingPort = port;
        this.mediaStoragePort = mediaStoragePort;
    }

    @Override
    public Mono<UploadMediaCommandResult> uploadMedia(UploadMediaCommand cmd) {
        return null;
    }
}
