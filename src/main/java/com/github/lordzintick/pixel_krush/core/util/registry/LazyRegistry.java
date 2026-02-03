package com.github.lordzintick.pixel_krush.core.util.registry;

import com.github.lordzintick.pixel_krush.core.util.Identifier;
import com.github.lordzintick.pixel_krush.core.util.Pair;

import java.util.ArrayList;

public class LazyRegistry<T> extends Registry<T> {
    private final ArrayList<Pair<Identifier, T>> queue = new ArrayList<>();

    @Override
    public <R extends T> R register(Identifier id, R value) {
        queue.add(new Pair<>(id, value));
        return value;
    }

    public void flush() {
        for (Pair<Identifier, T> pair : queue) {
            map.put(pair.first(), pair.second());
        }

        queue.clear();
    }
}
