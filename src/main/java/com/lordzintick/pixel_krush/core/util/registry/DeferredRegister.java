package com.lordzintick.pixel_krush.core.util.registry;

import com.lordzintick.pixel_krush.core.api.AbstractGame;
import com.lordzintick.pixel_krush.core.util.Identifier;

public class DeferredRegister<T> extends Registry<T> {
    private final Identifier registryId;
    private final String namespace;

    private DeferredRegister(Identifier registryId, String namespace) {
        this.registryId = registryId;
        this.namespace = namespace;
    }

    public Identifier getRegistryId() {return registryId;}

    public static <R> DeferredRegister<R> create(AbstractGame game, Identifier registryId) {
        return new DeferredRegister<>(registryId, game.getNamespace());
    }

    @Override
    public <R extends T> R register(Identifier id, R value) {
        return register(id.getPath(), value);
    }

    public <R extends T> R register(String name, R value) {
        Identifier id = Identifier.of(namespace, name);
        if (containsID(id))
            throw new IllegalRegistrationException("Identifier " + id + " is already registered!");

        map.put(id, value);
        return value;
    }

    public void register(AbstractGame game) {
        game.registerDeferred(this);
    }
}
