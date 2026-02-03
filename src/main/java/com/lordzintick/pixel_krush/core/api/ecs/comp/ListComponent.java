package com.lordzintick.pixel_krush.core.api.ecs.comp;

import com.lordzintick.pixel_krush.core.api.ecs.Entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ListComponent<T> extends ValueComponent<List<T>> {
    public ListComponent(Entity parent, List<T> value) {
        super(parent, value);
    }
    public ListComponent(Entity parent) {
        super(parent);
        this.value = new ArrayList<>();
    }

    public int size() {return this.value.size();}
    public void add(T value) {this.value.add(value);}
    public void set(int index, T value) {this.value.set(index, value);}
    public T get(int index) {return this.value.get(index);}
    public void remove(T value) {this.value.remove(value);}
    public void clear() {this.value.clear();}
    public void addAll(Collection<? extends T> collection) {this.value.addAll(collection);}

    @Override
    public void set(List<T> value) {
        throw new IllegalStateException("Cannot set a list component. Use \"add\" and \"remove\" instead");
    }
}
