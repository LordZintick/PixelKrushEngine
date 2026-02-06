package com.github.lordzintick.pixel_krush.core.util.input;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.ObjectMap;
import com.github.lordzintick.pixel_krush.core.api.AbstractGame;
import com.github.lordzintick.pixel_krush.core.util.MathUtil;
import com.github.lordzintick.pixel_krush.core.util.registry.ImmutableRegistry;
import com.github.lordzintick.pixel_krush.core.ui.api.Widget;

import java.util.ArrayList;
import java.util.function.BiConsumer;

/**
 * A wrapper class around {@link InputProcessor} for game-specific input handling
 */
public final class Input implements InputProcessor {
    /**
     * The {@link AbstractGame Game} this input handler is for.
     */
    private final AbstractGame game;
    /**
     * A <code>boolean</code> array representing the mouse buttons that the user is currently pressing.
     */
    public boolean[] mouseButtonsPressed = new boolean[] {false, false, false, false, false};
    /**
     * A list of {@link BiConsumer}s to listen for when the user scrolls, passing in the x and y scroll deltas.
     */
    public final ArrayList<BiConsumer<Float, Float>> scrollListeners = new ArrayList<>();

    /**
     * Constructs a new {@link Input} with the provided {@link AbstractGame}.
     * @param game The {@link AbstractGame Game} this {@link Input} is for.
     */
    public Input(AbstractGame game) {
        this.game = game;
    }

    /**
     * Gets all the registered {@link Keybind}s in the parent game.
     * @return An {@link ObjectMap.Values} containing all the registered keybinds in the parent game.
     */
    private ObjectMap.Values<Keybind> getKeybinds() {
        return ((ImmutableRegistry) game.queryRegistryOrThrow(AbstractGame.getGlobalId("keybinds"))).valueCollection();
    }

    @Override
    public boolean keyDown(int key) {
        // Iterate through all the keybinds
        for (Keybind keybind : getKeybinds()) {
            for (int bindKey : keybind.keys) {
                if (!keybind.checkContext(game.getScreen())) continue;
                if (bindKey != key) continue;

                keybind.isPressed = true;
                keybind.action.run();
            }
        }
        return false;
    }

    @Override
    public boolean keyUp(int key) {
        // Iterate through all the keybinds
        for (Keybind keybind : getKeybinds()) {
            for (int bindKey : keybind.keys) {
                if (!keybind.checkContext(game.getScreen())) continue;
                if (bindKey != key) continue;

                keybind.isPressed = false;
            }
        }

        return false;
    }

    @Override
    public boolean keyTyped(char key) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        mouseButtonsPressed[button] = true;
        if (game.getScreen() == null) return false;

        for (Keybind keybind : getKeybinds()) {
            for (int bindKey : keybind.keys) {
                if (!keybind.checkContext(game.getScreen())) continue;
                if (bindKey != button) continue;

                keybind.isPressed = true;
                keybind.action.run();
            }
        }

        // Iterate through all the screen's widgets
        for (Widget widget : game.getScreen().widgets) {
            // Check if the mouse position is in the widgets' area, and call the according method
            if (MathUtil.isPointInArea(screenX, screenY, widget.x, widget.y, widget.width, widget.height)) {
                widget.click(button);
            }
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        mouseButtonsPressed[button] = false;
        if (game.getScreen() == null) return false;

        for (Keybind keybind : getKeybinds()) {
            for (int bindKey : keybind.keys) {
                if (!keybind.checkContext(game.getScreen())) continue;
                if (bindKey != button) continue;

                keybind.isPressed = false;
            }
        }


        // Iterate through all the screen's widgets
        for (Widget widget : game.getScreen().widgets) {
            // Check if the mouse position is in the widgets' area, and call the according method
            if (MathUtil.isPointInArea(screenX, screenY, widget.x, widget.y, widget.width, widget.height)) {
                widget.release(button);
            }
        }
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        if (game.getScreen() == null) return false;

        // Iterate through all the screen's widgets
        for (Widget widget : game.getScreen().widgets) {
            // Check if the mouse position is in the widgets' area, and call the according method
            if (MathUtil.isPointInArea(screenX, screenY, widget.x, widget.y, widget.width, widget.height)) {
                widget.hover();
            } else {
                widget.unHover();
            }
        }
        return false;
    }

    @Override
    public boolean scrolled(float deltaX, float deltaY) {
        for (BiConsumer<Float, Float> listener : scrollListeners) {
            listener.accept(deltaX, deltaY);
        }
        return false;
    }
}
