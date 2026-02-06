package com.github.lordzintick.pixel_krush.core.api;

import com.badlogic.gdx.math.Rectangle;
import com.github.lordzintick.pixel_krush.core.util.IUpdateable;

/**
 * An abstract class representing the base for all physical game objects
 */
public abstract class AbstractGameObject extends PositionedRenderable implements IUpdateable {
    /**
     * The width of the game object.
     */
    public int width, /**
     * The height of the game object.
     */
 height;
    /**
     * The scale factor to scale the rendering and collisions of the object.
     */
    public float scale;
    /**
     * Whether this object should be removed from the screen (it was destroyed, etc.).
     */
    boolean shouldRemove = false;
    /**
     * The parent {@link AbstractGameScreen} of this object.
     */
    public final AbstractGameScreen screen;
    /**
     * A {@link Rectangle} used for collision detection representing the hitbox of this object.
     */
    public Rectangle collisionRect;

    /**
     * Constructs a new game object in the provided screen.
     * @param screen The {@link AbstractGameScreen} that is the parent/holder of this game object.
     */
    public AbstractGameObject(AbstractGameScreen screen) {
        this.screen = screen;
        this.collisionRect = new Rectangle(x, y, width, height);
    }

    /**
     * Updates the object.<br>
     * The default implementation contains an update to the collision rectangle based on position and height, so make sure that this is always called from inheritors!
     * @param deltaTime The time since the last frame was rendered.
     */
    public void update(float deltaTime) {
        this.collisionRect.set(x, y, width, height);
    }

    /**
     * Called when this object is removed from the game/disposed.
     */
    public void dispose() {}

    /**
     * Called when this object collides with another (their collision rectangles overlap).
     * @param other The object this object is colliding with.
     */
    public void collide(AbstractGameObject other) {}

    /**
     * Queues this object to be removed from the game.<br>
     * Note that this happens <i>before</i> removal, not <i>after</i> removal like {@link AbstractGameObject#dispose()}.
     */
    public void remove() {this.shouldRemove = true;}

    /**
     * Gets this object removal queue state.
     * @return Whether this object is queued for removal from the game.
     */
    public final boolean shouldRemove() {return shouldRemove;}
}
