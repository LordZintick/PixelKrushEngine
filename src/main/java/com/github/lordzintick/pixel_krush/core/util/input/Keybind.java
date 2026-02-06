package com.github.lordzintick.pixel_krush.core.util.input;

import com.github.lordzintick.pixel_krush.core.api.AbstractGameScreen;
import com.github.lordzintick.pixel_krush.core.api.AbstractUIScreen;
import com.github.lordzintick.pixel_krush.core.api.BaseScreen;

/**
 * An input class defining an array of integer key codes and an action to run when any of the keys are pressed.
 */
public class Keybind {
    /**
     * The array of key codes to check for.
     */
    public final int[] keys;
    /**
     * The {@link Keybind.Context} this keybind is valid in.
     */
    public final Context context;
    /**
     * The {@link Runnable} to run when this keybind is pressed.
     */
    public final Runnable action;
    /**
     * Whether this keybind is pressed or not.
     */
    public boolean isPressed = false;

    /**
     * Constructs a new {@link Keybind} with the provided context, action, and key code array.
     * @param context The {@link Context} for the keybind to be valid in.
     * @param action The action to run when the keybind is pressed.
     * @param keys The array of key codes this keybind is checking for.
     */
    public Keybind(Context context, Runnable action, int... keys) {
        this.keys = keys;
        this.context = context;
        this.action = action;
    }

    /**
     * Constructs a new {@link Keybind} with the provided context and key code array and an empty action.
     * @param context The {@link Context} for the keybind to be valid in.
     * @param keys The array of key codes this keybind is checking for.
     */
    public Keybind(Context context, int... keys) {
        this.keys = keys;
        this.context = context;
        this.action = () -> {};
    }

    /**
     * Gets the "unknown" keybind for cases where you want an empty keybind.
     * @return A keybind with no keycodes defined, thus making it impossible to press.
     */
    public static Keybind unknown() {
        return new Keybind(Context.UI, () -> {});
    }

    /**
     * Gets whether this keybind's context is correct for the provided screen.<br>
     * Essentially, if the screen is an {@link AbstractUIScreen} and this keybind's context is <code>Context.UI</code>, or
     * if the screen is an {@link AbstractGameScreen} and this keybind's context is <code>Context.GAME</code>, then this check returns <code>true</code>.
     * @param screen The {@link BaseScreen} to check this keybind's context against.
     * @return Whether this keybind has the correct context for the provided screen.
     */
    public boolean checkContext(BaseScreen screen) {
        return
            (screen instanceof AbstractUIScreen && (context == Context.UI)) ||
                (screen instanceof AbstractGameScreen && (context == Context.GAME));
    }

    /**
     * An enum representing a keybind "context."
     */
    public enum Context {
        UI,
        GAME
    }
}
