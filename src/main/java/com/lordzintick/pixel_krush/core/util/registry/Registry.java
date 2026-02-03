package com.lordzintick.pixel_krush.core.util.registry;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.OrderedMap;
import com.lordzintick.pixel_krush.core.util.Identifier;
import com.lordzintick.pixel_krush.core.util.IdentifierNotFoundException;
import com.lordzintick.pixel_krush.core.util.Nullable;
import com.lordzintick.pixel_krush.core.util.Pair;

import java.util.*;
import java.util.function.BiConsumer;

public class Registry<T> implements IRegisterableList<T> {
    protected final OrderedMap<Identifier, T> map = new OrderedMap<>();

    public <R extends T> R register(Identifier id, R value) {
        if (containsID(id))
            throw new IllegalRegistrationException("Identifier " + id + " is already registered to another object!");

        map.put(id, value);
        return value;
    }

    public <R extends T> R override(Identifier id, R value) {
        if (!containsID(id))
            throw new IllegalRegistrationException("Cannot override a registry object that does not exist");

        map.put(id, value);
        return value;
    }

    public void registerAll(IRegisterableList<T> registerable) {
        for (Pair<Identifier, T> pair : registerable.pairCollection()) {
            register(pair.first(), pair.second());
        }
    }

    public ObjectMap.Values<T> valueCollection() {
        return map.values();
    }
    public Array<Identifier> idArray() {return map.orderedKeys();}
    public <R extends T> List<R> list() {
        ArrayList<R> list = new ArrayList<>();
        map.forEach(entry -> list.add((R) entry.value));
        return list;
    }

    public <R extends T> Optional<R> get(Identifier id) {
        return containsID(id) ? Optional.ofNullable(getOrNull(id)) : Optional.empty();
    }

    public Optional<Identifier> getId(T value) {
        return map.containsValue(value, false) ? Optional.ofNullable(getIdOrNull(value)) : Optional.empty();
    }

    public <R extends T> R getOrThrow(Identifier id) {
        try {
            return getOrError(id);
        } catch (RegistryError e) {
            throw new IdentifierNotFoundException(id.toString());
        }
    }

    public Identifier getIdOrThrow(T value) {
        try {
            return getIdOrError(value);
        } catch (RegistryError e) {
            throw new IllegalArgumentException(value + " not present in registry");
        }
    }

    public <R extends T> R getOrError(Identifier id) throws RegistryError {
        if (!containsID(id))
            throw new RegistryError("Identifier " + id + " not found");

        return getOrNull(id);
    }

    public Identifier getIdOrError(T value) throws RegistryError {
        if (!map.containsValue(value, false))
            throw new RegistryError(value + " not present in registry");

        return getIdOrNull(value);
    }

    public boolean containsID(Identifier id) {
        for (Identifier id1 : idArray()) {
            if (id1.toString().equals(id.toString())) {
                return true;
            }
        }

        return false;
    }

    public void forEachEntry(BiConsumer<Identifier, T> entryConsumer) {
        map.forEach(entry -> entryConsumer.accept(entry.key, entry.value));
    }

    @Nullable
    public <R extends T> R getOrNull(Identifier id) {
        for (Identifier id1 : idArray()) {
            if (id1.toString().equals(id.toString())) {
                return (R) map.get(id1);
            }
        }

        return null;
    }

    @Nullable
    public <R extends T> Identifier getIdOrNull(R value) {
        for (Identifier id : idArray()) {
            if (map.get(id).equals(value)) {
                return id;
            }
        }

        return null;
    }

    @Override
    public Collection<Pair<Identifier, T>> pairCollection() {
        return Pair.transformOrderedMap(map);
    }
}
