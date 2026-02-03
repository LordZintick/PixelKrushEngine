package com.lordzintick.pixel_krush.core.util.input;

import com.lordzintick.pixel_krush.core.api.AbstractGameScreen;
import com.lordzintick.pixel_krush.core.api.AbstractUIScreen;
import com.lordzintick.pixel_krush.core.api.BaseScreen;

/**
 * An input class defining a key and an action to run when that key is pressed
 */
public class Keybind {
    public final int[] keys;
    public final Context context;
    public final Runnable action;
    public boolean isPressed = false;

    /**
     * Construct a new Keybind with the provided key and action
     * @param context The {@link Context} for the keybind to take effect in
     * @param action The action to run when the aforementioned key is pressed
     */
    public Keybind(Context context, Runnable action, int... keys) {
        this.keys = keys;
        this.context = context;
        this.action = action;
    }

    public Keybind(Context context, int... keys) {
        this.keys = keys;
        this.context = context;
        this.action = () -> {};
    }

    public static Keybind unknown() {
        return new Keybind(Context.UI, () -> {});
    }

    public boolean checkContext(BaseScreen screen) {
        return
            (screen instanceof AbstractUIScreen && (context == Context.UI)) ||
                (screen instanceof AbstractGameScreen && (context == Context.GAME));
    }

    public enum Context {
        UI,
        GAME
    }
}
