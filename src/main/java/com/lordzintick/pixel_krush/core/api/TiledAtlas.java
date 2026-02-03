package com.lordzintick.pixel_krush.core.api;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TiledAtlas {
    private final TextureRegion[][] regions;

    TiledAtlas(AbstractGame game, String path, int tileWidth, int tileHeight) {
        regions = TextureRegion.split(game.getAssetOrThrow(path), tileWidth, tileHeight);
    }

    public TextureRegion get(int x, int y) {
        return regions[y][x];
    }
    public Texture getTexture() {return get(0, 0).getTexture();}
    public TextureRegion[][] list() {return regions;}
}
