package com.example.webflux.application.listings.orchestator;

import org.springframework.stereotype.Service;

import com.example.webflux.application.listings.command.ApproveListingCommand;
import com.example.webflux.application.listings.command.ApproveListingCommandResult;
import com.example.webflux.application.listings.command.CreateListingCommand;
import com.example.webflux.application.listings.command.CreateListingCommandResult;
import com.example.webflux.application.listings.command.PublishListingCommand;
import com.example.webflux.application.listings.command.PublishListingCommandResult;
import com.example.webflux.application.listings.command.RejectedListingCommand;
import com.example.webflux.application.listings.command.RejectedListingCommandResult;
import com.example.webflux.application.listings.command.SuspendListingCommand;
import com.example.webflux.application.listings.command.SuspendListingCommandResult;
import com.example.webflux.application.listings.exceptions.ApproveListingException;
import com.example.webflux.application.listings.exceptions.PublishListingException;
import com.example.webflux.application.listings.exceptions.SuspendListingException;
import com.example.webflux.application.listings.usecases.ListingUseCase;
import com.example.webflux.application.products.commands.RegisterProductCommand;
import com.example.webflux.application.products.usecases.ProductUseCases;
import com.example.webflux.domain.listings.models.ListingModelDomain;
import com.example.webflux.domain.listings.ports.ListingDomainRepositoryPort;

import reactor.core.publisher.Mono;

@Service
public class ListingUseCaseImp implements ListingUseCase {

        private final ProductUseCases productUseCase;
        private final ListingDomainRepositoryPort listingPort;

        public ListingUseCaseImp(ProductUseCases productUseCases,
                        ListingDomainRepositoryPort listingPort) {
                this.productUseCase = productUseCases;
                this.listingPort = listingPort;
        }

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

        @Override
        public Mono<ApproveListingCommandResult> approveListing(ApproveListingCommand cmd) {
                return listingPort.findByListingId(cmd.listingId())
                                .switchIfEmpty(Mono.error(new ApproveListingException()))
                                .map(ListingModelDomain::submitForReview)
                                .flatMap(listingPort::save)
                                .map(newListing -> new ApproveListingCommandResult(
                                                newListing.getListingId(),
                                                String.valueOf(newListing.getReviewStatus()),
                                                String.valueOf(newListing.getPublicationStatus())));
        }

        @Override
        public Mono<RejectedListingCommandResult> rejectedListing(RejectedListingCommand cmd) {
                return listingPort.findByListingId(cmd.listingId())
                                .switchIfEmpty(Mono.error(new SuspendListingException()))
                                .map(ListingModelDomain::rejectReview)
                                .flatMap(listingPort::save)
                                .map(newListing -> new RejectedListingCommandResult())
                                .onErrorMap(IllegalStateException.class,
                                                e -> new IllegalStateException(
                                                                "Error to process the suspend state listing"));
        }

        // metodos de publicacion de listings (suspender)
        @Override
        public Mono<SuspendListingCommandResult> suspendListing(SuspendListingCommand cmd) {
                return listingPort.findByListingId(cmd.listingId())
                                .switchIfEmpty(Mono.error(new SuspendListingException()))
                                .map(ListingModelDomain::suspend)
                                .flatMap(listingPort::save)
                                .map(newListing -> new SuspendListingCommandResult())
                                .onErrorMap(IllegalStateException.class,
                                                e -> new IllegalStateException(
                                                                "Error to process the suspend state listing"));
        }

        @Override
        public Mono<PublishListingCommandResult> publishListing(PublishListingCommand cmd) {
                return listingPort.findByListingId(cmd.listingId())
                                .switchIfEmpty(Mono.error(new PublishListingException()))
                                .map(ListingModelDomain::publish)
                                .flatMap(listingPort::save)
                                .map(newListing -> new PublishListingCommandResult())
                                .onErrorMap(IllegalStateException.class,
                                                e -> new IllegalStateException(
                                                                "Error to process the suspend state listing"));
        }
}
