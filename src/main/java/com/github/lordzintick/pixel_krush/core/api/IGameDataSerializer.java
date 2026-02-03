package com.github.lordzintick.pixel_krush.core.api;

import com.github.lordzintick.pixel_krush.core.util.JsonAccessor;

import java.util.HashMap;

public interface IGameDataSerializer {
    HashMap<String, Object> write();
    void read(JsonAccessor jsonAccessor);
    String getName();
}
