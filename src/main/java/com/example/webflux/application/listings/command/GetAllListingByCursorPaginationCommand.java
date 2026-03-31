package com.example.webflux.application.listings.command;

import java.util.UUID;

import com.example.webflux.domain.listings.models.ListingPublicationStatus;
import com.example.webflux.domain.listings.models.ListingStatusReview;

public record GetAllListingByCursorPaginationCommand(String cursor, int limit, ListingStatusReview review,
        ListingPublicationStatus publicationStatus, UUID userId) {

}

/**
 * Cursor -> raw codificado en base64 y nos permite hacer la siguiente
 * paginacion
 * Limit -> limite de resultados devueltos
 * userId -> para verificar roles y permisos
 * ListingStatusReview | ListingPublicationStatus en caso de ser admin poder
 * obtner los diferentes
 * listing segun el estado de publicacion o de revision
 */