package com.lordzintick.pixel_krush.core.api.ecs.comp;

import com.lordzintick.pixel_krush.core.api.ecs.Entity;

public class BiValueComponent<T, R> extends AbstractComponent {
    protected T value;
    protected R value2;

    public BiValueComponent(Entity parent, T value, R value2) {
        super(parent);
        this.value = value;
        this.value2 = value2;
    }

    public T get() {return value;}
    public void set(T value) {this.value = value;}
    public R get2() {return value2;}
    public void set2(R value2) {this.value2 = value2;}
}
