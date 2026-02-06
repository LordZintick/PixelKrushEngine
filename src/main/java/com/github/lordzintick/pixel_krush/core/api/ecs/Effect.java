package com.github.lordzintick.pixel_krush.core.api.ecs;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.github.lordzintick.pixel_krush.core.api.AbstractGame;

/**
 * An abstract base class representing a status effect that an {@link Entity} can have.
 */
public abstract class Effect {
    /**
     * A {@link TextureRegion} representing the icon for the effect.
     */
    public final TextureRegion sprite;
    /**
     * The parent {@link AbstractGame Game} of the effect.
     */
    public final AbstractGame game;
    /**
     * The amount of time remaining until the effect should be removed, in seconds.
     */
    public float timeLeft;
    /**
     * The amplifier of the effect, starting at 1 for the normal intensity.
     */
    public int level;

    /**
     * Constructs a new effect with the provided configuration.<br>
     * Gets its icon from an atlas cached by the game called "<code>effects</code>" at the specified x and y coordinates.
     * @param spritex The x-coordinate in the atlas of the effect icon.
     * @param spritey The y-coordinate in the atlas of the effect icon.
     * @param game The {@link AbstractGame Game} that this effect belongs to.
     * @param time The time that the effect should be applied for, in seconds.
     * @param level The amplifier/intensity of the effect, starting at 1 for normal intensity.
     */
    protected Effect(int spritex, int spritey, AbstractGame game, float time, int level) {
        this.sprite = game.getCachedAtlas("effects").get(spritex, spritey);
        this.game = game;
        this.timeLeft = time;
        this.level = level;
    }

    /**
     * Called when this effect is applied to an entity.
     * @param entity The {@link LivingEntity} to apply this effect to.
     */
    public abstract void apply(LivingEntity entity);

    /**
     * Called every frame the entity that has this effect is updated/ticked.
     * @param entity The {@link LivingEntity} that has this effect.
     * @param deltaTime The time since the last frame was rendered.
     */
    public abstract void tick(LivingEntity entity, float deltaTime);

    /**
     * Called when this effect is removed from an entity.
     * @param entity The {@link LivingEntity} that this effect is being removed from.
     */
    public abstract void end(LivingEntity entity);
}
