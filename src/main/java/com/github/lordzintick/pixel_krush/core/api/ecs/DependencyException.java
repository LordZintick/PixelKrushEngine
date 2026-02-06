package com.github.lordzintick.pixel_krush.core.api.ecs;

/**
 * Indicates that an exception occurred when loading a dependency.
 */
public class DependencyException extends RuntimeException {
    public DependencyException(String message) {
        super("Error in dependency loading: " + message);
    }
}
