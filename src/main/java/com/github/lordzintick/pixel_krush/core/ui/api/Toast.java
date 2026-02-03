package com.github.lordzintick.pixel_krush.core.ui.api;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.github.lordzintick.pixel_krush.core.api.BaseScreen;
import com.github.lordzintick.pixel_krush.core.util.Text;
import com.github.lordzintick.pixel_krush.core.util.UIUtil;

public abstract class Toast extends Widget {
    public final TextureRegion icon;
    public final Text displayName;

    protected Toast(BaseScreen screen, TextureRegion icon, Text displayName) {
        super(screen);
        this.icon = icon;
        this.displayName = displayName;
    }

    @Override
    public void render(Batch batch, float deltaTime) {
        super.render(batch, deltaTime);
        SpriteBatch uiBatch = screen.game.getBatch("ui");
        uiBatch.draw(icon, Gdx.graphics.getWidth() - 74, Gdx.graphics.getHeight() - 74, 64, 64);
        BitmapFont font = screen.game.getFont(displayName.font);
        int width = (int) UIUtil.getFontStringWidth(displayName.toString(), font);
        UIUtil.renderText(screen.game, uiBatch,displayName,
                Gdx.graphics.getWidth() - 84 - width,
                Gdx.graphics.getHeight() - font.getLineHeight() * 2f, width, false);
    }
}
