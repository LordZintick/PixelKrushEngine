package com.github.lordzintick.pixel_krush.core.util;

public class ModifiableValueImpl<T> extends AbstractModifiableValue<T> {
    private T value;

    public ModifiableValueImpl(T value) {
        this.value = value;
    }

    @Override
    public T get() {
        return value;
    }

    @Override
    public void set(T value) {
        this.value = value;
    }
}
