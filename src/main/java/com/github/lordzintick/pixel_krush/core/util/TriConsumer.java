package com.github.lordzintick.pixel_krush.core.util;

/**
 * A utility functional interface that accepts three values and does something.
 * @param <T> The type of the first value to consume.
 * @param <E> The type of the second value to consume.
 * @param <R> The type of the third value to consume.
 */
@FunctionalInterface
public interface TriConsumer<T, E, R> {
    void accept(T t, E e, R r);
}
