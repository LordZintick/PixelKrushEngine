package com.lordzintick.pixel_krush.core.util;

public class AssetException extends RuntimeException {
    public AssetException(String message) {
        super("Exception in asset loading: " + message);
    }
}
