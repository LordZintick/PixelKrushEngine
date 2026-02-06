package com.github.lordzintick.pixel_krush.core.util;

import com.badlogic.gdx.graphics.g2d.Batch;

/**
 * A small interface used to provide a simple standard for a render method.
 */
public interface IRenderable {
    /**
     * Renders the object onto the screen.
     * @param batch The {@link Batch} to use for rendering.
     * @param deltaTime The time since the last frame was rendered.
     */
    void render(Batch batch, float deltaTime);
}
