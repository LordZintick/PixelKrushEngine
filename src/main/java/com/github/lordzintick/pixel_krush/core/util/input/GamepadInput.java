package com.github.lordzintick.pixel_krush.core.util.input;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.ControllerMapping;
import com.badlogic.gdx.utils.ObjectMap;
import com.github.lordzintick.pixel_krush.core.api.AbstractGame;
import com.github.lordzintick.pixel_krush.core.util.Identifier;
import com.github.lordzintick.pixel_krush.core.util.MathUtil;
import com.github.lordzintick.pixel_krush.core.util.registry.ImmutableRegistry;
import com.github.lordzintick.pixel_krush.core.ui.api.Widget;

import java.util.function.Function;

/**
 * An input handler class to handle input from a gamepad.
 */
public final class GamepadInput implements ControllerListener {
    /**
     * The {@link AbstractGame Game} this input handler is for.
     */
    private final AbstractGame game;

    /**
     * Constructs a new {@link GamepadInput} for the specified game.
     * @param game The {@link AbstractGame Game} this input handler is for.
     */
    public GamepadInput(AbstractGame game) {
        this.game = game;
    }

    /**
     * Gets all the registered {@link Keybind}s in the parent game.
     * @return An {@link ObjectMap.Values} containing all the registered keybinds in the parent game.
     */
    private ObjectMap.Values<Keybind> getKeybinds() {
        return ((ImmutableRegistry) game.queryRegistryOrThrow(Identifier.of("global", "keybinds"))).valueCollection();
    }

    @Override
    public void connected(Controller controller) {
    }

    @Override
    public void disconnected(Controller controller) {
    }

    @Override
    public boolean buttonDown(Controller controller, int button) {
        for (Keybind keybind : getKeybinds()) {
            for (int bindKey : keybind.keys) {
                if (!keybind.checkContext(game.getScreen())) continue;
                if (bindKey != button) continue;

                keybind.isPressed = true;
                keybind.action.run();
            }
        }

        if (button == controller.getMapping().buttonA) {
            for (Widget widget : game.getScreen().widgets) {
                // Check if the mouse position is in the widgets' area, and call the according method
                if (MathUtil.isPointInArea(game.gamepadCursorX, game.gamepadCursorY, widget.x, widget.y, widget.width, widget.height)) {
                    widget.click(button);
                }
            }
        }
        return false;
    }

    @Override
    public boolean buttonUp(Controller controller, int button) {
        for (Keybind keybind : getKeybinds()) {
            for (int bindKey : keybind.keys) {
                if (!keybind.checkContext(game.getScreen())) continue;
                if (bindKey != button) continue;

                keybind.isPressed = false;
            }
        }
        return false;
    }

    @Override
    public boolean axisMoved(Controller controller, int axis, float value) {
        return false;
    }

    /**
     * Attempts to get the button index of a certain button from a {@link ControllerMapping}, or returns <code>ControllerMapping.UNDEFINED</code> if the {@link ControllerMapping} is null.
     * @param mapping The {@link ControllerMapping} to attempt to get the button from.
     * @param func The function to run to get the button index if the provided <code>mapping</code> is not null.
     * @return The result of the <code>func</code> function when providing the provided <code>mapping</code>, or <code>ControllerMapping.UNDEFINED</code> if <code>mapping</code> is null.
     */
    public static int tryGetButton(ControllerMapping mapping, Function<ControllerMapping, Integer> func) {
        if (mapping == null) return ControllerMapping.UNDEFINED;
        return func.apply(mapping);
    }
}
