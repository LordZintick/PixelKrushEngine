package com.github.lordzintick.pixel_krush.core.util;

/**
 * A small interface used to provide a universal update method.
 */
public interface IUpdateable {
    /**
     * Updates the object.
     * @param deltaTime The time since the last frame was rendered.
     */
    void update(float deltaTime);
}
