package com.lordzintick.pixel_krush.core.util.registry;

public class IllegalRegistrationException extends RuntimeException {
    public IllegalRegistrationException(String message) {
        super("Illegal registration: " + message);
    }
}
