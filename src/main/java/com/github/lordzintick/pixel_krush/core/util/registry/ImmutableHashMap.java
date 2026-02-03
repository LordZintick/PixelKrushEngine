package com.github.lordzintick.pixel_krush.core.util.registry;

import java.util.HashMap;

public class ImmutableHashMap<K, V> {
    private final HashMap<K, V> map = new HashMap<>();

    private ImmutableHashMap() {}
    public static <K, V> ImmutableHashMap<K, V> of(HashMap<K, V> map) {
        ImmutableHashMap<K, V> immutable = new ImmutableHashMap<>();
        immutable.map.putAll(map);
        return immutable;
    }

    public V get(K key) {return map.get(key);}
}
