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

public abstract class AbstractAnimationSystem extends ColorModifierSystem {
    private static final Logger LOGGER = new Logger(AbstractAnimationSystem.class);

    protected final TextureArrayComponent textures;
    protected final AnimationComponent animation;
    protected final ValueComponent<Boolean> moving;
    protected final ValueComponent<Float> angle;

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

    protected void draw(Batch batch, TextureRegion texture) {
        batch.draw(texture, parent.x, parent.y, (float) parent.width / 2 * parent.scale, (float) parent.height / 2 * parent.scale, parent.width * parent.scale, parent.height * parent.scale, 1, 1, angle.get());
    }
}
