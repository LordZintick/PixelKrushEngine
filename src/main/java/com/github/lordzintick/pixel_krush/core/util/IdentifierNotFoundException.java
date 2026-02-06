package com.github.lordzintick.pixel_krush.core.util;

/**
 * Throws when an {@link Identifier} could not be found for whatever reason.
 */
public class IdentifierNotFoundException extends RuntimeException {
    public IdentifierNotFoundException(String id) {
        super("ID \"" + id + "\" could not be found");
    }
}
