package com.github.lordzintick.pixel_krush.core.api.ecs.comp;

import com.badlogic.gdx.math.MathUtils;
import com.github.lordzintick.pixel_krush.core.api.ecs.Entity;

public class RangedFloatComponent extends ValueComponent<Float> {
    private float min, max;

    public RangedFloatComponent(Entity parent, float value, float min, float max) {
        super(parent, value);
        this.min = min;
        this.max = max;
    }

    public void setLimits(float min, float max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public void set(Float value) {
        this.value = MathUtils.clamp(value, min, max);
    }
}
