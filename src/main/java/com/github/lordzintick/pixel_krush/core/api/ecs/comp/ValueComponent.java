package com.github.lordzintick.pixel_krush.core.api.ecs.comp;

import com.github.lordzintick.pixel_krush.core.api.ecs.Entity;

/**
 * A utility component used for simply storing a value of a given type.<br>
 * See {@link BiValueComponent} and {@link TriValueComponent} for components that hold multiple values at once.
 * @param <T> The type of the value in this component.
 */
public class ValueComponent<T> extends AbstractComponent {
    /**
     * A <code>protected</code> field used for holding the value in the component.<br>
     * Cannot be directly queried or changed except by inheritors.<br>
     * Use {@link ValueComponent#get()} and {@link ValueComponent#set(T)} instead.
     */
    protected T value = null;

    /**
     * Constructs a new {@link ValueComponent} with the provided initial value.
     * @param parent The parent {@link Entity} of this component.
     * @param value The initial value of the component.
     */
    public ValueComponent(Entity parent, T value) {
        super(parent);
        this.value = value;
    }

    /**
     * Constructs a new {@link ValueComponent} with an initial value of <code>null</code>.
     * @param parent The parent {@link Entity} of this component.
     */
    public ValueComponent(Entity parent) {
        super(parent);
    }

    /**
     * Gets the current value of the value in this component.
     * @return The current value.
     */
    public T get() {return value;}
    /**
     * Sets the value in this component to the provided value.
     * @param value The new value to set to.
     */
    public void set(T value) {this.value = value;}
}
