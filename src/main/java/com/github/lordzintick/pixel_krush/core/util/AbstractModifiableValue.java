package com.github.lordzintick.pixel_krush.core.util;

/**
 * An abstraction of the {@link IModifiableValue} interface, with constructors for blank and initial values.
 * @param <T> The type of the value.
 */
public abstract class AbstractModifiableValue<T> implements IModifiableValue<T> {
    /**
     * Constructs a new {@link AbstractModifiableValue} with the specified initial value.
     * @param value The initial value.
     */
    public AbstractModifiableValue(T value) {
        set(value);
    }

    /**
     * Constructs a new {@link AbstractModifiableValue} with an initial value of <code>null</code>.
     */
    public AbstractModifiableValue() {}
}
