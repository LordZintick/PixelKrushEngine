package com.github.lordzintick.pixel_krush.core.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Align;

/**
 * A utility class describing a formatted string of text
 */
public class Text {
    /**
     * The actual text string to render.
     */
    public String text;
    /**
     * The color to render the text with.
     */
    public Color color = Color.WHITE;
    /**
     * Whether the text should render as random characters or should be normal text.
     */
    public boolean glitchy = false;
    /**
     * The alignment of the text, either {@link Align#left}, {@link Align#center}, or {@link Align#right}.
     */
    public int align = Align.left;
    /**
     * The font to render the text with. Directly maps to the name the font is registered with.
     */
    public String font = "normal";

    @Override
    public String toString() {
        return text;
    }

    /**
     * Constructs a new {@link Text} around the provided string.
     * @param text The raw string to format.
     */
    public Text(String text) {
        this.text = text;
    }

    /**
     * Changes the color of the text to the provided color.
     * @param color The new color.
     * @return The modified {@link Text}.
     */
    public Text setColor(Color color) {
        this.color = color;
        return this;
    }

    /**
     * Changes the horizontal alignment of the text to the provided alignment.
     * @param align The new alignment.
     * @return The modified {@link Text}.
     */
    public Text setAlign(int align) {
        this.align = align;
        return this;
    }

    /**
     * Sets the text to be "glitchy," making it display random characters instead of the provided string.
     * @return The modified {@link Text}.
     */
    public Text glitchy() {
        this.glitchy = true;
        return this;
    }

    /**
     * Sets the text to be non-glitchy, returning it to its normal state.
     * @return The modified {@link Text}.
     */
    public Text notGlitchy() {
        this.glitchy = false;
        return this;
    }

    /**
     * Sets the font name of the font to draw the text with to the provided one.
     * @param fontName The new font name to draw with.
     * @return The modified {@link Text}.
     */
    public Text setFont(String fontName) {
        this.font = fontName;
        return this;
    }

    /**
     * Concatenates a string to the end of the raw {@link #text} string.
     * @param otherText The string to concatenate.
     * @return The modified {@link Text}.
     */
    public Text concat(String otherText) {
        this.text = text.concat(otherText);
        return this;
    }
}
