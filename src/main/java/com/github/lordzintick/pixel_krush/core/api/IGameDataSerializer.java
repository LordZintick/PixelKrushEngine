package com.github.lordzintick.pixel_krush.core.api;

import com.github.lordzintick.pixel_krush.core.util.JsonAccessor;

import java.util.HashMap;

/**
 * An interface for all data I/O serializers/deserializers used in saving and loading game data.
 */
public interface IGameDataSerializer {
    /**
     * Gathers the data to write to the game data file when saving.
     * @return A {@link HashMap} of mapped key-value pairs to write to the data file.
     */
    HashMap<String, Object> write();

    /**
     * Updates values with serialized ones from a {@link JsonAccessor}.
     * @param jsonAccessor A {@link JsonAccessor} instance containing the deserialized JSON data.
     */
    void read(JsonAccessor jsonAccessor);

    /**
     * Used to define the name of the data serializer, used for data categorization.<br>
     * Should usually be the game namespace, but can really be anything.
     * @return The name of the serializer.
     */
    String getName();
}
