package com.lordzintick.pixel_krush.core.api;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.lordzintick.pixel_krush.core.util.audio.Sound;
import com.lordzintick.pixel_krush.core.util.Logger;
import com.lordzintick.pixel_krush.core.util.registry.LazyList;
import com.lordzintick.pixel_krush.core.impl.Particle;
import com.lordzintick.pixel_krush.core.ui.api.Widget;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * An abstract base class from which {@link AbstractGameScreen} and {@link com.lordzintick.java_game.ui.screen.AbstractUIScreen} extend
 */
public abstract class BaseScreen {
    protected final Logger LOGGER = new Logger(this.getClass());
    public final AbstractGame game;
    public final ArrayList<Widget> widgets = new ArrayList<>();
    public final LazyList<AbstractGameObject> objects = new LazyList<>();
    protected boolean paused = false;
    private int playingIndex = -1;

    /**
     * Constructs a new {@link BaseScreen} with the provided {@link AbstractGame} and adds the initial widgets to it
     * @param game The {@link AbstractGame} this screen is for
     */
    protected BaseScreen(AbstractGame game) {
        this.game = game;
        addWidgets();
        populateInitialObjects();
    }

    /**
     * Provides the background music to play when this screen is open.<br>
     * Return {@code null} to disable.<br>
     * Also note that all of these MUST be music tracks ({@link Sound#stream} = {@code true}) in order to work correctly
     * @return A list of {@link Sound}s of which to pick a random one to play while this screen is open
     */
    public List<Sound> getBackgroundMusic() {
        return null;
    }

    /**
     * Provides the background color of this screen
     * @return The background color of this screen
     */
    public Color getBackgroundColor() {return Color.BLACK;}

    /**
     * A simple utility to get the midpoint X coordinate of the screen
     * @return The midpoint X of the screen
     */
    protected int getMidX() {return Gdx.graphics.getWidth() / 2;}
    /**
     * A simple utility to get the midpoint Y coordinate of the screen
     * @return The midpoint Y of the screen
     */
    protected int getMidY() {return Gdx.graphics.getHeight() / 2;}

    /**
     * Called to add the UI widgets to this screen
     */
    protected abstract void addWidgets();
    protected void populateInitialObjects() {}

    public void startMusic() {
        if (getBackgroundMusic() == null) return;

        if (playingIndex == -1) playingIndex = game.getRandom().nextInt(getBackgroundMusic().size());
        Sound backgroundMusic = getBackgroundMusic().get(playingIndex);

        if (backgroundMusic != null && backgroundMusic.stream) {
            backgroundMusic.play(music -> {
                playingIndex = -1;
                startMusic();
            });
        }
    }

    public void pauseMusic() {
        if (getBackgroundMusic() == null) return;
        Sound backgroundMusic = getBackgroundMusic().get(Math.max(playingIndex, 0));

        if (backgroundMusic != null && backgroundMusic.stream) {
            backgroundMusic.pause();
        }
    }

    public String getPlayingBackgroundMusic() {
        if (getBackgroundMusic() == null) return null;
        if (playingIndex == -1) return getBackgroundMusic().get(0).fileName;
        return getBackgroundMusic().get(playingIndex).fileName;
    }

    public void pause() {
        paused = true;
        pauseMusic();
    }

    public void resume() {
        paused = false;
        startMusic();
    }

    public boolean isPaused() {return paused;}

    public void update(float deltaTime) {
        Iterator<AbstractGameObject> iterator = objects.iterator();
        while (iterator.hasNext()) {
            AbstractGameObject gameObject = iterator.next();
            if (gameObject.shouldRemove) {
                iterator.remove();
                gameObject.dispose();
            } else {
                gameObject.update(deltaTime);

                if (!(gameObject instanceof Particle)) {
                    Iterator<AbstractGameObject> iterator1 = objects.iterator();
                    while (iterator1.hasNext()) {
                        AbstractGameObject gameObject1 = iterator1.next();

                        if (gameObject == gameObject1
                                || Math.abs(gameObject.x - gameObject1.x) > Math.max(gameObject.collisionRect.width * gameObject.scale, gameObject1.collisionRect.width * gameObject1.scale)
                                || Math.abs(gameObject.y - gameObject1.y) > Math.max(gameObject.collisionRect.height * gameObject.scale, gameObject1.collisionRect.height * gameObject1.scale)
                                || gameObject1 instanceof Particle) continue;
                        if (gameObject.collisionRect.overlaps(gameObject1.collisionRect)) {
                            gameObject.collide(gameObject1);
                            gameObject1.collide(gameObject);
                        }
                    }
                }
            }
        }

        objects.flush();
    }

    public void renderGame(float deltaTime) {
        objects.forEach(gameObject -> gameObject.render(game.getBatch("game"), deltaTime));
    }
    public void renderUI(float deltaTime) {
        widgets.forEach(widget -> widget.render(game.getBatch("ui"), deltaTime));
    }
    public void dispose() {}
}
