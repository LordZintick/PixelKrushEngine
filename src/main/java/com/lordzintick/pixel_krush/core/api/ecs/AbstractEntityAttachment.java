package com.lordzintick.pixel_krush.core.api.ecs;

public abstract class AbstractEntityAttachment {
    public final Entity parent;

    protected AbstractEntityAttachment(Entity parent) {
        this.parent = parent;
    }
}
