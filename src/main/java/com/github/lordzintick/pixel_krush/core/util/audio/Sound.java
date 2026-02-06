package com.github.lordzintick.pixel_krush.core.util.audio;

import com.badlogic.gdx.audio.Music;
import com.github.lordzintick.pixel_krush.core.api.AbstractGame;

/**
 * An audio class simply holding a {@link Sound#fileName} of a sound to play and some sound metadata.
 */
public class Sound {
    /**
     * The file path for the sound file stored in this sound.
     */
    public final String fileName;
    /**
     * Whether this sound loops or not.
     */
    public final boolean looping;
    /**
     * The volume coefficient from <code>0.0</code> to <code>1.0</code>, <code>1.0</code> being full volume and <code>0.0</code> being muted.
     */
    public final float volume;
    /**
     * Whether this sound is a music track (<code>true</code>) or a sound effect (<code>false</code>)
     */
    public final boolean stream;
    /**
     * If this sound is a sound effect, the {@link com.badlogic.gdx.audio.Sound} reference.
     */
    private com.badlogic.gdx.audio.Sound sfx;
    /**
     * If this sound is a music track, the {@link Music} reference.
     */
    protected Music music;

    /**
     * Constructs a new {@link Sound} with the provided sound file name.
     * @param fileName The name of the sound file this sound is for.
     * @param looping Whether this sound should loop when it is finished.
     * @param volume The volume this sound should play at.
     * @param stream Whether this sound should stream instead of play directly. If this is true, it is considered music. If false, it is a sound effect.
     */
    public Sound(AbstractGame game, String fileName, boolean looping, float volume, boolean stream) {
        this.fileName = fileName;
        this.looping = looping;
        this.volume = volume;
        this.stream = stream;

        if (stream) {
            music = game.getAssetOrThrow(fileName);
            music.setLooping(looping);
            music.setVolume(volume);
        } else {
            sfx = game.getAssetOrThrow(fileName);
        }
    }

    /**
     * If this sound has {@code stream} set to {@code true}, pauses the music track
     */
    public void pause() {
        if (stream) {
            music.pause();
        }
    }

    /**
     * Plays the sound effect/music track and applies metadata
     */
    public void play() {
        if (stream) {
            music.play();
        } else {
            long id = sfx.play(volume);
            sfx.setLooping(id, looping);
        }
    }

    /**
     * If <code>stream = true</code>, plays the music track with the specified {@link Music.OnCompletionListener} to trigger when it has finished playing.
     * @param onCompletionListener The {@link Music.OnCompletionListener} to trigger when the music track has finished.
     */
    public void play(Music.OnCompletionListener onCompletionListener) {
        if (stream) {
            music.setOnCompletionListener(onCompletionListener);
            music.play();
        }
    }

    /**
     * Stops the sound effect/music track
     */
    public void stop() {
        if (stream) {
            music.stop();
        } else {
            sfx.stop();
        }
    }

    /**
     * Disposes the sound effect/music track, freeing up resources
     */
    public void dispose() {
        if (stream) {
            music.dispose();
        } else {
            sfx.dispose();
        }
    }
}
