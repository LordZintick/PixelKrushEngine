package com.github.lordzintick.pixel_krush.core.api.ecs.comp;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.github.lordzintick.pixel_krush.core.api.ecs.Entity;

/**
 * A component used to track various animation-related data, such as current {@link AnimationComponent#frame}, current {@link AnimationComponent#texture}, and {@link AnimationComponent#frameCount}.
 */
public class AnimationComponent extends AbstractComponent {
    /**
     * The amount of time it takes to pass to the next frame in the animation(s) in question.
     */
    public final float frameTime;
    /**
     * The total amount of frames in the animation(s) in question.
     */
    public final int frameCount;

    /**
     * A constantly-increasing value that ticks up every frame rendered.<br>
     * Used for detecting when to change frame.
     */
    public float animTicks = 0;
    /**
     * The current frame of animation the animation(s) in question are in.
     */
    public int frame = 0;
    /**
     * The current texture to be rendered for the animation.
     */
    public TextureRegion texture;

    /**
     * Constructs a new {@link AnimationComponent} with the provided frame time and count, and attaches it to the specified entity.
     * @param parent The parent {@link Entity} of this component
     * @param frameTime The amount of time it takes to pass to the next frame in the animation(s) in question.
     * @param frameCount The total number of frames in the animation(s) in question.
     */
    public AnimationComponent(Entity parent, float frameTime, int frameCount) {
        super(parent);
        this.frameTime = frameTime;
        this.frameCount = frameCount;
    }
}
