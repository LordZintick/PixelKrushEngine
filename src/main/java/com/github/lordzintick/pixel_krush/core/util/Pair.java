package com.github.lordzintick.pixel_krush.core.util;

import com.badlogic.gdx.utils.OrderedMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A utility class storing two values, {@link #first} and {@link #second}.
 * @param <F> The type of the first value.
 * @param <S> The type of the second value.
 */
public final class Pair<F, S> {
    /**
     * The first value.
     */
    private final F first;
    /**
     * The second value.
     */
    private final S second;

    /**
     * Constructs a new {@link Pair} with the provided initial first and second values.
     * @param first The initial first value.
     * @param second The initial second value.
     */
    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    /**
     * Gets the first value.
     * @return The first value.
     */
    public F first() {return first;}
    /**
     * Gets the second value.
     * @return The second value.
     */
    public S second() {return second;}

    /**
     * Transforms a map into a {@link List} of {@link Pair}s.
     * @param map The map to transform.
     * @param <F> The type of the first value/key.
     * @param <S> The type of the second value/value.
     * @return A list of {@link Pair}s corresponding to entries in the provided map.
     */
    public static <F, S> List<Pair<F, S>> transformMap(Map<F, S> map) {
        ArrayList<Pair<F, S>> pairList = new ArrayList<>();
        for (F key : map.keySet()) {
            pairList.add(new Pair<>(key, map.get(key)));
        }
        return pairList;
    }

    /**
     * Transforms an ordered map into a {@link List} of {@link Pair}s.
     * @param map The ordered map to transform.
     * @param <F> The type of the first value/key.
     * @param <S> The type of the second value/value.
     * @return A list of {@link Pair}s corresponding to entries in the provided ordered map.
     */
    public static <F, S> List<Pair<F, S>> transformOrderedMap(OrderedMap<F, S> map) {
        ArrayList<Pair<F, S>> pairList = new ArrayList<>();
        for (F key : map.keys()) {
            pairList.add(new Pair<>(key, map.get(key)));
        }
        return pairList;
    }
}
