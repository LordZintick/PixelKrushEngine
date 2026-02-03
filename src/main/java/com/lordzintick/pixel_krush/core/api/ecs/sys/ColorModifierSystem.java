package com.lordzintick.pixel_krush.core.api.ecs.sys;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.lordzintick.pixel_krush.core.api.ecs.Entity;
import com.lordzintick.pixel_krush.core.api.ecs.comp.ValueComponent;

public class ColorModifierSystem extends AbstractSystem {
    private final ValueComponent<Color> colorModifier;

    public ColorModifierSystem(Entity parent) {
        super(parent);
        this.colorModifier = loadDependency(parent.getId("color_modifier"));
    }

    @Override
    public void render(Batch batch, float deltaTime) {
        batch.setColor(colorModifier.get().cpy().sub(0, 0, 0, 0.5f));
        renderColored(batch, deltaTime);
        batch.setColor(Color.WHITE);
    }

    protected void renderColored(Batch batch, float deltaTime) {}
}
