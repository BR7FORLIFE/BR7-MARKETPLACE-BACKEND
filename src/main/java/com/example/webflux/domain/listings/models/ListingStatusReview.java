package com.example.webflux.domain.listings.models;

import com.example.webflux.domain.listings.exceptions.InvalidStateException;

public enum ListingStatusReview {
    DRAFT {
        @Override
        public ListingStatusReview submit() {
            return IN_REVIEW;
        }
    },

    IN_REVIEW {
        @Override
        public ListingStatusReview approve() {
            return PUBLISHED;
        }

        @Override
        public ListingStatusReview requestFix() {
            return NEEDS_FIX;
        }

        @Override
        public ListingStatusReview reject() {
            return REJECTED;
        }
    },

    NEEDS_FIX {
        @Override
        public ListingStatusReview resubmit() {
            return IN_REVIEW;
        }
    },

    PUBLISHED,
    REJECTED;

    // default
    public ListingStatusReview submit() {
        throw invalid("");
    }

    public ListingStatusReview approve() {
        throw invalid("");
    }

    public ListingStatusReview requestFix() {
        throw invalid("");
    }

    public ListingStatusReview reject() {
        throw invalid("");
    }

    public ListingStatusReview resubmit() {
        throw invalid("");
    }

    private RuntimeException invalid(String action) {
        return new InvalidStateException(this, action);
    }
}

/**
 * Estados de listings o publicaciones por el usuario
 * 
 * - PENDING -> En espera en que un admin o directivo revise la publicación del
 * vendedor
 * 
 * - SUCCESFULL -> El admin o directivo ha aprovado tu publicacion y puedes
 * publicarlo en la app
 * 
 * - ISSUE -> EL admin o directivo te notifican el porque la publicación tiene
 * problemas y que deberias de cambiar
 * 
 * - REFUSED -> El admin o directivo rechazo tu publicación ya sea por
 * inapropiado o violando las politicas
 * 
 * Arbol de estados
 * 
 * Estado inicial PENDING -> ISSUE -> SUCCESFULL O REFUSED
 * 
 * O:
 * 
 * ESTADO INICIAL PENDING -> SUCCESFULL O REFUSED // rechazado o aceptado
 * directamente sin pasar por ISSUE
 * 
 * 
 * 
 */