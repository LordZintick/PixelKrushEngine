package com.github.lordzintick.pixel_krush.core.api;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * A wrapper around a {@link TextureRegion}<code>[][]</code> providing some simple utilities.
 */
public class TiledAtlas {
    /**
     * A 2D {@link TextureRegion} array representing a grid of textures split from one big texture atlas.
     */
    private final TextureRegion[][] regions;

    /**
     * Constructs a new {@link TiledAtlas} that splits a texture retrieved from a certain path into tiles of a specified size.
     * @param game The {@link AbstractGame Game} to get the texture from.
     * @param path The path of the texture atlas.
     * @param tileWidth The width of a region.
     * @param tileHeight The height of a region.
     */
    TiledAtlas(AbstractGame game, String path, int tileWidth, int tileHeight) {
        regions = TextureRegion.split(game.getAssetOrThrow(path), tileWidth, tileHeight);
    }

    /**
     * Gets a texture from the texture array at the specified x and y coordinates.
     * @param x The x-coordinate, relative to the left side of the atlas, of the texture to get.
     * @param y The y-coordinate, relative to the top side of the atlas, of the texture to get.
     * @return The {@link TextureRegion} at the specified x and y coordinates.
     */
    public TextureRegion get(int x, int y) {
        return regions[y][x];
    }

    /**
     * Gets the texture used to create the atlas.
     * @return The texture atlas the {@link TiledAtlas} is getting its texture regions from.
     */
    public Texture getTexture() {return get(0, 0).getTexture();}

    /**
     * Gets the array that stores the texture regions in the atlas.
     * @return The texture array used to store texture regions.
     */
    public TextureRegion[][] list() {return regions;}
}
