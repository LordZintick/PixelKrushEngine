package com.github.lordzintick.pixel_krush.core.util.registry;

import com.github.lordzintick.pixel_krush.core.util.Identifier;
import com.github.lordzintick.pixel_krush.core.util.Pair;

import java.util.Collection;

/**
 * An interface providing a method to get a collection of <code>Pair{@literal <Identifier, T>}</code>s.
 * @param <T> The type of object to register.
 */
public interface IRegisterableList<T> {
    /**
     * Gets a collection of <code>Pair{@literal <Identifier, T>}</code>s.
     * @return A collection of <code>Pair{@literal <Identifier, T>}</code>s.
     */
    Collection<Pair<Identifier, T>> pairCollection();
}
