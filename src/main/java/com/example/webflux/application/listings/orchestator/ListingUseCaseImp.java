package com.example.webflux.application.listings.orchestator;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.webflux.application.Authorization.ports.RolUserRepositoryPort;
import com.example.webflux.application.auth.exceptions.UserNotFoundException;
import com.example.webflux.application.listings.command.ApproveListingCommand;
import com.example.webflux.application.listings.command.ApproveListingCommandResult;
import com.example.webflux.application.listings.command.CreateListingCommand;
import com.example.webflux.application.listings.command.CreateListingCommandResult;
import com.example.webflux.application.listings.command.GetAllListingByCursorPaginationCommand;
import com.example.webflux.application.listings.command.GetAllListingByCursorPaginationCommandResult;
import com.example.webflux.application.listings.command.InReviewStatusListingCommand;
import com.example.webflux.application.listings.command.InReviewStatusListingCommandResult;
import com.example.webflux.application.listings.command.PublishListingCommand;
import com.example.webflux.application.listings.command.PublishListingCommandResult;
import com.example.webflux.application.listings.command.RejectedListingCommand;
import com.example.webflux.application.listings.command.RejectedListingCommandResult;
import com.example.webflux.application.listings.command.SuspendListingCommand;
import com.example.webflux.application.listings.command.SuspendListingCommandResult;
import com.example.webflux.application.listings.exceptions.InvalidCursorException;
import com.example.webflux.application.listings.exceptions.ListingNotFoundException;
import com.example.webflux.application.listings.usecases.ListingUseCase;
import com.example.webflux.application.products.commands.RegisterProductCommand;
import com.example.webflux.application.products.usecases.ProductUseCases;
import com.example.webflux.domain.listings.models.ListingCursor;
import com.example.webflux.domain.listings.models.ListingModelDomain;
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

                                                boolean canViewAll = ListingService.canViewAll(roles);

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

        // Estado de publicacion Inactiva andemas del estado de revision pasa a InReview
        @Override
        public Mono<InReviewStatusListingCommandResult> inReviewListing(InReviewStatusListingCommand cmd) {
                return listingPort.findByListingId(cmd.listingId())
                                .switchIfEmpty(Mono.error(new ListingNotFoundException()))
                                .map(ListingModelDomain::submitForReview)
                                .flatMap(listingPort::save)
                                .map(inReviewListing -> new InReviewStatusListingCommandResult(
                                                inReviewListing.getListingId(),
                                                String.valueOf(inReviewListing.getReviewStatus()),
                                                String.valueOf(inReviewListing.getPublicationStatus())));
        }

        // Estado de publicacion esperando a ser activa ademas del estado de revision en
        // PUBLISHED
        @Override
        public Mono<ApproveListingCommandResult> approveListing(ApproveListingCommand cmd) {
                return listingPort.findByListingId(cmd.listingId())
                                .switchIfEmpty(Mono.error(new ListingNotFoundException()))
                                .map(ListingModelDomain::approveReview)
                                .flatMap(listingPort::save)
                                .map(newListing -> new ApproveListingCommandResult(
                                                newListing.getListingId(),
                                                String.valueOf(newListing.getReviewStatus()),
                                                String.valueOf(newListing.getPublicationStatus())));
        }

        /*
         * Listing totalmente rechazado sin posibilidad de cambiar de estado
         * de revision y ademas notificar al usuario el motivo del rechazo
         * 
         */
        @Override
        public Mono<RejectedListingCommandResult> rejectedListing(RejectedListingCommand cmd) {
                return listingPort.findByListingId(cmd.listingId())
                                .switchIfEmpty(Mono.error(new ListingNotFoundException()))
                                .map(ListingModelDomain::rejectReview)
                                .flatMap(listingPort::save)
                                .map(newListing -> new RejectedListingCommandResult());
        }

        /*
         * metodos de publicacion de listings (suspender) estado SUSPEND ->
         * ListingPublicationStatus ademas de notificar al usuario porque
         * su publicacion ha sido suspendida
         */
        @Override
        public Mono<SuspendListingCommandResult> suspendListing(SuspendListingCommand cmd) {
                return listingPort.findByListingId(cmd.listingId())
                                .switchIfEmpty(Mono.error(new ListingNotFoundException()))
                                .map(ListingModelDomain::suspend)
                                .flatMap(listingPort::save)
                                .map(newListing -> new SuspendListingCommandResult());
        }

        // estado Published -> ListingPublicationStatus

        /*
         * En este apartado es necesario notificar al usuario
         * ya sea por medio de un correo electronico que su publicacion
         * ha sido publicada
         */
        @Override
        public Mono<PublishListingCommandResult> publishListing(PublishListingCommand cmd) {
                return listingPort.findByListingId(cmd.listingId())
                                .switchIfEmpty(Mono.error(new ListingNotFoundException()))
                                .map(ListingModelDomain::publish)
                                .flatMap(listingPort::save)
                                .map(newListing -> new PublishListingCommandResult());
        }
}
