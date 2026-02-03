package com.github.lordzintick.pixel_krush.core.util.audio;

import com.badlogic.gdx.audio.Music;
import com.github.lordzintick.pixel_krush.core.api.AbstractGame;

public class IntroLoopSound extends Sound {
    private final Music loop;
    public boolean hasPlayedIntro = false;

    /**
     * Constructs a new {@link Sound} with the provided sound file name
     *
     * @param game
     * @param introFileName The name of the intro sound file this sound is for
     * @param volume   The volume this sound should play at
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
