package com.github.lordzintick.pixel_krush.core.util;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.github.lordzintick.pixel_krush.core.api.IGameDataSerializer;

public class JsonAccessor {
    private final String name;
    private final Json json;
    private final JsonValue data;

    public JsonAccessor(String name, Json json, JsonValue data) {
        this.name = name;
        this.json = json;
        this.data = data;
    }

    public <T> T get(String key, Class<T> type) {
        return json.readValue("\"" + key + "\"", type, data.get("\"" + name + "\""));
    }
}
