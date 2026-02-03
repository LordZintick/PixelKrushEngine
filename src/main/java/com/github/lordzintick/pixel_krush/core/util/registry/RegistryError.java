package com.github.lordzintick.pixel_krush.core.util.registry;

public class RegistryError extends Exception {
    public RegistryError(String message) {
        super("Exception in registry: " + message);
    }
}
