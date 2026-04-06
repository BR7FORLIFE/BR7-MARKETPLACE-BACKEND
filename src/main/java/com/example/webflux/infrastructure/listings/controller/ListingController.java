package com.example.webflux.infrastructure.listings.controller;

import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.example.webflux.application.AssetsMedia.dto.response.UploadMediaResponseDto;
import com.example.webflux.application.AssetsMedia.orchestator.AssetsMediaUseCaseImp;
import com.example.webflux.application.listings.command.ChangeStatusListingCommand;
import com.example.webflux.application.listings.command.GetAllListingByCursorPaginationCommand;
import com.example.webflux.application.listings.command.GetListingByIdCommand;
import com.example.webflux.application.listings.dto.request.ChangeStatusRequestDto;
import com.example.webflux.application.listings.dto.response.ChangeStatusResponseDto;
import com.example.webflux.application.listings.dto.response.GetAllListingResponseDto;
import com.example.webflux.application.listings.dto.response.GetListingByIdResponseDto;
import com.example.webflux.application.listings.usecases.ListingUseCase;
import com.example.webflux.domain.listings.models.ListingPublicationStatus;
import com.example.webflux.domain.listings.models.ListingStatusReview;
import com.example.webflux.infrastructure.AssetsMedia.controller.FileParserConverter;
import com.example.webflux.infrastructure.security.CustomUserDetails;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/listing")
public class ListingController {

        private final ListingUseCase listingUseCase;
        private final FileParserConverter fileParserConverter;
        private final AssetsMediaUseCaseImp assetsMediaUseCaseImp;

        public ListingController(ListingUseCase listingUseCase, FileParserConverter converter,
                        AssetsMediaUseCaseImp assetsMediaUseCaseImp) {
                this.listingUseCase = listingUseCase;
                this.fileParserConverter = converter;
                this.assetsMediaUseCaseImp = assetsMediaUseCaseImp;
        }

        // obtener todos los listings paginados y ordernados!
        @GetMapping
        public Mono<ResponseEntity<GetAllListingResponseDto>> getAllListings(
                        @RequestParam(required = false) String cursor,
                        @RequestParam(defaultValue = "10") int limit,
                        @RequestParam(required = false) ListingStatusReview review,
                        @RequestParam(required = false) ListingPublicationStatus publicationStatus,
                        Authentication authentication) {

                CustomUserDetails details = (CustomUserDetails) authentication.getPrincipal();

                UUID userId = details.getUserId();

                GetAllListingByCursorPaginationCommand cmd = new GetAllListingByCursorPaginationCommand(cursor, limit,
                                review,
                                publicationStatus, userId);

                return listingUseCase.getAllListings(cmd)
                                .map(res -> ResponseEntity.ok()
                                                .body(new GetAllListingResponseDto(res.listings(), res.cursor(),
                                                                res.hasMore())));
        }

        // obtener un listing por id
        @GetMapping("/{id}")
        public Mono<ResponseEntity<GetListingByIdResponseDto>> getListingById(Authentication authentication,
                        @PathVariable(name = "id") UUID listingId) {
                CustomUserDetails details = (CustomUserDetails) authentication.getPrincipal();
                UUID userId = details.getUserId();

                GetListingByIdCommand cmd = new GetListingByIdCommand(userId, listingId);

                return listingUseCase.getListingById(cmd)
                                .map(result -> ResponseEntity.ok()
                                                .body(new GetListingByIdResponseDto(result.listing(), result.error())));
        }

        @PatchMapping("/{id}/status")
        public Mono<ResponseEntity<ChangeStatusResponseDto>> changeStatusListing(
                        @PathVariable(name = "id") UUID listingId, @RequestBody ChangeStatusRequestDto dto) {
                ChangeStatusListingCommand cmd = new ChangeStatusListingCommand(listingId, dto.review(),
                                dto.publicationStatus());
                return listingUseCase.changeStatusListing(cmd)
                                .map(res -> ResponseEntity.ok()
                                                .body(new ChangeStatusResponseDto(res.message(), res.updateAt())));
        }

        @PostMapping(value = "/{listingId}/media", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        public Mono<ResponseEntity<UploadMediaResponseDto>> uploadAssetsMedia(
                        @RequestPart("file") FilePart part, Authentication authentication,
                        @PathVariable UUID listingId) {

                CustomUserDetails details = (CustomUserDetails) authentication.getPrincipal();
                UUID userId = details.getUserId();

                return null;
        }
}
