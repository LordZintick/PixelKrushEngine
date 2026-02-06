package com.github.lordzintick.pixel_krush.core.api.ecs.sys.anim;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.github.lordzintick.pixel_krush.core.api.ecs.Entity;

/**
 * An animation system used to animate simple objects with one constantly-playing animation.
 */
public class SimpleAnimationSystem extends AbstractAnimationSystem {
    /**
     * Constructs a new {@link SimpleAnimationSystem} and initializes dependencies.
     * @param parent The parent {@link Entity} of the system.
     */
    public SimpleAnimationSystem(Entity parent) {
        super(parent);
    }

    @Override
    public void render(Batch batch, float deltaTime) {
        if (animation.animTicks >= animation.frameTime) {
            animation.animTicks = 0;
            if (animation.frame == animation.frameCount - 1)
                animation.frame = 0;
            else
                animation.frame++;
        }
        animation.texture = textures.get(0, animation.frame);
        draw(batch, animation.texture);
        super.render(batch, deltaTime);
    }

    @Override
    protected void renderColored(Batch batch, float deltaTime) {
        draw(batch, animation.texture);
    }
}
