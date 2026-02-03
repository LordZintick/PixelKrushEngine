package com.lordzintick.pixel_krush.core.api.ecs.comp;

import com.lordzintick.pixel_krush.core.api.ecs.Entity;
import com.lordzintick.pixel_krush.core.util.Identifier;
import com.lordzintick.pixel_krush.core.util.registry.Registry;

public class ComponentCollection extends AbstractComponent {
    private final Registry<AbstractComponent> collection;

    private ComponentCollection(Entity parent, Registry<AbstractComponent> collection) {
        super(parent);
        this.collection = collection;
    }

    public <T extends AbstractComponent> T get(Identifier id) {
        return collection.getOrThrow(id);
    }

    public static Builder builder(Entity parent) {return new Builder(parent);}

    public static final class Builder {
        private final Entity parent;
        private final Registry<AbstractComponent> collection = new Registry<>();

        private Builder(Entity parent) {
            this.parent = parent;
        }

        public Builder put(Identifier id, AbstractComponent component) {
            collection.register(id, component);
            return this;
        }

        public ComponentCollection build() {
            return new ComponentCollection(parent, collection);
        }
    }
}
