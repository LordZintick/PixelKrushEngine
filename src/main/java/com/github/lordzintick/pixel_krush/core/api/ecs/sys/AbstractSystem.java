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

/**
 * A base class representing a "system" in the ECS used for {@link Entity Entities}.
 */
public abstract class AbstractSystem extends AbstractEntityAttachment {
    /**
     * A {@link Registry} used to uniquely identify and hold all {@link AbstractComponent}s that this system depends on.
     */
    private final Registry<AbstractComponent> dependencies = new Registry<>();

    /**
     * Constructs a new {@link AbstractSystem} connected to the provided entity.
     * @param parent The parent {@link Entity} of this system.
     */
    public AbstractSystem(Entity parent) {
        super(parent);
    }

    /**
     * Loads a component with the provided ID as a dependency of this system.
     * @param id The identifier of the component to load as a dependency of this system.
     * @param <T> The type of component to load.
     * @return The loaded dependency.
     */
    protected <T extends AbstractComponent> T loadDependency(Identifier id) {
        ImmutableRegistry<AbstractComponent> components = parent.queryComponents();
        try {
            return components.getOrError(id);
        } catch (RegistryError e) {
            throw new DependencyException("Component with id \"" + id + "\" does not exist");
        }
    }

    /**
     * Gets a dependency with a certain ID.
     * @param id The identifier of the dependency to get from the {@link AbstractSystem#dependencies} registry.
     * @param <T> The type of dependency to get.
     * @return The dependency retrieved with the provided ID.
     */
    protected <T extends AbstractComponent> T getDependency(Identifier id) {
        try {
            return dependencies.getOrError(id);
        } catch (RegistryError e) {
            throw new DependencyException("Could not find dependency with id \"" + id + "\"");
        }
    }

    /**
     * Called every time the parent {@link Entity} is rendered, so the system can do its rendering logic.
     * There is no default implementation of this, so exactly what happens here depends on the system used.
     * @param batch The {@link Batch} to render with.
     * @param deltaTime The time since the last frame was rendered.
     */
    public void render(Batch batch, float deltaTime) {}
    /**
     * Called every time the parent {@link Entity} is updated, so the system can do its update logic.
     * There is no default implementation of this, so exactly what happens here depends on the system used.
     * @param deltaTime The time since the last frame was rendered.
     */
    public void update(float deltaTime) {}
    /**
     * Called when the parent {@link Entity} is removed from the screen it is in.
     */
    public void remove() {}
}
