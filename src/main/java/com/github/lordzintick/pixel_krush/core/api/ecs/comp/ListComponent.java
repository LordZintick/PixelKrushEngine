package com.github.lordzintick.pixel_krush.core.api.ecs.comp;

import com.github.lordzintick.pixel_krush.core.api.ecs.Entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A small utility {@link ValueComponent} implementation used for storing a list of objects.
 * @param <T> The type of object to store in the list.
 */
public class ListComponent<T> extends ValueComponent<List<T>> {
    /**
     * Constructs a new {@link ListComponent} with the provided list as its value.
     * @param parent The parent {@link Entity} of the component.
     * @param value The initial value of the component.
     */
    public ListComponent(Entity parent, List<T> value) {
        super(parent, value);
    }

    /**
     * Constructs a new {@link ListComponent} and sets its value to a new {@link ArrayList}.
     * @param parent The parent {@link Entity} of the component.
     */
    public ListComponent(Entity parent) {
        super(parent);
        this.value = new ArrayList<>();
    }

    /**
     * Gets the size of the list stored within the {@link ListComponent}.
     * @return The size of the list as an integer.
     */
    public int size() {return this.value.size();}

    /**
     * Adds the specified value to the list.
     * @param value The value to add to the list.
     */
    public void add(T value) {this.value.add(value);}

    /**
     * Sets a certain value at the specified index to the provided value.
     * @param index The index at which to replace the previous value with the newly provided one.
     * @param value The value to replace the object at the specified index with.
     */
    public void set(int index, T value) {this.value.set(index, value);}

    /**
     * Gets a value from the list at the specified index.
     * @param index The index from which to get the value stored within.
     * @return The object at the specified index stored inside the list.
     */
    public T get(int index) {return this.value.get(index);}

    /**
     * Removes a certain value from the list.
     * @param value The value to remove from the list.
     */
    public void remove(T value) {this.value.remove(value);}

    /**
     * Clears the list of all its stored objects.
     */
    public void clear() {this.value.clear();}

    /**
     * Adds all the objects from the specified collection to the list.
     * @param collection The collection to add all the objects from to the list.
     */
    public void addAll(Collection<? extends T> collection) {this.value.addAll(collection);}

    @Override
    public void set(List<T> value) {
        throw new IllegalStateException("Cannot set a list component. Use \"add\" and \"remove\" instead");
    }
}
