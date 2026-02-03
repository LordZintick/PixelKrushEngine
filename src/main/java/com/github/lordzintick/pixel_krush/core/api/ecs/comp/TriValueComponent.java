package com.github.lordzintick.pixel_krush.core.api.ecs.comp;

import com.github.lordzintick.pixel_krush.core.api.ecs.Entity;

public class TriValueComponent<T, R, E> extends AbstractComponent {
    protected T value;
    protected R value2;
    protected E value3;

    public TriValueComponent(Entity parent, T value, R value2, E value3) {
        super(parent);
        this.value = value;
        this.value2 = value2;
        this.value3 = value3;
    }

    public T get() {return value;}
    public void set(T value) {this.value = value;}
    public R get2() {return value2;}
    public void set2(R value2) {this.value2 = value2;}
    public E get3() {return value3;}
    public void set3(E value3) {this.value3 = value3;}
}
