package com.github.lordzintick.pixel_krush.core.api.ecs.comp;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.github.lordzintick.pixel_krush.core.api.ecs.Entity;

public class AnimationComponent extends AbstractComponent {
    public final float frameTime;
    public final int frameCount;

    public float animTicks = 0;
    public int frame = 0;
    public TextureRegion texture;

    public AnimationComponent(Entity parent, float frameTime, int frameCount) {
        super(parent);
        this.frameTime = frameTime;
        this.frameCount = frameCount;
    }
}
