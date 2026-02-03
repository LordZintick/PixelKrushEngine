package com.github.lordzintick.pixel_krush.core.api;

/**
 * An abstract class representing the base of all "UI" screens, that is to say they are part of an out-of-game menu and not part of the physical game
 */
public abstract class AbstractUIScreen extends BaseScreen {
    /**
     * Constructs a new {@link AbstractUIScreen} with the provided {@link AbstractGame}
     * @param game The {@link AbstractGame} instance that this game is for
     */
    public AbstractUIScreen(AbstractGame game) {
        super(game);
    }
}
