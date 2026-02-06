package com.github.lordzintick.pixel_krush.core.util;

import com.badlogic.gdx.Gdx;

/**
 * A small wrapper class to provide some utilities for logging.
 */
public class Logger {
    /**
     * The prefix to use for logging.
     */
    private final String prefix;

    /**
     * Constructs a new {@link Logger} with the prefix set to the provided class' simple name.
     * @param clazz The class to use for the prefix.
     */
    public Logger(Class<?> clazz) {
        this.prefix = clazz.getSimpleName();
    }

    /**
     * Constructs a new {@link Logger} with the provided prefix.
     * @param prefix The prefix to use when logging.
     */
    public Logger(String prefix) {
        this.prefix = prefix;
    }

    /**
     * Logs a message to the output with the preconfigured prefix.
     * @param msg The message to log.
     */
    public void log(String msg) {
        Gdx.app.log(prefix, msg);
    }

    /**
     * Logs a message to the output with the preconfigured prefix in a red font color and with the additional "Warning:" prefix.
     * @param msg The message to warn.
     */
    public void warn(String msg) {Gdx.app.error(prefix, "Warning: " + msg);}
}
