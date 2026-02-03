package com.lordzintick.pixel_krush.core.api;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector4;
import com.lordzintick.pixel_krush.core.impl.Particle;

/**
 * An abstract class representing the base of all "game" screens, that is to say they are part of the physical game and not part of any UI menu
 */
public abstract class AbstractGameScreen extends BaseScreen {

    /**
     * Constructs a new {@link AbstractGameScreen} with the provided {@link AbstractGame}
     * @param game The {@link AbstractGame} instance that this screen is for
     */
    public AbstractGameScreen(AbstractGame game) {
        super(game);
    }

    public void addParticle(TextureRegion[] frames, float x, float y, Vector4 velocity, float scale, float frameTime, float lifeTime) {
        if (Math.abs(x) >= Gdx.graphics.getWidth() * 2 || Math.abs(y) >= Gdx.graphics.getWidth() * 2 || x == 0 || y == 0) return;

        Particle particle = new Particle(this, frames, scale, frameTime, lifeTime);
        particle.x = x;
        particle.y = y;
        particle.velocity = velocity;
        objects.add(particle);
    }
}
