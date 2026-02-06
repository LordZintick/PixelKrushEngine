package com.github.lordzintick.pixel_krush.core.api.ecs.sys;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.github.lordzintick.pixel_krush.core.api.ecs.Entity;
import com.github.lordzintick.pixel_krush.core.api.ecs.comp.ValueComponent;

/**
 * An abstract system that stores a color modifier component and exposes an abstract {@link ColorModifierSystem#renderColored(Batch, float)} method to render textures with the parent {@link Entity}'s modifier color.
 */
public abstract class ColorModifierSystem extends AbstractSystem {
    /**
     * A {@link ValueComponent} storing the color modifier of the parent {@link Entity}.
     */
    private final ValueComponent<Color> colorModifier;

    /**
     * Constructs a new {@link ColorModifierSystem} and initializes dependencies.
     * @param parent The parent {@link Entity} of the system.
     */
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

    /**
     * Called when the system should render textures that it wishes to be rendered with the parent {@link Entity}'s modifier color.
     * @param batch The {@link Batch} to render with.
     * @param deltaTime The time since the last frame was rendered.
     */
    protected abstract void renderColored(Batch batch, float deltaTime);
}
