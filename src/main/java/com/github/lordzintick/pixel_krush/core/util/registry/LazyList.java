package com.github.lordzintick.pixel_krush.core.util.registry;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.Consumer;

/**
 * A utility wrapper class around three separate lists:<br>
 * The {@link #values},<br>
 * the {@link #queue},<br>
 * and the {@link #removeQueue}.<br>
 * This list is safe to add/remove objects while iterating through it, as it stores the newly added/removed objects in their respective queues for future ("lazy") flushing using the {@link #flush()} method.<br>
 * Also see {@link LazyRegistry}.
 * @param <T> The type of object to store in the list.
 */
public class LazyList<T> {
    /**
     * The queue of objects to be removed from the list on the next {@link #flush()}.
     */
    private final ArrayList<T> removeQueue = new ArrayList<>();
    /**
     * The current values in the list.
     */
    private final ArrayList<T> values = new ArrayList<>();
    /**
     * The queue of objects to be added to the list on the next {@link #flush()}.
     */
    private final ArrayList<T> queue = new ArrayList<>();

    /**
     * Queues a value to be added to the list on the next {@link #flush()}.
     * @param value The value to add to the list.
     * @param <R> The type of value to add to the list.
     * @return The value added to the list.
     */
    public <R extends T> R add(R value) {
        queue.add(value);
        return value;
    }

    /**
     * Gets the size of the list.
     * @return The size of the current values.
     */
    public int size() {return values.size();}

    /**
     * Gets whether a specified value exists in the list.
     * @param t The value to try and find in the current values.
     * @return Whether the specified value exists in the current values list.
     */
    public boolean contains(T t) {return values.contains(t);}

    /**
     * Gets a value from the list at the specified index.
     * @param index The index of the object to get.
     * @param <R> The type of object to get.
     * @return The object in the current values at the specified index.
     */
    public <R extends T> R get(int index) {
        return (R) values.get(index);
    }

    /**
     * Runs a consumer for each value in the current values list.
     * @param consumer The consumer to run on every value in the current values list.
     * @param <R> The type of value to consume.
     */
    public <R extends T> void forEach(Consumer<R> consumer) {
        values.forEach((Consumer<? super T>) consumer);
    }

    /**
     * Gets the iterator of the current values list for further iteration.
     * @param <R> The type of value to iterate on.
     * @return The iterator of the current values list.
     */
    public <R extends T> Iterator<R> iterator() {
        return (Iterator<R>) values.iterator();
    }

    /**
     * Queues a value to be removed from the list on the next {@link #flush()}.
     * @param value The value to remove from the list.
     * @param <R> The type of value to remove from the list.
     */
    public <R extends T> void remove(R value) {
        if (removeQueue.contains(value))
            throw new IllegalArgumentException("This value is already queued for removal!");
        if (!values.contains(value))
            throw new IllegalArgumentException("This value is not in the list yet!");

        removeQueue.add(value);
    }

    /**
     * Removes all values queued for removal, then adds all values queued for addition, and clears both queues.
     */
    public void flush() {
        values.removeAll(removeQueue);
        removeQueue.clear();
        values.addAll(queue);
        queue.clear();
    }
}
