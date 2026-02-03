package com.github.lordzintick.pixel_krush.core.api.ecs.comp;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.github.lordzintick.pixel_krush.core.api.TiledAtlas;
import com.github.lordzintick.pixel_krush.core.api.ecs.Entity;

public class TextureArrayComponent extends AbstractComponent {
    private TextureRegion[][] textureArray;

    public TextureArrayComponent(Entity parent, Texture texture, int width, int height) {
        super(parent);
        this.textureArray = TextureRegion.split(texture, width, height);
    }

    public TextureArrayComponent(Entity parent, TiledAtlas texture) {
        super(parent);
        this.textureArray = texture.list();
    }

    public TextureRegion get(int x, int y) {
        return textureArray[y][x];
    }

    public void set(TextureRegion[][] regions) {
        textureArray = regions;
    }
}
