package com.lordzintick.pixel_krush.core.api;

import com.badlogic.gdx.math.Rectangle;
import com.lordzintick.pixel_krush.core.util.IUpdateable;

/**
 * An abstract class representing the base for all physical game objects
 */
public abstract class AbstractGameObject extends PositionedRenderable implements IUpdateable {
    public int width, height;
    public float scale;
    boolean shouldRemove = false;
    public final AbstractGameScreen screen;
    public Rectangle collisionRect;

    /**
     * Constructs a new game object in the provided screen
     * @param screen The {@link AbstractGameScreen} that is the parent/holder of this game object
     */
    public AbstractGameObject(AbstractGameScreen screen) {
        this.screen = screen;
        this.collisionRect = new Rectangle(x, y, width, height);
    }

    public void update(float deltaTime) {
        this.collisionRect.set(x, y, width, height);
    }
    public void dispose() {}
    public void collide(AbstractGameObject other) {}
    public void remove() {this.shouldRemove = true;}
    public final boolean shouldRemove() {return shouldRemove;}
}
