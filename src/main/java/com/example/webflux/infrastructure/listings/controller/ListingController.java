package com.example.webflux.infrastructure.listings.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.example.webflux.application.AssetsMedia.dto.response.UploadMediaResponseDto;
import com.example.webflux.application.AssetsMedia.orchestator.AssetsMediaUseCaseImp;
import com.example.webflux.application.listings.command.ApproveListingCommand;
import com.example.webflux.application.listings.command.CreateListingCommand;
import com.example.webflux.application.listings.command.GetAllListingByCursorPaginationCommand;
import com.example.webflux.application.listings.command.InReviewStatusListingCommand;
import com.example.webflux.application.listings.draft.ProductDraft;
import com.example.webflux.application.listings.dto.request.ApproveListingRequestDto;
import com.example.webflux.application.listings.dto.request.CreateListingRequestDto;
import com.example.webflux.application.listings.dto.request.InReviewListingRequestDto;
import com.example.webflux.application.listings.dto.request.PublishListingRequestDto;
import com.example.webflux.application.listings.dto.request.RejectedListingRequestDto;
import com.example.webflux.application.listings.dto.request.SuspendListingRequestDto;
import com.example.webflux.application.listings.dto.response.ApproveListingResponseDto;
import com.example.webflux.application.listings.dto.response.CreateListingResponseDto;
import com.example.webflux.application.listings.dto.response.GetAllListingResponseDto;
import com.example.webflux.application.listings.dto.response.InReviewListingResponseDto;
import com.example.webflux.application.listings.dto.response.PublishListingResponseDto;
import com.example.webflux.application.listings.dto.response.RejectedListingResponseDto;
import com.example.webflux.application.listings.dto.response.SuspendListingResponseDto;
import com.example.webflux.application.listings.usecases.ListingUseCase;
import com.example.webflux.domain.listings.models.ListingPublicationStatus;
import com.example.webflux.domain.listings.models.ListingStatusReview;
import com.example.webflux.infrastructure.AssetsMedia.controller.FileParserConverter;
import com.example.webflux.infrastructure.security.CustomUserDetails;

import jakarta.validation.Valid;
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
        @GetMapping("/id")
        public Mono<ResponseEntity<?>> getListingById() {
                return null;
        }

        /**
         * 
         * @param dto            -> la informaicon que llega del cliente a tratar en el
         *                       servidor
         * @param authentication -> usuario acutla que quiere crear el listing
         * @return -> estado o resultado de la operativa en el servidor
         * 
         *         La idea es que al crear un listing o una peticion de publicacion de
         *         producto pues primero
         *         se cree el producto con el usuario interesado y despues crear el
         *         listing a publicar
         * 
         *         isActive (CreateListingDraft) -> define que la publicacion no esta
         *         activa o publicada sino que esta en estado de revision por los
         *         moderadores
         */
        @PostMapping
        public Mono<ResponseEntity<CreateListingResponseDto>> createListing(
                        @RequestBody @Valid CreateListingRequestDto dto, Authentication authentication) {
                CustomUserDetails details = (CustomUserDetails) authentication.getPrincipal();

                UUID userId = details.getUserId();

                ProductDraft product = new ProductDraft(dto.product().name(), dto.product().shortDescription(),
                                dto.product().longDescription(), dto.product().model(), dto.product().sku());

                CreateListingCommand cmd = new CreateListingCommand(userId, product, dto.price(), dto.currency());

                return listingUseCase.createListing(cmd)
                                .map(result -> ResponseEntity.status(HttpStatus.CREATED)
                                                .body(new CreateListingResponseDto(result.listingId(), result.status(),
                                                                result.message())));
        }

        @PostMapping("/inreview")
        public Mono<ResponseEntity<InReviewListingResponseDto>> inReviewListing(
                        @RequestBody InReviewListingRequestDto dto) {

                InReviewStatusListingCommand cmd = new InReviewStatusListingCommand(dto.listingId());
                return listingUseCase.inReviewListing(cmd)
                                .map(result -> ResponseEntity.status(HttpStatus.ACCEPTED)
                                                .body(new InReviewListingResponseDto(
                                                                result.listingId(), result.reviewStatus(),
                                                                result.publicationStatus())));
        }

        // @PreAuthorize("hasRole('ADMIN')") // <-- solo los moderadores o
        // administradores pueden aprobar un listing
        @PostMapping("/approve")
        public Mono<ResponseEntity<ApproveListingResponseDto>> approveListing(
                        @RequestBody ApproveListingRequestDto dto) {
                ApproveListingCommand cmd = new ApproveListingCommand(dto.listingId());

                return listingUseCase.approveListing(cmd)
                                .map(result -> ResponseEntity.status(HttpStatus.ACCEPTED)
                                                .body(new ApproveListingResponseDto(
                                                                result.listingId(), result.reviewStatus(),
                                                                result.publicationStatus())));
        }

        @PostMapping("/publish")
        public Mono<ResponseEntity<PublishListingResponseDto>> publishListing(
                        @RequestBody @Valid PublishListingRequestDto dto) {
                return null;
        }

        /**
         * Loes estados de publicar, rechazar y suspender tienen logica especial
         * ya que deben notficar al usuario porque su publicacion ha sido
         * rechazada o suspendida
         */
        @PostMapping("/rejected")
        public Mono<ResponseEntity<RejectedListingResponseDto>> rejectedListing(
                        @RequestBody @Valid RejectedListingRequestDto dto) {
                return null;
        }

        @PostMapping("/suspend")
        public Mono<ResponseEntity<SuspendListingResponseDto>> suspendListing(
                        @RequestBody @Valid SuspendListingRequestDto dto) {
                return null;
        }

        /**
         * metodo para cargar las distintas imagenes o recursos para el listing actual
         * 
         */
        @PostMapping(value = "/{listingId}/media", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        public Mono<ResponseEntity<UploadMediaResponseDto>> uploadAssetsMedia(
                        @RequestPart("file") FilePart part, Authentication authentication,
                        @PathVariable UUID listingId) {

                CustomUserDetails details = (CustomUserDetails) authentication.getPrincipal();
                UUID userId = details.getUserId();

                return null;
        }

}
