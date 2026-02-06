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

/**
 * An extension of {@link Widget} used for small notifications that show up in the top-right corner of the screen.
 */
public abstract class Toast extends Widget {
    /**
     * A {@link TextureRegion} representing the icon of the widget to show to the right of the text.
     */
    public final TextureRegion icon;
    /**
     * {@link Text} representing the text to show next to the icon on the display.
     */
    public final Text displayName;

    /**
     * Constructs a new {@link Toast} with the provided icon and display name.
     * @param screen The {@link BaseScreen} this {@link Toast} should be displayed on.
     * @param icon The {@link TextureRegion} representing the icon of this {@link Toast} to show to the right of the text on the display.
     * @param displayName The text to show next to the icon on the display.
     */
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
