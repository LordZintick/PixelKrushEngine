package com.github.lordzintick.pixel_krush.core.util.audio;

import com.badlogic.gdx.audio.Music;
import com.github.lordzintick.pixel_krush.core.api.AbstractGame;

/**
 * An extension of {@link Sound} representing a sound that has an intro part that plays initially and a loop part that loops after the intro fully plays.
 */
public class IntroLoopSound extends Sound {
    /**
     * The {@link Music} for the looping part.
     */
    private final Music loop;
    /**
     * Whether this sound has fully played the intro yet.
     */
    public boolean hasPlayedIntro = false;

    /**
     * Constructs a new {@link IntroLoopSound} with the provided sound file name.
     * @param game The {@link AbstractGame Game} to get the sound file from.
     * @param introFileName The name of the intro sound file this sound is for
     * @param volume The volume this sound should play at
     */
    public IntroLoopSound(AbstractGame game, String introFileName, String loopFileName, float volume) {
        super(game, introFileName, false, volume, true);

        loop = game.getAssetOrThrow(loopFileName);
        loop.setLooping(true);
        loop.setVolume(volume);
    }

    @Override
    public void pause() {
        if (hasPlayedIntro) {
            loop.pause();
        } else {
            music.pause();
        }
    }

    @Override
    public void play() {
        if (hasPlayedIntro) {
            loop.play();
        } else {
            music.setOnCompletionListener(music1 -> {
                hasPlayedIntro = true;
                play();
            });
            music.play();
        }
    }

    @Override
    public void play(Music.OnCompletionListener onCompletionListener) {
        loop.setOnCompletionListener(onCompletionListener);
        this.play();
    }

    @Override
    public void stop() {
        hasPlayedIntro = false;
        loop.stop();
        music.stop();
    }

    @Override
    public void dispose() {
        music.dispose();
        loop.dispose();
    }
}
