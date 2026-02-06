package com.github.lordzintick.pixel_krush.core.api.ecs;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.github.lordzintick.pixel_krush.core.api.AbstractGameScreen;
import com.github.lordzintick.pixel_krush.core.api.TiledAtlas;
import com.github.lordzintick.pixel_krush.core.api.ecs.comp.*;
import com.github.lordzintick.pixel_krush.core.api.ecs.sys.anim.AbstractAnimationSystem;
import com.github.lordzintick.pixel_krush.core.api.ecs.sys.EffectsSystem;
import com.github.lordzintick.pixel_krush.core.util.Direction;

/**
 * An extension of {@link Entity} that contains some methods and components unique to moving, living entities, such as enemies or players.
 */
public abstract class LivingEntity extends Entity {
    /**
     * A {@link RangedFloatComponent} representing the current health points of this entity.
     */
    public final RangedFloatComponent health;
    /**
     * A {@link TextureArrayComponent} holding the split spritesheet of this entity.
     */
    protected final TextureArrayComponent textures;
    /**
     * An {@link AnimationComponent} holding animation and graphical-related metadata.
     */
    protected final AnimationComponent animation;
    /**
     * A {@link BiValueComponent} holding the base speed and the speed multiplier, respectively.
     */
    public final BiValueComponent<Float, Float> speed;
    /**
     * A {@link ValueComponent} holding the angle of this entity.
     */
    protected final ValueComponent<Float> angle;
    /**
     * A {@link ValueComponent} representing whether this entity is currently moving.
     */
    protected final ValueComponent<Boolean> moving;
    /**
     * A {@link ValueComponent} representing the direction this entity is facing.
     */
    protected final ValueComponent<Direction> direction;
    /**
     * An {@link EffectsComponent} that holds all the status effects this entity is currently experiencing.
     */
    private final EffectsComponent effectsComponent;

    /**
     * The remaining i-frames (immunity time) of this entity, in seconds.
     */
    public float iframes = 0;

    /**
     * Constructs a new {@link LivingEntity} with the provided spritesheet, which will be split into regions of the provided size, and initializes components/systems.
     *
     * @param screen The {@link AbstractGameScreen} containing this entity.
     * @param width  The width of the entity's image.
     * @param height The height of the entity's image.
     */
    public LivingEntity(AbstractGameScreen screen, Texture texture, int width, int height) {
        super(screen, width, height);
        textures = components.register(getId("textures"), new TextureArrayComponent(this, texture, width, height));
        animation = components.register(getId("animation"), new AnimationComponent(this, getFrameTime(), getFrameCount()));
        angle = components.register(getId("angle"), new ValueComponent<>(this, 0f));
        moving = components.register(getId("moving"), new ValueComponent<>(this, false));
        direction = components.register(getId("direction"), new ValueComponent<>(this, Direction.DOWN));
        effectsComponent = components.register(getId("effects"), new EffectsComponent(this));
        health = components.register(getId("health"), new RangedFloatComponent(this, getMaxHealth(), 0, getMaxHealth()));
        speed = components.register(getId("speed"), new BiValueComponent<>(this, 1f, 1f));

        systems.register(getId("effects"), new EffectsSystem(this));
        systems.register(getId("animation"), getAnimationSystem());
    }

    /**
     * Called when this entity dies.
     */
    public void onDeath() {
        remove();
    }

    /**
     * Applies a specified effect to this entity.
     * @param effect The {@link Effect} to apply to this entity.
     */
    public void applyEffect(Effect effect) {
        effect.apply(this);
        effectsComponent.applyEffect(effect);
    }

    /**
     * Gets whether this entity is experiencing a specified status effect.
     * @param effect The {@link Effect} to check whether this entity is experiencing.
     * @return Whether this entity is experiencing the specified status effect.
     */
    public boolean hasEffect(Effect effect) {
        return effectsComponent.hasEffect(effect);
    }

    /**
     * Damages the entity by the specified amount, with immunity frames.
     * @param amount The amount to damage this entity by. If you want to heal the entity, use {@link LivingEntity#heal(float)} instead.
     */
    public void damage(float amount) {
        damage(amount, false);
    }

    /**
     * Damages the entity by the specified amount.
     * @param amount The amount to damage this entity by. If you want to heal the entity, use {@link LivingEntity#heal(float)} instead.
     * @param noImmunity Whether to not trigger the i-frame damage cooldown of 0.25 seconds.
     */
    public void damage(float amount, boolean noImmunity) {
        health.set(health.get() - amount);
        if (!noImmunity)
            iframes = 0.25f;
    }

    /**
     * Heals the entity by the specified amount.
     * @param amount The amount to heal this entity by. If you want to damage the entity, use {@link LivingEntity#damage(float)} instead.
     */
    public void heal(float amount) {
        health.set(Math.min(getMaxHealth(), health.get() + amount));
        screen.game.getAudioSample("heal").play();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (iframes > 0) {
            iframes -= deltaTime;
            colorModifier.set(Color.RED);
        } else if (colorModifier.get() == Color.RED) {
            colorModifier.set(Color.WHITE);
        }

        if (health.get() <= 0 && ticks >= 1f) {
            onDeath();
        }
    }

    @Override
    public void render(Batch batch, float deltaTime) {
        super.render(batch, deltaTime);
        for (int i = 0; i < effectsComponent.size(); i++) {
            Effect effect = effectsComponent.getEffectOrNull(i);
            batch.draw(effect.sprite, x + (float) width / 2 * scale - 8, y + height * scale + 10 + i * 26, 16, 16);
        }
    }

    /**
     * Used to define the maximum health of the entity.
     * @return The maximum health of the entity.
     */
    public abstract int getMaxHealth();

    /**
     * Used to define the animation system to use for animation of the entity.
     * @return The animation system to be used by the entity.
     */
    protected abstract AbstractAnimationSystem getAnimationSystem();

    /**
     * Used to define the amount of time it takes to advance to the next frame of animation.
     * @return The time it takes to advance to the next frame of animation.
     */
    protected abstract float getFrameTime();

    /**
     * Used to define the amount of frames the entity has in its animation(s).
     * @return The amount of frames the entity has in its animation(s).
     */
    protected abstract int getFrameCount();
}
