package com.github.lordzintick.pixel_krush.core.api.ecs.sys.anim;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.github.lordzintick.pixel_krush.core.api.ecs.Entity;
import com.github.lordzintick.pixel_krush.core.api.ecs.comp.AnimationComponent;
import com.github.lordzintick.pixel_krush.core.api.ecs.comp.TextureArrayComponent;
import com.github.lordzintick.pixel_krush.core.api.ecs.comp.ValueComponent;
import com.github.lordzintick.pixel_krush.core.api.ecs.sys.ColorModifierSystem;
import com.github.lordzintick.pixel_krush.core.util.Logger;

/**
 * A base class for all systems that animate an entity.<br>
 * Provides a few common component dependencies shared by most animation systems.
 */
public abstract class AbstractAnimationSystem extends ColorModifierSystem {
    /**
     * A {@link TextureArrayComponent} to store the split spritesheet for the entity.
     */
    protected final TextureArrayComponent textures;
    /**
     * An {@link AnimationComponent} to store animation-related metadata.
     */
    protected final AnimationComponent animation;
    /**
     * A {@link ValueComponent} to store whether the entity is moving.
     */
    protected final ValueComponent<Boolean> moving;
    /**
     * A {@link ValueComponent} to store the angle of the entity.
     */
    protected final ValueComponent<Float> angle;

    /**
     * Constructs a new {@link AbstractAnimationSystem} and initialized all the dependencies.
     * @param parent The parent {@link Entity} of the system.
     */
    public AbstractAnimationSystem(Entity parent) {
        super(parent);
        textures = loadDependency(parent.getId("textures"));
        animation = loadDependency(parent.getId("animation"));
        moving = loadDependency(parent.getId("moving"));
        angle = loadDependency(parent.getId("angle"));
    }

    @Override
    public void render(Batch batch, float deltaTime) {
        animation.animTicks += deltaTime;
        super.render(batch, deltaTime);
    }

    /**
     * A utility method that draws a texture with the entity's position, rotation, and scale.
     * @param batch The {@link Batch} to draw with.
     * @param texture The {@link TextureRegion} to draw.
     */
    protected void draw(Batch batch, TextureRegion texture) {
        batch.draw(texture, parent.x, parent.y, (float) parent.width / 2 * parent.scale, (float) parent.height / 2 * parent.scale, parent.width * parent.scale, parent.height * parent.scale, 1, 1, angle.get());
    }
}
