package com.github.lordzintick.pixel_krush.core.api.ecs.comp;

import com.github.lordzintick.pixel_krush.core.api.ecs.Entity;

import java.util.HashMap;

/**
 * A simple utility {@link ValueComponent} implementation that stores a map of objects.
 * @param <K> The type of key to use for the map.
 * @param <V> The type of value to use for the map.
 */
public class MapComponent<K, V> extends ValueComponent<HashMap<K, V>> {
    /**
     * Constructs a new {@link MapComponent} and sets the value to a new {@link HashMap}.
     * @param parent The parent {@link Entity} of the component.
     */
    public MapComponent(Entity parent) {
        super(parent);
        this.value = new HashMap<>();
    }

    /**
     * Associates a certain key with a value in the map.
     * @param key The key with which the specified value is to be associated.
     * @param value The value with which the specified key is to be associated.
     * @return The previous value associated with the <code>key</code>, or <code>null</code> if there was no mapping for <code>key</code>.
     */
    public V put(K key, V value) {return this.value.put(key, value);}

    /**
     * Gets the value associated with the specified key.
     * @param key The key to get the associated value of.
     * @return The value associated to the <code>key</code>.
     */
    public V get(K key) {return this.value.get(key);}

    /**
     * Removes a mapping with a certain key from the map.
     * @param key The key to remove from the map.
     */
    public void remove(K key) {this.value.remove(key);}

    /**
     * Gets whether the map contains a mapping for the specified key.
     * @param key The key to query the existence of.
     * @return Whether the map contains a mapping for the specified key.
     */
    public boolean containsKey(K key) {return this.value.containsKey(key);}

    @Override
    public void set(HashMap<K, V> value) {
        throw new IllegalStateException("Cannot set a map component. Use \"put\" and \"remove\" instead");
    }
}
