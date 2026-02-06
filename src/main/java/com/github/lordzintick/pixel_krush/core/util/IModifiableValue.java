package com.github.lordzintick.pixel_krush.core.util;

/**
 * An interface containing methods to get and set some arbitrary value.
 * @param <T> The type of value.
 */
public interface IModifiableValue<T> {
    /**
     * Gets the value.
     * @return The value.
     */
    T get();

    /**
     * Sets the value to the provided value.
     * @param value The new value to set the current value to.
     */
    void set(T value);
}
