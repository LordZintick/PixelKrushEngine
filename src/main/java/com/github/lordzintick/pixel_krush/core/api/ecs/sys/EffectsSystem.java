package com.github.lordzintick.pixel_krush.core.api.ecs.sys;

import com.github.lordzintick.pixel_krush.core.api.ecs.Entity;
import com.github.lordzintick.pixel_krush.core.api.ecs.comp.EffectsComponent;

/**
 * A simple system used to handle an {@link EffectsComponent}, updating it as needed.
 */
public class EffectsSystem extends AbstractSystem {
    /**
     * An {@link EffectsComponent} used to store and handle all the parent {@link Entity}'s effects.
     */
    private final EffectsComponent effectsComponent;

    /**
     * Constructs a new {@link EffectsSystem} and initializes dependencies.
     * @param parent The parent {@link Entity} of the system.
     */
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
