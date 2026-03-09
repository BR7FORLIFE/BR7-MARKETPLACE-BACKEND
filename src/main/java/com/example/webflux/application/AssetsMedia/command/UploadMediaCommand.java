package com.example.webflux.application.AssetsMedia.command;

import java.io.File;
import java.util.UUID;

public record UploadMediaCommand(UUID listingId, UUID ownerId, File file) {

}
