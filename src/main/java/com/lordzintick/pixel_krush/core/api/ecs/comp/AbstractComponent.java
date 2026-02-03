package com.lordzintick.pixel_krush.core.api.ecs.comp;

import com.lordzintick.pixel_krush.core.api.ecs.AbstractEntityAttachment;
import com.lordzintick.pixel_krush.core.api.ecs.Entity;

public abstract class AbstractComponent extends AbstractEntityAttachment {
    public AbstractComponent(Entity parent) {
        super(parent);
    }
}
