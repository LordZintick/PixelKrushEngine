package com.github.lordzintick.pixel_krush.core.api.ecs.comp;

import com.github.lordzintick.pixel_krush.core.api.ecs.Entity;
import com.github.lordzintick.pixel_krush.core.util.Identifier;
import com.github.lordzintick.pixel_krush.core.util.registry.ImmutableRegistry;
import com.github.lordzintick.pixel_krush.core.util.registry.Registry;

/**
 * A utility component for storing a collection of similar components in a structured, grouped manner using a {@link Registry} (see {@link ComponentCollection#collection}).
 */
public class ComponentCollection extends AbstractComponent {
    /**
     * An {@link ImmutableRegistry} used for holding components in the collection.<br>
     * The use of an {@link ImmutableRegistry} instead of a normal {@link Registry} here prevents any setters after initialization, though the actual components <i>inside</i> the collection can be freely changed.
     */
    private final ImmutableRegistry<AbstractComponent> collection;

    /**
     * A <code>private</code> constructor used by {@link ComponentCollection.Builder} to create a new collection.
     * @param parent The parent {@link Entity} of this component
     * @param collection The pre-defined {@link Registry} for the collection of {@link AbstractComponent}s to include in the collection.
     */
    private ComponentCollection(Entity parent, Registry<AbstractComponent> collection) {
        super(parent);
        this.collection = ImmutableRegistry.of(collection);
    }

    /**
     * Gets a component from the collection with the specified ID.
     * @param id The identifier to query the collection with.
     * @param <T> The requested component type to get.
     * @return The collection found with the specified ID, or throws an exception if none is found.
     */
    public <T extends AbstractComponent> T get(Identifier id) {
        return collection.getOrThrow(id);
    }

    /**
     * @param parent The parent {@link Entity} of the {@link ComponentCollection} that will result from the builder.
     * @return A newly created {@link ComponentCollection.Builder} for simple initialization of a collection.
     */
    public static Builder builder(Entity parent) {return new Builder(parent);}

    /**
     * A builder class used to create a {@link ComponentCollection} easily and straightforwardly.
     */
    public static final class Builder {
        /**
         * The parent {@link Entity} that will result from the builder.
         */
        private final Entity parent;
        /**
         * A {@link Registry} used to hold the components to be passed to the built {@link ComponentCollection}.<br>
         * A {@link Registry} was used in place of an {@link ImmutableRegistry} due to its mutability.
         */
        private final Registry<AbstractComponent> collection = new Registry<>();

        /**
         * A <code>private</code> constructor used by {@link ComponentCollection#builder(Entity)} for constructing a new builder.
         * @param parent The parent {@link Entity} of the {@link ComponentCollection} that will result from the builder.
         */
        private Builder(Entity parent) {
            this.parent = parent;
        }

        /**
         * Puts a component with the specified ID into this {@link Builder} for further building.
         * @param id The ID to register the component with.
         * @param component The component to register into the builder, which will then be passed to the {@link ComponentCollection} upon building.
         * @return The newly modified {@link Builder} instance.
         */
        public Builder put(Identifier id, AbstractComponent component) {
            collection.register(id, component);
            return this;
        }

        /**
         * Builds this builder into a new {@link ComponentCollection} based on this builder's configuration.
         * @return The newly built {@link ComponentCollection} resulting from this builder's configuration.
         */
        public ComponentCollection build() {
            return new ComponentCollection(parent, collection);
        }
    }
}
