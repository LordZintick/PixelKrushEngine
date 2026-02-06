package com.github.lordzintick.pixel_krush.core.ui.api;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.github.lordzintick.pixel_krush.core.api.PositionedRenderable;
import com.github.lordzintick.pixel_krush.core.api.BaseScreen;

/**
 * An abstract base class from which all UI elements extend
 */
public abstract class Widget extends PositionedRenderable {
    /**
     * The {@link BaseScreen} containing this widget.
     */
    protected final BaseScreen screen;
    /**
     * The width of the widget.
     */
    public int width, /**
     * The height of the widget.
     */
 height = 0;
    /**
     * Whether the user is hovering over this widget or not.
     */
    protected boolean hovering = false;
    /**
     * The amount of time this widget has existed for, in seconds.
     */
    protected double ticks = 0;
    /**
     * Whether this widget is visible or not.
     */
    public boolean visible = true;

    /**
     * Constructs a simple {@link Widget} with no defined width or height.
     * @param screen The {@link BaseScreen} this {@link Widget} is for.
     */
    protected Widget(BaseScreen screen) {
        this.screen = screen;
    }

    /**
     * Constructs a {@link Widget} and defines its initial {@code width} and {@code height}.
     * @param screen The {@link BaseScreen} this {@link Widget} is for.
     * @param width The initial width of the {@link Widget}.
     * @param height The initial height of the {@link Widget}.
     */
    protected Widget(BaseScreen screen, int width, int height) {
        this.screen = screen;
        this.width = width;
        this.height = height;
    }

    /**
     * Triggers when the user clicks this {@link Widget}.
     * @param button The mouse button used to click the {@link Widget}.
     */
    public void click(int button) {}

    /**
     * Triggers when the user releases their mouse over this {@link Widget}.
     * @param button The mouse button released over this {@link Widget}.
     */
    public void release(int button) {}

    /**
     * Triggers when the user hovers their mouse over this {@link Widget}.
     */
    public void hover() {
        hovering = true;
    }

    /**
     * Triggers when the user moves their mouse away from over this {@link Widget}.
     */
    public void unHover() {
        hovering = false;
    }

    @Override
    public void render(Batch batch, float deltaTime) {
        ticks += deltaTime;
    }
}
