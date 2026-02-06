package com.github.lordzintick.pixel_krush.core.util;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.github.lordzintick.pixel_krush.core.api.IGameDataSerializer;

/**
 * A utility wrapper around a {@link Json} instance, {@link JsonValue} data, and a string name.
 */
public class JsonAccessor {
    /**
     * The name of the {@link JsonAccessor}; used for data categorization.
     */
    private final String name;
    /**
     * The {@link Json} instance used for deserialization.
     */
    private final Json json;
    /**
     * The {@link JsonValue} representing the data from the data file.
     */
    private final JsonValue data;

    /**
     * Constructs a new {@link JsonAccessor} with the provided parameters.
     * @param name The name of the JSON accessor; used for data categorization.
     * @param json The {@link Json} instance used for deserialization.
     * @param data The JSON data from the data file.
     */
    public JsonAccessor(String name, Json json, JsonValue data) {
        this.name = name;
        this.json = json;
        this.data = data;
    }

    /**
     * Gets a value from the specified key and categorized based on the {@link #name} of the {@link JsonAccessor}.
     * @param key The key to get the value of.
     * @param type The {@link Class} that is the type of value to get.
     * @param <T> The type of value to get.
     * @return The value retrieved from the data.
     */
    public <T> T get(String key, Class<T> type) {
        return json.readValue("\"" + key + "\"", type, data.get("\"" + name + "\""));
    }
}
