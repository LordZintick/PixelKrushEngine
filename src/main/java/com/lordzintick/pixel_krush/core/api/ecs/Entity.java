package com.lordzintick.pixel_krush.core.api.ecs;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.lordzintick.pixel_krush.core.api.ecs.comp.AbstractComponent;
import com.lordzintick.pixel_krush.core.api.ecs.comp.ValueComponent;
import com.lordzintick.pixel_krush.core.api.ecs.sys.AbstractSystem;
import com.lordzintick.pixel_krush.core.api.ecs.sys.ColorModifierSystem;
import com.lordzintick.pixel_krush.core.util.Identifier;
import com.lordzintick.pixel_krush.core.util.Logger;
import com.lordzintick.pixel_krush.core.api.AbstractGameObject;
import com.lordzintick.pixel_krush.core.api.AbstractGameScreen;
import com.lordzintick.pixel_krush.core.util.registry.ImmutableRegistry;
import com.lordzintick.pixel_krush.core.util.registry.Registry;

import java.awt.*;

/**
 * A base abstract class from which all moving and living entities should extend from
 */
public abstract class Entity extends AbstractGameObject {
    public final ValueComponent<Color> colorModifier;
    protected final Logger LOGGER = new Logger(this.getClass());

    protected final Registry<AbstractComponent> components = new Registry<>();
    protected final Registry<AbstractSystem> systems = new Registry<>();

    protected double ticks = 0;

    /**
     * Constructs a new {@link Entity} with the provided spritesheet, which will be split into regions of the provided size
     * @param screen The {@link AbstractGameScreen} containing this entity
     * @param width The width of the entity's damage
     * @param height The height of the entity's image
     */
    public Entity(AbstractGameScreen screen, int width, int height) {
        super(screen);
        colorModifier = components.register(getId("color_modifier"), new ValueComponent<>(this, Color.WHITE));
        systems.register(getId("color_modifier"), new ColorModifierSystem(this));

        this.width = width;
        this.height = height;
    }

    public void onDeath() {
        remove();
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

    public final Identifier getId(String path) {return screen.game.getId(path);}
    public final ImmutableRegistry<AbstractComponent> queryComponents() {return ImmutableRegistry.of(components);}
    public final ImmutableRegistry<AbstractSystem> querySystems() {return ImmutableRegistry.of(systems);}
}
