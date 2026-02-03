package com.github.lordzintick.pixel_krush.core.ui.impl;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.github.lordzintick.pixel_krush.core.api.BaseScreen;
import com.github.lordzintick.pixel_krush.core.util.Text;
import com.github.lordzintick.pixel_krush.core.util.UIUtil;
import com.github.lordzintick.pixel_krush.core.ui.api.Widget;

/**
 * An extension of {@link Widget} describing a clickable button that has a text label attached.<br>
 * Also see {@link TextLabel}
 */
public class TextButton extends Widget {
    public Text label;
    public final Runnable onClick;
    private NinePatch texture, textureHover;
    private Texture debugTexture;

    /**
     * Initialize the textures needed for rendering
     */
    private void initTextures() {
        texture = new NinePatch((Texture) screen.game.getAssetOrThrow("textures/ui/button.png"), 2, 2, 2, 2);
        textureHover = new NinePatch((Texture) screen.game.getAssetOrThrow("textures/ui/button_pressed.png"), 2, 2, 2, 2);
        debugTexture = screen.game.getAssetOrThrow("textures/debug.png");
    }

    /**
     * @param screen The {@link BaseScreen} this {@link TextButton} is for
     * @param label The {@link Text} to display on this button
     * @param x The initial X position of the button
     * @param y The initial Y position of the button
     * @param width The initial width of the button
     * @param height The initial height of the button
     * @param onClick The {@link Runnable} to trigger when the button is left-clicked
     */
    public TextButton(BaseScreen screen, Text label, int x, int y, int width, int height, Runnable onClick) {
        super(screen, width, height);
        this.x = x - (float) width / 2;
        this.y = y - (float) height / 2;
        this.label = label;
        this.onClick = onClick;
        initTextures();
    }

    @Override
    public void click(int button) {
        // If the click was a left click, run the onClick runnable
        if (button == Input.Buttons.LEFT) {
            onClick.run();
        }
    }

    @Override
    public void render(Batch batch, float deltaTime) {
        super.render(batch, deltaTime);
        if (!visible) return;
        // Draw the debug texture
        batch.draw(debugTexture, x, y, width, height);

        // Draw the corresponding texture depending on whether the user is hovering over the button or not
        if (hovering) {
            textureHover.draw(batch, x, y, 0, 0, 16, 8, (float) width / 16, (float) height / 8, 0);
        } else {
            texture.draw(batch, x, y, 0, 0, 16, 8, (float) width / 16, (float) height / 8, 0);
        }

        // Use UIUtil to render the text will all of its configured formatting
        UIUtil.renderText(screen.game, batch, label, x, y - (float) -height / 2 + screen.game.getFont("normal").getLineHeight() / 4, width, true);
    }
}
