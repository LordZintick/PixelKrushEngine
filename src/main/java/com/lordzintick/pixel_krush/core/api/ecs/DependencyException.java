package com.lordzintick.pixel_krush.core.api.ecs;

public class DependencyException extends RuntimeException {
    public DependencyException(String message) {
        super("Error in dependency loading: " + message);
    }
}
