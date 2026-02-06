package com.github.lordzintick.pixel_krush.core.util.registry;

import com.github.lordzintick.pixel_krush.core.util.Identifier;
import com.github.lordzintick.pixel_krush.core.util.Pair;

import java.util.ArrayList;

/**
 * A utility extension of {@link Registry} that stores all object registered in a {@link #queue} for future ("lazy") addition with the {@link #flush()} method.<br>
 * Also see {@link LazyList}.
 * @param <T> The type of object to register.
 */
public class LazyRegistry<T> extends Registry<T> {
    /**
     * The queue of objects to register.
     */
    private final ArrayList<Pair<Identifier, T>> queue = new ArrayList<>();

    @Override
    public <R extends T> R register(Identifier id, R value) {
        queue.add(new Pair<>(id, value));
        return value;
    }

    /**
     * Registers all queued objects and clears the queue.
     */
    public void flush() {
        for (Pair<Identifier, T> pair : queue) {
            map.put(pair.first(), pair.second());
        }

        queue.clear();
    }
}
