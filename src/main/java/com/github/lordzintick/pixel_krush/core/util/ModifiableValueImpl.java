package com.github.lordzintick.pixel_krush.core.util;

/**
 * An implementation of the {@link IModifiableValue} interface and an extension of {@link AbstractModifiableValue}.
 * @param <T> The type of value.
 */
public class ModifiableValueImpl<T> extends AbstractModifiableValue<T> {
    /**
     * The value.
     */
    private T value;

    /**
     * Constructs a new {@link ModifiableValueImpl} with the provided initial value.
     * @param value The initial value.
     */
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
