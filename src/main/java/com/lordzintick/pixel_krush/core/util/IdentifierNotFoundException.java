package com.lordzintick.pixel_krush.core.util;

public class IdentifierNotFoundException extends RuntimeException {
    public IdentifierNotFoundException(String id) {
        super("ID \"" + id + "\" could not be found");
    }
}
