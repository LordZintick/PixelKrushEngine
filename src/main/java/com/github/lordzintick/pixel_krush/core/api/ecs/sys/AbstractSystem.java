package com.github.lordzintick.pixel_krush.core.api.ecs.sys;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.github.lordzintick.pixel_krush.core.api.ecs.AbstractEntityAttachment;
import com.github.lordzintick.pixel_krush.core.api.ecs.DependencyException;
import com.github.lordzintick.pixel_krush.core.api.ecs.Entity;
import com.github.lordzintick.pixel_krush.core.api.ecs.comp.AbstractComponent;
import com.github.lordzintick.pixel_krush.core.util.Identifier;
import com.github.lordzintick.pixel_krush.core.util.registry.ImmutableRegistry;
import com.github.lordzintick.pixel_krush.core.util.registry.Registry;
import com.github.lordzintick.pixel_krush.core.util.registry.RegistryError;

public abstract class AbstractSystem extends AbstractEntityAttachment {
    private final Registry<AbstractComponent> dependencies = new Registry<>();

    public AbstractSystem(Entity parent) {
        super(parent);
    }

    protected <T extends AbstractComponent> T loadDependency(Identifier id) {
        ImmutableRegistry<AbstractComponent> components = parent.queryComponents();
        try {
            return components.getOrError(id);
        } catch (RegistryError e) {
            throw new DependencyException("Component with id \"" + id + "\" does not exist");
        }
    }

    protected <T extends AbstractComponent> T getDependency(Identifier id) {
        try {
            return dependencies.getOrError(id);
        } catch (RegistryError e) {
            throw new DependencyException("Could not find dependency with id \"" + id + "\"");
        }
    }

    public void spawn() {}
    public void render(Batch batch, float deltaTime) {}
    public void update(float deltaTime) {}
    public void remove() {}
}
