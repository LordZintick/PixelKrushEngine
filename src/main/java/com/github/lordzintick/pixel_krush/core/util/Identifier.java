package com.github.lordzintick.pixel_krush.core.util;

public class Identifier {
    private final String namespace;
    private final String path;

    private Identifier(String namespace, String path) {
        this.namespace = namespace;
        this.path = path;
    }

    public static Identifier empty() {
        return new Identifier("", "");
    }

    public static Identifier of(String namespace, String path) {
        return new Identifier(namespace, path);
    }

    public static Identifier of(String identifier) {
        String[] split = identifier.split(":");
        if (split.length != 2) return Identifier.empty();
        return new Identifier(split[0], split[1]);
    }

    public String getNamespace() {
        return namespace;
    }

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

        return ((Identifier) obj).toString().equals(this.toString());
    }
}
