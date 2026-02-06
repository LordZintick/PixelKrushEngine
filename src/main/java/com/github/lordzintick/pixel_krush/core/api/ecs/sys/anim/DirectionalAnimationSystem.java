package com.github.lordzintick.pixel_krush.core.api.ecs.sys.anim;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.github.lordzintick.pixel_krush.core.api.ecs.Entity;
import com.github.lordzintick.pixel_krush.core.api.ecs.comp.ValueComponent;
import com.github.lordzintick.pixel_krush.core.util.Direction;

/**
 * An animation system that animates a character that can face in 4 directions.
 */
public class DirectionalAnimationSystem extends AbstractAnimationSystem {
    /**
     * A {@link ValueComponent} used to track the current direction of the entity.
     */
    private final ValueComponent<Direction> direction;

    /**
     * Constructs a new {@link DirectionalAnimationSystem} and initializes dependencies.
     * @param parent The parent {@link Entity} of the system.
     */
    public DirectionalAnimationSystem(Entity parent) {
        super(parent);
        direction = loadDependency(parent.getId("direction"));
    }

    @Override
    public void render(Batch batch, float deltaTime) {
        animation.texture = textures.get(0, 0);

        if (!moving.get()) {
            switch (direction.get()) {
                case DOWN:
                    break;
                case LEFT:
                    animation.texture = textures.get(0, 1);
                    break;
                case RIGHT:
                    animation.texture = textures.get(0, 2);
                    break;
                case UP:
                    animation.texture = textures.get(0, 3);
                    break;
            }
        } else {
            if (animation.animTicks >= animation.frameTime) {
                animation.animTicks = 0;
                if (animation.frame == animation.frameCount - 1)
                    animation.frame = 0;
                else
                    animation.frame++;
            }

            switch (direction.get()) {
                case DOWN :
                    animation.texture = textures.get(0, 4 + animation.frame);
                    break;
                case LEFT :
                    animation.texture = textures.get(0, 4 + animation.frameCount + animation.frame);
                    break;
                case RIGHT :
                    animation.texture = textures.get(0, 4 + 2 * animation.frameCount + animation.frame);
                    break;
                case UP :
                    animation.texture = textures.get(0, 4 + 3 * animation.frameCount + animation.frame);
                    break;
            };
        }

        draw(batch, animation.texture);
        super.render(batch, deltaTime);
    }

    @Override
    protected void renderColored(Batch batch, float deltaTime) {
        draw(batch, animation.texture);
    }
}
