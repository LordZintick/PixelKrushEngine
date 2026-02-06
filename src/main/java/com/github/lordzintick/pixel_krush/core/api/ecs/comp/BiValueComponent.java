package com.github.lordzintick.pixel_krush.core.api.ecs.comp;

import com.github.lordzintick.pixel_krush.core.api.ecs.Entity;

/**
 * A simple utility component that simply stores two separate values that can have differing types.<br>
 * Provides getters and setters for the values.
 * @param <T> The type of the first value.
 * @param <R> The type of the second value.
 */
public class BiValueComponent<T, R> extends AbstractComponent {
    /**
     * A <code>protected</code> field used for holding the first value in the component.<br>
     * Cannot be directly queried or changed except by inheritors.<br>
     * Use {@link BiValueComponent#get()} and {@link BiValueComponent#set(T)} instead.
     */
    protected T value;
    /**
     * A <code>protected</code> field used for holding the second value in the component.<br>
     * Cannot be directly queried or changed except by inheritors.<br>
     * Use {@link BiValueComponent#get2()} and {@link BiValueComponent#set2(T)} instead.
     */
    protected R value2;

    /**
     * Constructs a new {@link BiValueComponent} with the provided default values and parent {@link Entity}.
     * @param parent The parent {@link Entity} of this component.
     * @param value The default value for the first value.
     * @param value2 The default value for the second value.
     */
    public BiValueComponent(Entity parent, T value, R value2) {
        super(parent);
        this.value = value;
        this.value2 = value2;
    }

    /**
     * Gets the current value of the first value in this component.
     * @return The current first value.
     */
    public T get() {return value;}
    /**
     * Sets the first value in this component to the provided value.
     * @param value The new value to set to.
     */
    public void set(T value) {this.value = value;}

    /**
     * Gets the current value of the second value in this component.
     * @return The current second value.
     */
    public R get2() {return value2;}
    /**
     * Sets the second value in this component to the provided value.
     * @param value The new value to set to.
     */
    public void set2(R value) {this.value2 = value;}
}
