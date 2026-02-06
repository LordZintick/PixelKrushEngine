package com.github.lordzintick.pixel_krush.core.api.ecs.comp;

import com.github.lordzintick.pixel_krush.core.api.ecs.AbstractEntityAttachment;
import com.github.lordzintick.pixel_krush.core.api.ecs.Entity;

/**
 * A base categorization class for all components for {@link Entity Entities}.
 */
public abstract class AbstractComponent extends AbstractEntityAttachment {
    /**
     * Constructs a new component attached to the specified entity.
     * @param parent The parent {@link Entity} of this component
     */
    public AbstractComponent(Entity parent) {
        super(parent);
    }
}
