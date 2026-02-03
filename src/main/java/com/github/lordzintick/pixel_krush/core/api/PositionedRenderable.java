package com.github.lordzintick.pixel_krush.core.api;

import com.github.lordzintick.pixel_krush.core.util.IRenderable;

/**
 * An abstraction of the {@link IRenderable} interface that provides a position
 */
public abstract class PositionedRenderable implements IRenderable {
    public float x, y = 0;

    /**
     * Constructs a new {@link PositionedRenderable} with {@code x} and {@code y} set to 0
     */
    protected PositionedRenderable() {}

    /**
     * Constructs a new {@link PositionedRenderable} with the provided parameters as the initial x and y coordinates
     * @param x The initial x position of the {@link PositionedRenderable}
     * @param y The initial y position of the {@link PositionedRenderable}
     */
    protected PositionedRenderable(float x, float y) {
        this.x = x;
        this.y = y;
    }
}
