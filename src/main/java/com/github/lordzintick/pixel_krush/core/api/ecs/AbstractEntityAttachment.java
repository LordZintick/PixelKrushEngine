package com.github.lordzintick.pixel_krush.core.api.ecs;

/**
 * An abstract class that simply stores a {@link AbstractEntityAttachment#parent} value representing the parent {@link Entity} of the class.
 */
public abstract class AbstractEntityAttachment {
    /**
     * The parent {@link Entity} of the entity attachment.
     */
    public final Entity parent;

    /**
     * Constructs a new {@link AbstractEntityAttachment} with the provided parent {@link Entity}.
     * @param parent The parent {@link Entity} of the entity attachment.
     */
    protected AbstractEntityAttachment(Entity parent) {
        this.parent = parent;
    }
}
