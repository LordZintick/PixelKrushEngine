package com.github.lordzintick.pixel_krush.core.util;

/**
 * Thrown if an error occurs during asset loading.
 */
public class AssetException extends RuntimeException {
    public AssetException(String message) {
        super("Exception in asset loading: " + message);
    }
}
