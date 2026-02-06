package com.github.lordzintick.pixel_krush.core.api.ecs.comp;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.github.lordzintick.pixel_krush.core.api.TiledAtlas;
import com.github.lordzintick.pixel_krush.core.api.ecs.Entity;

/**
 * A somewhat specialized component used for storing an array of {@link TextureRegion}s, such as for an animation, etc.
 */
public class TextureArrayComponent extends AbstractComponent {
    /**
     * The array of {@link TextureRegion}s that stores the actual image data.
     */
    private TextureRegion[][] textureArray;

    /**
     * Constructs a new {@link TextureArrayComponent} by splitting a {@link Texture} into frames.
     * @param parent The parent {@link Entity} of this component.
     * @param texture The base {@link Texture} to split into frames.
     * @param width The width of an image frame.
     * @param height The height of an image frame.
     */
    public TextureArrayComponent(Entity parent, Texture texture, int width, int height) {
        super(parent);
        this.textureArray = TextureRegion.split(texture, width, height);
    }

    /**
     * Constructs a new {@link TextureArrayComponent} from a pre-split {@link TiledAtlas}.
     * @param parent The parent {@link Entity} of this component.
     * @param texture A {@link TiledAtlas} to load the texture frames from.
     */
    public TextureArrayComponent(Entity parent, TiledAtlas texture) {
        super(parent);
        this.textureArray = texture.list();
    }

    /**
     * Gets a {@link TextureRegion} frame from the texture array.
     * @param x The x-coordinate, relative to the left of the image, of the texture frame to get.
     * @param y The y-coordinate, relative to the top of the image, of the texture fram to get.
     * @return The frame in the texture array corresponding to the provided coordinates.
     */
    public TextureRegion get(int x, int y) {
        return textureArray[y][x];
    }

    /**
     * Sets the current split frames to the specified texture array.
     * @param regions A pre-split {@link TextureRegion} array containing the new texture frames in this component.
     */
    public void set(TextureRegion[][] regions) {
        textureArray = regions;
    }
}
