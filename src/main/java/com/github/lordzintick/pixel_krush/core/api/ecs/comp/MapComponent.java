package com.github.lordzintick.pixel_krush.core.api.ecs.comp;

import com.github.lordzintick.pixel_krush.core.api.ecs.Entity;

import java.util.HashMap;

public class MapComponent<K, V> extends ValueComponent<HashMap<K, V>> {
    public MapComponent(Entity parent) {
        super(parent);
        this.value = new HashMap<>();
    }

    public V put(K key, V value) {return this.value.put(key, value);}
    public V get(K key) {return this.value.get(key);}
    public void remove(K key) {this.value.remove(key);}
    public boolean containsKey(K key) {return this.value.containsKey(key);}

    @Override
    public void set(HashMap<K, V> value) {
        throw new IllegalStateException("Cannot set a map component. Use \"put\" and \"remove\" instead");
    }
}
