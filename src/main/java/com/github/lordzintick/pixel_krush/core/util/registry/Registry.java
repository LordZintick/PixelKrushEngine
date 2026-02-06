package com.github.lordzintick.pixel_krush.core.util.registry;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.OrderedMap;
import com.github.lordzintick.pixel_krush.core.util.Identifier;
import com.github.lordzintick.pixel_krush.core.util.IdentifierNotFoundException;
import com.github.lordzintick.pixel_krush.core.util.Nullable;
import com.github.lordzintick.pixel_krush.core.util.Pair;

import java.util.*;
import java.util.function.BiConsumer;

/**
 * A utility class that stores an {@link OrderedMap} of {@link Identifier}s and <code>{@literal <T>}</code>s.<br>
 * Each identifier can only be registered once, and there are many utility methods to get objects or IDs with different fallbacks.
 * @param <T> The type of object to register.
 */
public class Registry<T> implements IRegisterableList<T> {
    /**
     * The {@link OrderedMap} at the core of all {@link Registry Registries}.
     */
    protected final OrderedMap<Identifier, T> map = new OrderedMap<>();

    /**
     * Registers a value with the specified ID to the registry.
     * @param id The ID to register the object with.
     * @param value The value to register.
     * @param <R> The type of object to register.
     * @return The object registered.
     */
    public <R extends T> R register(Identifier id, R value) {
        if (containsID(id))
            throw new IllegalRegistrationException("Identifier " + id + " is already registered to another object!");

        map.put(id, value);
        return value;
    }

    /**
     * Overrides an already-registered object with a new one.
     * @param id The ID to override the already-registered object with the new one.
     * @param value The object to override the old object with the specified ID with.
     * @param <R> The type of object to override.
     * @return The object overridden.
     */
    public <R extends T> R override(Identifier id, R value) {
        if (!containsID(id))
            throw new IllegalRegistrationException("Cannot override a registry object that does not exist");

        map.put(id, value);
        return value;
    }

    /**
     * Registers all the {@link Pair}s in an {@link IRegisterableList} to this {@link Registry}.
     * @param registerable An {@link IRegisterableList} to register all of the {@link Pair}s of.
     */
    public void registerAll(IRegisterableList<T> registerable) {
        for (Pair<Identifier, T> pair : registerable.pairCollection()) {
            register(pair.first(), pair.second());
        }
    }

    /**
     * Gets a collection of all the registered values in this {@link Registry}.
     * @return An {@link ObjectMap.Values} containing all the registered values of this {@link Registry}.
     */
    public ObjectMap.Values<T> valueCollection() {
        return map.values();
    }

    /**
     * Gets a collection of all the registered IDs in this {@link Registry}.
     * @return An {@link Array} containing all the registered IDs of this {@link Registry}.
     */
    public Array<Identifier> idArray() {return map.orderedKeys();}

    /**
     * Gets a list of all the registered objects in this {@link Registry}.
     * @param <R> The type of object to list.
     * @return A list of all the registered objects in this {@link Registry}.
     */
    public <R extends T> List<R> list() {
        ArrayList<R> list = new ArrayList<>();
        map.forEach(entry -> list.add((R) entry.value));
        return list;
    }

    /**
     * Gets an object registered with the specified ID.
     * @param id The ID to get the registered object of.
     * @param <R> The type of object to get.
     * @return An {@link Optional} containing the found object, or empty if none was found.
     */
    public <R extends T> Optional<R> get(Identifier id) {
        return containsID(id) ? Optional.ofNullable(getOrNull(id)) : Optional.empty();
    }

    /**
     * Gets an ID registered with the specified object.
     * @param value The object to get the registered ID of.
     * @return An {@link Optional} containing the found ID, or empty if none was found.
     */
    public Optional<Identifier> getId(T value) {
        return map.containsValue(value, false) ? Optional.ofNullable(getIdOrNull(value)) : Optional.empty();
    }

    /**
     * Gets an object registered with the specified ID, and throws an exception if none was found.
     * @param id The ID to get the registered object of.
     * @param <R> The type of object to get.
     * @return The found object.
     */
    public <R extends T> R getOrThrow(Identifier id) {
        try {
            return getOrError(id);
        } catch (RegistryError e) {
            throw new IdentifierNotFoundException(id.toString());
        }
    }

    /**
     * Gets an ID registered with the specified object, and throws an exception if none was found.
     * @param value The object to get the registered ID of.
     * @return The found ID.
     */
    public Identifier getIdOrThrow(T value) {
        try {
            return getIdOrError(value);
        } catch (RegistryError e) {
            throw new IllegalArgumentException(value + " not present in registry");
        }
    }

    /**
     * Gets an object registered with the specified ID, and returns the object, or throws an error if none was found.
     * @param id The ID to get the registered object of.
     * @param <R> The type of object to get.
     * @return The found object.
     * @throws RegistryError If an object with the specified ID was not found.
     */
    public <R extends T> R getOrError(Identifier id) throws RegistryError {
        if (!containsID(id))
            throw new RegistryError("Identifier " + id + " not found");

        return getOrNull(id);
    }

    /**
     * Gets an ID registered with the specified object, and returns the ID, or throws an exception if none was found.
     * @param value The object to get the registered ID of.
     * @return The found ID.
     * @throws RegistryError If an ID with the specified object was not found.
     */
    public Identifier getIdOrError(T value) throws RegistryError {
        if (!map.containsValue(value, false))
            throw new RegistryError(value + " not present in registry");

        return getIdOrNull(value);
    }

    /**
     * Gets whether a specified ID exists within this registry.
     * @param id The ID to check the existence of.
     * @return Whether this registry contains an object registered with the specified ID.
     */
    public boolean containsID(Identifier id) {
        for (Identifier id1 : idArray()) {
            if (id1.toString().equals(id.toString())) {
                return true;
            }
        }

        return false;
    }

    /**
     * Runs a {@link BiConsumer} for each entry in the {@link Registry}.
     * @param entryConsumer A {@link BiConsumer} to run something for each entry, passing the id of the entry and the entry's value.
     */
    public void forEachEntry(BiConsumer<Identifier, T> entryConsumer) {
        map.forEach(entry -> entryConsumer.accept(entry.key, entry.value));
    }

    /**
     * Gets an object registered with the specified ID.
     * @param id The ID to get the registered object of.
     * @param <R> The type of object to get.
     * @return The found object, or null if none was found.
     */
    @Nullable
    public <R extends T> R getOrNull(Identifier id) {
        for (Identifier id1 : idArray()) {
            if (id1.toString().equals(id.toString())) {
                return (R) map.get(id1);
            }
        }

        return null;
    }

    /**
     * Gets an ID registered with the specified object.
     * @param value The object to get the registered ID of.
     * @param <R> The type of object to get.
     * @return The found ID, or null if none was found.
     */
    @Nullable
    public <R extends T> Identifier getIdOrNull(R value) {
        for (Identifier id : idArray()) {
            if (map.get(id).equals(value)) {
                return id;
            }
        }

        return null;
    }

    /**
     * Gets a collection of <code>Pair{@literal <Identifier, T>}</code>s representing each entry in this {@link Registry}.
     * @return A collection of <code>Pair{@literal <Identifier, T>}</code>s representing each entry in this {@link Registry}.
     */
    @Override
    public Collection<Pair<Identifier, T>> pairCollection() {
        return Pair.transformOrderedMap(map);
    }
}
