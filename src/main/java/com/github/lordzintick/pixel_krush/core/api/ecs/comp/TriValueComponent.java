package com.github.lordzintick.pixel_krush.core.api.ecs.comp;

import com.github.lordzintick.pixel_krush.core.api.ecs.Entity;

/**
 * A utility component consisting of three separate values, which can each have different types.
 * @param <T> The type of the first value
 * @param <R> The type of the second value
 * @param <E> The type of the third value
 */
public class TriValueComponent<T, R, E> extends AbstractComponent {
    /**
     * A <code>protected</code> field used for holding the first value in the component.<br>
     * Cannot be directly queried or changed except by inheritors.<br>
     * Use {@link TriValueComponent#get()} and {@link TriValueComponent#set(T)} instead.
     */
    protected T value;
    /**
     * A <code>protected</code> field used for holding the second value in the component.<br>
     * Cannot be directly queried or changed except by inheritors.<br>
     * Use {@link TriValueComponent#get2()} and {@link TriValueComponent#set2(R)} instead.
     */
    protected R value2;
    /**
     * A <code>protected</code> field used for holding the third value in the component.<br>
     * Cannot be directly queried or changed except by inheritors.<br>
     * Use {@link TriValueComponent#get3()} and {@link TriValueComponent#set3(E)} instead.
     */
    protected E value3;

    /**
     * Constructs a new {@link TriValueComponent} with the provided initial values.
     * @param parent The parent {@link Entity} of the component.
     * @param value The initial first value.
     * @param value2 The initial second value.
     * @param value3 The initial third value.
     */
    public TriValueComponent(Entity parent, T value, R value2, E value3) {
        super(parent);
        this.value = value;
        this.value2 = value2;
        this.value3 = value3;
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
    /**
     * Gets the current value of the third value in this component.
     * @return The current third value.
     */
    public E get3() {return value3;}
    /**
     * Sets the third value in this component to the provided value.
     * @param value The new value to set to.
     */
    public void set3(E value) {this.value3 = value;}
}
