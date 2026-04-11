package com.example.webflux.application.listings.orchestator;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.webflux.application.Authorization.exceptions.AccessDeniedException;
import com.example.webflux.application.Authorization.ports.RolUserRepositoryPort;
import com.example.webflux.application.auth.exceptions.UserNotFoundException;
import com.example.webflux.application.listings.command.ChangeStatusListingCommand;
import com.example.webflux.application.listings.command.ChangeStatusListingCommandResult;
import com.example.webflux.application.listings.command.CreateListingCommand;
import com.example.webflux.application.listings.command.CreateListingCommandResult;
import com.example.webflux.application.listings.command.GetAllListingByCursorPaginationCommand;
import com.example.webflux.application.listings.command.GetAllListingByCursorPaginationCommandResult;
import com.example.webflux.application.listings.command.GetListingByIdCommand;
import com.example.webflux.application.listings.command.GetListingByIdCommandResult;
import com.example.webflux.application.listings.exceptions.InvalidCursorException;
import com.example.webflux.application.listings.exceptions.ListingNotFoundException;
import com.example.webflux.application.listings.usecases.ListingUseCase;
import com.example.webflux.application.products.commands.RegisterProductCommand;
import com.example.webflux.application.products.usecases.ProductUseCases;
import com.example.webflux.domain.Authorization.services.AuthorizationService;
import com.example.webflux.domain.listings.models.ListingCursor;
import com.example.webflux.domain.listings.models.ListingModelDomain;
import com.example.webflux.domain.listings.models.ListingStatusReview;
import com.example.webflux.domain.listings.ports.ListingDomainRepositoryPort;
import com.example.webflux.domain.listings.services.ListingService;
import com.example.webflux.infrastructure.config.Base64Config;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ListingUseCaseImp implements ListingUseCase {

        // Utils
        private final Base64Config base64Config;

        private final ProductUseCases productUseCase;
        private final ListingDomainRepositoryPort listingPort;
        private final RolUserRepositoryPort rolUserRepositoryPort;

        public ListingUseCaseImp(ProductUseCases productUseCases,
                        ListingDomainRepositoryPort listingPort, RolUserRepositoryPort port,
                        Base64Config base64Config) {
                this.productUseCase = productUseCases;
                this.listingPort = listingPort;
                this.rolUserRepositoryPort = port;
                this.base64Config = base64Config;
        }

        /*
         * Necesitamos ver los roles del usuario para poder
         * hacer la respectiva paginacion dependiendo si el usuario
         * es admin o usuario
         * 
         */
        @Override
        public Mono<GetAllListingByCursorPaginationCommandResult> getAllListings(
                        GetAllListingByCursorPaginationCommand cmd) {

                return Mono.defer(() -> {
                        ListingCursor cursor = null;

                        if (cmd.cursor() != null && !cmd.cursor().isBlank()) {
                                try {
                                        cursor = base64Config.decode(cmd.cursor());
                                } catch (Exception e) {
                                        return Mono.error(new InvalidCursorException());
                                }
                        }

                        Instant createdAt = cursor != null ? cursor.getCreateAt() : null;
                        UUID id = cursor != null ? cursor.getId() : null;

                        String review = cmd.review() != null ? cmd.review().name() : null;
                        String status = cmd.publicationStatus() != null ? cmd.publicationStatus().name() : null;

                        return rolUserRepositoryPort.obtainRolByUserId(cmd.userId())
                                        .switchIfEmpty(Mono.error(new UserNotFoundException()))
                                        .collectList()
                                        .flatMap(roles -> {

                                                boolean canViewAll = AuthorizationService.canViewAll(roles);

                                                Flux<ListingModelDomain> listings = canViewAll
                                                                ? listingPort.getAllListings(createdAt, id,
                                                                                cmd.limit() + 1, review, status)
                                                                : listingPort.getAllListings(createdAt, id,
                                                                                cmd.limit() + 1, null, null);

                                                return listings.collectList()
                                                                .map(list -> {

                                                                        boolean hasMore = list.size() > cmd.limit();

                                                                        List<ListingModelDomain> page = hasMore
                                                                                        ? list.subList(0, cmd.limit())
                                                                                        : list;

                                                                        String nextCursor = null;

                                                                        if (!page.isEmpty()) {
                                                                                ListingModelDomain last = page
                                                                                                .get(page.size() - 1);

                                                                                ListingCursor newCursor = new ListingCursor(
                                                                                                1,
                                                                                                last.getCreatedAt(),
                                                                                                last.getListingId());

                                                                                nextCursor = base64Config.encoderBase64(
                                                                                                newCursor);
                                                                        }

                                                                        return new GetAllListingByCursorPaginationCommandResult(
                                                                                        page,
                                                                                        nextCursor,
                                                                                        hasMore);
                                                                });
                                        });
                });
        }

        // Estado de publicacion inactive y revision en DRAFT a la hora de crear el
        // listing
        @Override
        public Mono<CreateListingCommandResult> createListing(CreateListingCommand cmd) {

                RegisterProductCommand cmdProduct = new RegisterProductCommand(cmd.userId(),
                                cmd.product().sku(), cmd.product().name(),
                                cmd.product().shortDescription(),
                                cmd.product().longDescription(), cmd.product().model());

                return productUseCase.registerProduct(cmdProduct)
                                .flatMap(product -> {

                                        ListingModelDomain listing = ListingModelDomain
                                                        .createDraft(
                                                                        product.productId(),
                                                                        cmd.price(),
                                                                        cmd.currency());

                                        return listingPort.save(listing);
                                })
                                .map(listing -> new CreateListingCommandResult(
                                                listing.getListingId(),
                                                listing.getReviewStatus().name(),
                                                "Listing create succesfull!"));

        }

        public Mono<GetListingByIdCommandResult> getListingById(GetListingByIdCommand cmd) {
                return rolUserRepositoryPort.obtainRolByUserId(cmd.userId())
                                .switchIfEmpty(Mono.error(new UserNotFoundException()))
                                .collectList()
                                .flatMap(role -> {
                                        Boolean canView = AuthorizationService.canViewAll(role);

                                        return listingPort.findByListingId(cmd.listingId())
                                                        .switchIfEmpty(Mono.error(new ListingNotFoundException()))
                                                        .map(listing -> {
                                                                ListingStatusReview status = listing.getReviewStatus();

                                                                if (status != ListingStatusReview.DRAFT && !canView) {
                                                                        throw new AccessDeniedException();
                                                                }

                                                                return new GetListingByIdCommandResult(listing, null);
                                                        });

                                });
        }

        @Override
        public Mono<ChangeStatusListingCommandResult> changeStatusListing(ChangeStatusListingCommand cmd) {
                return listingPort.findByListingId(cmd.listingId())
                                .switchIfEmpty(Mono.error(new ListingNotFoundException()))
                                .flatMap(listing -> {
                                        ListingModelDomain listingUpdate = ListingService.verifyChangeStatus(listing,
                                                        cmd.review(),
                                                        cmd.publicationStatus());

                                        return listingPort.save(listingUpdate)
                                                        .map(statusListing -> new ChangeStatusListingCommandResult(
                                                                        "Listing update status succesfull!",
                                                                        Instant.now()));
                                });
        }
}
