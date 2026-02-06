package com.github.lordzintick.pixel_krush.core.impl;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector4;
import com.github.lordzintick.pixel_krush.core.api.AbstractGameObject;
import com.github.lordzintick.pixel_krush.core.util.Logger;
import com.github.lordzintick.pixel_krush.core.api.AbstractGameScreen;

/**
 * An implementation of {@link AbstractGameObject} that provides many utilities for somewhat complex movement and simple animation.
 */
public class Particle extends AbstractGameObject {
    /**
     * The velocity of the particle, in the format <code>x, y, scale, angle</code>
     */
    public Vector4 velocity;
    /**
     * An array of textures used for the display and animation of the particle.
     */
    public final TextureRegion[] frames;
    /**
     * The scale of the particle from its texture's original size.
     */
    public float scale;
    /**
     * Used for calculating when to go to the next frame.
     */
    private float animTicks = 0;
    /**
     * The amount of time the particle has existed for, in seconds.<br>
     * Used for calculating when to remove the particle based on its lifetime.
     */
    private float ticks = 0;
    /**
     * The current frame the particle is drawing.
     */
    private int frame = 0;
    /**
     * The amount of time it takes for the particle to advance one frame in its animation.
     */
    public final float frameTime;
    /**
     * The amount of time the particle should exist for.
     */
    private final float lifeTime;
    /**
     * The angle the particle is at.
     */
    private float angle = 0;

    /**
     * Constructs a new {@link Particle} with the provided parameters.
     * @param screen The {@link AbstractGameScreen} that contains this particle.
     * @param frames An array of {@link TextureRegion}s representing the animation frames this particle should draw.
     * @param scale The initial scale of the particle relative to its texture's native size.
     * @param frameTime The amount of time it takes for the particle to advance one frame in its animation.
     * @param lifeTime The amount of time the particle should exist for.
     */
    public Particle(AbstractGameScreen screen, TextureRegion[] frames, float scale, float frameTime, float lifeTime) {
        super(screen);
        this.frames = frames;
        this.scale = scale;
        this.frameTime = frameTime;
        this.lifeTime = lifeTime;
    }

    @Override
    public void render(Batch batch, float deltaTime) {
        animTicks += deltaTime;
        if (animTicks >= frameTime) {
            animTicks = 0;
            if (frame >= frames.length - 1) {
                frame = 0;
            } else {
                frame++;
            }
        }

        TextureRegion frameToDraw = frames[frame];
        batch.draw(frameToDraw, x, y, 0, 0, frameToDraw.getRegionWidth(), frameToDraw.getRegionHeight(), scale, scale, angle);
    }

    @Override
    public void update(float deltaTime) {
        x += velocity.x * deltaTime;
        y += velocity.y * deltaTime;
        scale = Math.max(0, scale + velocity.z * deltaTime);
        angle += velocity.w * deltaTime;

        ticks += deltaTime;
        if (ticks >= lifeTime || scale <= 0) {
            remove();
        }
    }
}
