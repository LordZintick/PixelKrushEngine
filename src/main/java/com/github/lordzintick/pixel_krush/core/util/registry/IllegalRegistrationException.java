package com.github.lordzintick.pixel_krush.core.util.registry;

/**
 * Thrown when something attempts to register but can't due to some rule or problem.
 */
public class IllegalRegistrationException extends RuntimeException {
    public IllegalRegistrationException(String message) {
        super("Illegal registration: " + message);
    }
}
