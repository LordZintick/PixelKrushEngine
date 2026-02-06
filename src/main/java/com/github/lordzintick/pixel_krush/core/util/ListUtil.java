package com.github.lordzintick.pixel_krush.core.util;

import java.util.*;

/**
 * A utility class providing list-related utility methods.
 */
public class ListUtil {
    /**
     * Groups a provided vararg of elements into a list.
     * @param elements A vararg of elements to group into a list.
     * @param <T> The type of element to group.
     * @return A list containing the provided elements.
     */
    @SafeVarargs
    public static <T> List<T> listOf(T... elements) {
        return new ArrayList<>(Arrays.asList(elements));
    }

    /**
     * Gets the key associated with a certain value in a map.
     * @param map The map to get the key from the provided value of.
     * @param value The value to get the associated key.
     * @param <K> The type of key.
     * @param <V> The type of value.
     * @return The key associated with the provided value in the provided map.
     */
    public static <K, V> K getKey(Map<K, V> map, V value) {
        ArrayList<K> keys = new ArrayList<>(map.keySet());
        ArrayList<V> values = new ArrayList<>(map.values());

        return keys.get(values.indexOf(value));
    }

    /**
     * Gets a random value from the provided values.
     * @param random The random number generator to use.
     * @param values The vararg of values to use.
     * @param <T> The type of value.
     * @return A random value from the vararg of values.
     */
    @SafeVarargs
    public static <T> T choose(Random random, T... values) {
        return values[random.nextInt(values.length)];
    }
}
