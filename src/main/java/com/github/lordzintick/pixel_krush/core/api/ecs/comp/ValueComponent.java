package com.github.lordzintick.pixel_krush.core.api.ecs.comp;

import com.github.lordzintick.pixel_krush.core.api.ecs.Entity;

public class ValueComponent<T> extends AbstractComponent {
    protected T value = null;

    public ValueComponent(Entity parent, T value) {
        super(parent);
        this.value = value;
    }
    public ValueComponent(Entity parent) {
        super(parent);
    }

    public T get() {return value;}
    public void set(T value) {this.value = value;}
}
