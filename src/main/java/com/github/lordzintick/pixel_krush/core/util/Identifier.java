package com.github.lordzintick.pixel_krush.core.util;

/**
 * A utility class holding a {@link #namespace} and a {@link #path}.
 */
public class Identifier {
    /**
     * The namespace of the identifier.
     */
    private final String namespace;
    /**
     * The path of the identifier.
     */
    private final String path;

    /**
     * Constructs a new {@link Identifier} with the specified namespace and path.
     * @param namespace The namespace of the identifier.
     * @param path The path of the identifier.
     */
    private Identifier(String namespace, String path) {
        this.namespace = namespace;
        this.path = path;
    }

    /**
     * Creates an empty {@link Identifier} with empty strings as the namespace and path.
     * @return An empty identifier.
     */
    public static Identifier empty() {
        return new Identifier("", "");
    }

    /**
     * Creates a new {@link Identifier} with the provided namespace and path.
     * @param namespace The namespace of the identifier.
     * @param path The path of the identifier.
     * @return A new identifier with the provided namespace and path.
     */
    public static Identifier of(String namespace, String path) {
        return new Identifier(namespace, path);
    }

    /**
     * Creates a new {@link Identifier} by parsing the provided string.
     * @param identifier The identifier, as a string, with namespace and path separated by <code>":"</code>.
     * @return A new identifier created by parsing the provided string, or an empty identifier if it is invalid.
     */
    public static Identifier of(String identifier) {
        String[] split = identifier.split(":");
        if (split.length != 2) return Identifier.empty();
        return new Identifier(split[0], split[1]);
    }

    /**
     * Gets the namespace of the {@link Identifier}.
     * @return The namespace of the identifier.
     */
    public String getNamespace() {
        return namespace;
    }

    /**
     * Gets the path of the {@link Identifier}.
     * @return The path of the identifier.
     */
    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        return getNamespace() + ":" + getPath();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Identifier))
            return false;

        return obj.toString().equals(this.toString());
    }
}
