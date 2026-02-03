package com.lordzintick.pixel_krush.core.api.ecs;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.lordzintick.pixel_krush.core.api.AbstractGameScreen;
import com.lordzintick.pixel_krush.core.api.TiledAtlas;
import com.lordzintick.pixel_krush.core.api.ecs.comp.*;
import com.lordzintick.pixel_krush.core.api.ecs.sys.anim.AbstractAnimationSystem;
import com.lordzintick.pixel_krush.core.api.ecs.sys.EffectsSystem;
import com.lordzintick.pixel_krush.core.util.Direction;

public abstract class LivingEntity extends Entity {
    public final RangedFloatComponent health;
    protected final TextureArrayComponent textures;
    protected final AnimationComponent animation;
    public final BiValueComponent<Float, Float> speed;
    protected final ValueComponent<Float> angle;
    protected final ValueComponent<Boolean> moving;
    protected final ValueComponent<Direction> direction;
    private final EffectsComponent effectsComponent;

    public float iframes = 0;

    /**
     * Constructs a new {@link LivingEntity} with the provided spritesheet, which will be split into regions of the provided size
     *
     * @param screen The {@link AbstractGameScreen} containing this entity
     * @param width  The width of the entity's damage
     * @param height The height of the entity's image
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

    public void applyEffect(Effect effect) {
        effect.apply(this);
        effectsComponent.applyEffect(effect);
    }

    public boolean hasEffect(Effect effect) {
        return effectsComponent.hasEffect(effect);
    }

    public void damage(float amount) {
        damage(amount, false);
    }

    public void damage(float amount, boolean noImmunity) {
        health.set(health.get() - amount);
        if (!noImmunity)
            iframes = 0.25f;
    }

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

    public abstract int getMaxHealth();
    protected abstract AbstractAnimationSystem getAnimationSystem();
    protected abstract float getFrameTime();
    protected abstract int getFrameCount();
}
