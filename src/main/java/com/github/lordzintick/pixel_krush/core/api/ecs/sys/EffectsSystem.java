package com.github.lordzintick.pixel_krush.core.api.ecs.sys;

import com.github.lordzintick.pixel_krush.core.api.ecs.Entity;
import com.github.lordzintick.pixel_krush.core.api.ecs.comp.EffectsComponent;

public class EffectsSystem extends AbstractSystem {
    private final EffectsComponent effectsComponent;

    public EffectsSystem(Entity parent) {
        super(parent);
        this.effectsComponent = loadDependency(parent.getId("effects"));
    }

    @Override
    public void update(float deltaTime) {
        effectsComponent.tickAll(deltaTime);
        effectsComponent.flush();
    }
}
