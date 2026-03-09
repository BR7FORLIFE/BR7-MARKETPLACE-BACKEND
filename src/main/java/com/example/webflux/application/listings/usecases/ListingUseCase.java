package com.example.webflux.application.listings.usecases;

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

import reactor.core.publisher.Mono;

public interface ListingUseCase {
    Mono<PublishListingCommandResult> publishListing(PublishListingCommand cmd);

    Mono<CreateListingCommandResult> createListing(CreateListingCommand cmd);

    Mono<ApproveListingCommandResult> approveListing(ApproveListingCommand cmd);

    Mono<SuspendListingCommandResult> suspendListing(SuspendListingCommand cmd);

    Mono<RejectedListingCommandResult> rejectedListing(RejectedListingCommand cmd);
}
