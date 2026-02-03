package com.lordzintick.pixel_krush.core.util;

public abstract class AbstractModifiableValue<T> implements IModifiableValue<T> {
    public AbstractModifiableValue(T value) {
        set(value);
    }
    public AbstractModifiableValue() {}
}
