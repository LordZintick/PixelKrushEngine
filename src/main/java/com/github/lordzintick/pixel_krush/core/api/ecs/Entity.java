package com.github.lordzintick.pixel_krush.core.api.ecs;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.github.lordzintick.pixel_krush.core.api.ecs.comp.AbstractComponent;
import com.github.lordzintick.pixel_krush.core.api.ecs.comp.ValueComponent;
import com.github.lordzintick.pixel_krush.core.api.ecs.sys.AbstractSystem;
import com.github.lordzintick.pixel_krush.core.api.ecs.sys.ColorModifierSystem;
import com.github.lordzintick.pixel_krush.core.util.Identifier;
import com.github.lordzintick.pixel_krush.core.util.Logger;
import com.github.lordzintick.pixel_krush.core.api.AbstractGameObject;
import com.github.lordzintick.pixel_krush.core.api.AbstractGameScreen;
import com.github.lordzintick.pixel_krush.core.util.registry.ImmutableRegistry;
import com.github.lordzintick.pixel_krush.core.util.registry.Registry;

import java.awt.*;

/**
 * A base abstract class from which all "entities," that is to say, "utilizes the entity component system" should extend.<br>
 * Holds {@link Registry Registries} of {@link Entity#components} and {@link Entity#systems} for use by the ECS.
 */
public abstract class Entity extends AbstractGameObject {
    /**
     * A {@link ValueComponent} representing the color tint of this entity.
     */
    public final ValueComponent<Color> colorModifier;

    /**
     * A {@link Registry} used to hold all of this entity's {@link AbstractComponent components}.
     */
    protected final Registry<AbstractComponent> components = new Registry<>();
    /**
     * A {@link Registry} used to hold all of this entity's {@link AbstractSystem systems}.
     */
    protected final Registry<AbstractSystem> systems = new Registry<>();

    /**
     * The amount of time this entity has existed for, in seconds.
     */
    protected double ticks = 0;

    /**
     * Constructs a new {@link Entity} with the provided spritesheet, which will be split into regions of the provided size, and initializes components/systems.
     * @param screen The {@link AbstractGameScreen} containing this entity.
     * @param width The width of the entity's image.
     * @param height The height of the entity's image.
     */
    public Entity(AbstractGameScreen screen, int width, int height) {
        super(screen);
        colorModifier = components.register(getId("color_modifier"), new ValueComponent<>(this, Color.WHITE));

        this.width = width;
        this.height = height;
    }

    @Override
    public void update(float deltaTime) {
        this.collisionRect.set(x, y, width * scale, height * scale);
        ticks += deltaTime;
        systems.forEachEntry((id, system) -> system.update(deltaTime));
    }

    @Override
    public void render(Batch batch, float deltaTime) {
        systems.forEachEntry((id, system) -> system.render(batch, deltaTime));
    }

    @Override
    public void remove() {
        super.remove();
        systems.forEachEntry((id, system) -> system.remove());
    }

    /**
     * A helper shorthand method that gets an ID with this entity's game's namespace.
     * @param path The path/name of the new identifier.
     * @return A new ID using the namespace of this entity's game.
     */
    public final Identifier getId(String path) {return screen.game.getId(path);}

    /**
     * Queries an {@link ImmutableRegistry} containing all of this entity's components.
     * @return An {@link ImmutableRegistry} of this entity's components.
     */
    public final ImmutableRegistry<AbstractComponent> queryComponents() {return ImmutableRegistry.of(components);}
    /**
     * Queries an {@link ImmutableRegistry} containing all of this entity's systems.
     * @return An {@link ImmutableRegistry} of this entity's systems.
     */
    public final ImmutableRegistry<AbstractSystem> querySystems() {return ImmutableRegistry.of(systems);}
}
