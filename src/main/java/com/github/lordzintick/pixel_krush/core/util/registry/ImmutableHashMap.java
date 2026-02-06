package com.github.lordzintick.pixel_krush.core.util.registry;

import java.util.HashMap;

/**
 * A utility wrapper class around a {@link HashMap} that prevents further modification.
 * @param <K> The type of key to use.
 * @param <V> The type of value to use.
 */
public class ImmutableHashMap<K, V> {
    /**
     * The internal map used by this {@link ImmutableHashMap}.
     */
    private final HashMap<K, V> map = new HashMap<>();

    /**
     * Constructs a new, blank {@link ImmutableHashMap}.
     */
    private ImmutableHashMap() {}

    /**
     * Creates a new {@link ImmutableHashMap} from the provided map.
     * @param map The map to copy.
     * @param <K> The type of key to use.
     * @param <V> The type of value to use.
     * @return A new {@link ImmutableHashMap} with the same values as the provided map.
     */
    public static <K, V> ImmutableHashMap<K, V> of(HashMap<K, V> map) {
        ImmutableHashMap<K, V> immutable = new ImmutableHashMap<>();
        immutable.map.putAll(map);
        return immutable;
    }

    /**
     * Gets the value from the map associated with the provided key.
     * @param key The key to get the associated value with.
     * @return The value associated with the provided key.
     */
    public V get(K key) {return map.get(key);}
}
