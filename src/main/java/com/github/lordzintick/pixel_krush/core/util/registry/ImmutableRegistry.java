package com.github.lordzintick.pixel_krush.core.util.registry;

import com.badlogic.gdx.utils.ObjectMap;
import com.github.lordzintick.pixel_krush.core.util.Identifier;

/**
 * A utility extension of {@link Registry} that prevents further modification.
 * @param <T> The type of object to register.
 */
public class ImmutableRegistry<T> extends Registry<T> {
    /**
     * Creates a new {@link ImmutableRegistry} from the provided registry.
     * @param registry The {@link Registry} to copy.
     * @param <T> The type of object to register.
     * @return A new {@link ImmutableRegistry} with the same values as the provided registry.
     */
    public static <T> ImmutableRegistry<T> of(Registry<T> registry) {
        ImmutableRegistry<T> immutable = new ImmutableRegistry<>();
        immutable.map.putAll((ObjectMap<? extends Identifier, ? extends T>) registry.map);
        return immutable;
    }

    @Override
    public <R extends T> R register(Identifier id, R value) {
        throw new IllegalRegistrationException("Cannot register to an immutable registry");
    }

    @Override
    public void registerAll(IRegisterableList<T> registerable) {
        throw new IllegalRegistrationException("Cannot register to an immutable registry");
    }
}
