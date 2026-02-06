package com.github.lordzintick.pixel_krush.core.util.registry;

import com.github.lordzintick.pixel_krush.core.api.AbstractGame;
import com.github.lordzintick.pixel_krush.core.util.Identifier;

/**
 * An extension of {@link Registry} used for easy registering to {@link Registry Registries}.<br>
 * A {@link DeferredRegister} stores a {@link DeferredRegister#namespace} and a target {@link DeferredRegister#registryId}, and registers its own objects to the target registry when its {@link DeferredRegister#register(AbstractGame)} method is called.<br>
 * Use the {@link DeferredRegister#create(AbstractGame, Identifier)} method to create a new {@link DeferredRegister} to register to the provided {@link Registry}.
 * @param <T> The type of object to register.
 */
public class DeferredRegister<T> extends Registry<T> {
    /**
     * The {@link Identifier} of the {@link Registry} this {@link DeferredRegister} is targeting for registration.
     */
    private final Identifier registryId;
    /**
     * The namespace for this {@link DeferredRegister} to register objects under.
     */
    private final String namespace;

    /**
     * Constructs a new {@link DeferredRegister} targeting the specified {@link Registry} and using the specified namespace.
     * @param registryId The {@link Identifier} of the target {@link Registry} to register to.
     * @param namespace The namespace to register objects under.
     */
    private DeferredRegister(Identifier registryId, String namespace) {
        this.registryId = registryId;
        this.namespace = namespace;
    }

    /**
     * Gets the target registry ID of this {@link DeferredRegister}.
     * @return The {@link Identifier} of the target {@link Registry} this {@link DeferredRegister} is to be registering to.
     */
    public Identifier getRegistryId() {return registryId;}

    /**
     * Creates a new {@link DeferredRegister} with the specified target registry and the same {@link AbstractGame#getNamespace() namespace} as the provided {@link AbstractGame}.
     * @param game The {@link AbstractGame Game} to register objects under.
     * @param registryId The {@link Identifier} of the target registry to register to.
     * @param <R> The type of registry.
     * @return A new {@link DeferredRegister} with the provided <code>registryId</code> and the game's namespace.
     */
    public static <R> DeferredRegister<R> create(AbstractGame game, Identifier registryId) {
        return new DeferredRegister<>(registryId, game.getNamespace());
    }

    @Override
    public <R extends T> R register(Identifier id, R value) {
        return register(id.getPath(), value);
    }

    /**
     * Registers an object to the {@link DeferredRegister} with the provided name, using the {@link DeferredRegister}'s built-in {@link DeferredRegister#namespace}.
     * @param name The name to register the object with.
     * @param value The object to register.
     * @param <R> The type of object to register.
     * @return The registered object.
     */
    public <R extends T> R register(String name, R value) {
        Identifier id = Identifier.of(namespace, name);
        if (containsID(id))
            throw new IllegalRegistrationException("Identifier " + id + " is already registered!");

        map.put(id, value);
        return value;
    }

    /**
     * Registers this {@link DeferredRegister} to its target registry in the provided {@link AbstractGame}.<br>
     * See {@link AbstractGame#registerDeferred(DeferredRegister)} for more info on exactly what this does.
     * @param game The {@link AbstractGame} to register to.
     */
    public void register(AbstractGame game) {
        game.registerDeferred(this);
    }
}
