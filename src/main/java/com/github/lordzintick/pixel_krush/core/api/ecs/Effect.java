package com.github.lordzintick.pixel_krush.core.api.ecs;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.github.lordzintick.pixel_krush.core.api.AbstractGame;

public abstract class Effect {
    public final TextureRegion sprite;
    public final AbstractGame game;
    public float timeLeft;
    public int level;

    protected Effect(int spritex, int spritey, AbstractGame game, float time, int level) {
        this.sprite = game.getCachedAtlas("effects").get(spritex, spritey);
        this.game = game;
        this.timeLeft = time;
        this.level = level;
    }

    public abstract void apply(LivingEntity entity);
    public abstract void tick(LivingEntity entity, float deltaTime);
    public abstract void end(LivingEntity entity);
}
