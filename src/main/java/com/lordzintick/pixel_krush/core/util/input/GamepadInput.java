package com.lordzintick.pixel_krush.core.util.input;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.ControllerMapping;
import com.badlogic.gdx.utils.ObjectMap;
import com.lordzintick.pixel_krush.core.api.AbstractGame;
import com.lordzintick.pixel_krush.core.util.Identifier;
import com.lordzintick.pixel_krush.core.util.MathUtil;
import com.lordzintick.pixel_krush.core.util.registry.ImmutableRegistry;
import com.lordzintick.pixel_krush.core.ui.api.Widget;

import java.util.function.Function;

public final class GamepadInput implements ControllerListener {
    private final AbstractGame game;

    public GamepadInput(AbstractGame game) {
        this.game = game;
    }

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

    public static int tryGetButton(ControllerMapping mapping, Function<ControllerMapping, Integer> func) {
        if (mapping == null) return ControllerMapping.UNDEFINED;
        return func.apply(mapping);
    }
}
