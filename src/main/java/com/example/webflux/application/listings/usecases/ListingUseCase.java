package com.example.webflux.application.listings.usecases;

import com.example.webflux.application.listings.command.ChangeStatusListingCommand;
import com.example.webflux.application.listings.command.ChangeStatusListingCommandResult;
import com.example.webflux.application.listings.command.CreateListingCommand;
import com.example.webflux.application.listings.command.CreateListingCommandResult;
import com.example.webflux.application.listings.command.GetAllListingByCursorPaginationCommand;
import com.example.webflux.application.listings.command.GetAllListingByCursorPaginationCommandResult;
import com.example.webflux.application.listings.command.GetListingByIdCommand;
import com.example.webflux.application.listings.command.GetListingByIdCommandResult;

import reactor.core.publisher.Mono;

public interface ListingUseCase {

    Mono<GetAllListingByCursorPaginationCommandResult> getAllListings(GetAllListingByCursorPaginationCommand cmd);

    Mono<GetListingByIdCommandResult> getListingById(GetListingByIdCommand cmd);

    Mono<CreateListingCommandResult> createListing(CreateListingCommand cmd);

    Mono<ChangeStatusListingCommandResult> changeStatusListing(ChangeStatusListingCommand cmd);
}
