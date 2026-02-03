package com.lordzintick.pixel_krush.core.util.registry;

import com.lordzintick.pixel_krush.core.util.Identifier;
import com.lordzintick.pixel_krush.core.util.Pair;

import java.util.Collection;

public interface IRegisterableList<T> {
    Collection<Pair<Identifier, T>> pairCollection();
}
