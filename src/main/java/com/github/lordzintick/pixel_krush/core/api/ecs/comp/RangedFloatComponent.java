package com.github.lordzintick.pixel_krush.core.api.ecs.comp;

import com.badlogic.gdx.math.MathUtils;
import com.github.lordzintick.pixel_krush.core.api.ecs.Entity;

/**
 * A miscellaneous utility implementation of {@link ValueComponent} that stores three float values:<br>
 * One for the minimum, one for the value, and one for the maximum.<br>
 * It clamps the value to the minimum and maximum and will truncate all values outside that range.
 */
public class RangedFloatComponent extends ValueComponent<Float> {
    private float min, max;

    /**
     * Constructs a new {@link RangedFloatComponent} that has a value and clamps it to the specified <code>min</code> and <code>max</code>.
     * @param parent The parent {@link Entity} of the component.
     * @param value The initial value to store.
     * @param min The initial minimum value to clamp to.
     * @param max The initial maximum value to clamp to.
     */
    public RangedFloatComponent(Entity parent, float value, float min, float max) {
        super(parent, value);
        this.min = min;
        this.max = max;
    }

    /**
     * Sets new limits for the range.
     * @param min The new minimum value the value can be.
     * @param max The new maximum value the value can be.
     */
    public void setLimits(float min, float max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public void set(Float value) {
        this.value = MathUtils.clamp(value, min, max);
    }
}
