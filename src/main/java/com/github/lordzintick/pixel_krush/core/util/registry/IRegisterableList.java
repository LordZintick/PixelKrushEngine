package com.github.lordzintick.pixel_krush.core.util.registry;

import com.github.lordzintick.pixel_krush.core.util.Identifier;
import com.github.lordzintick.pixel_krush.core.util.Pair;

import java.util.Collection;

public interface IRegisterableList<T> {
    Collection<Pair<Identifier, T>> pairCollection();
}
