package com.github.lordzintick.pixel_krush.core.util.registry;

/**
 * Thrown if an error occurred within a registry.
 */
public class RegistryError extends Exception {
    public RegistryError(String message) {
        super("Error in registry: " + message);
    }
}
