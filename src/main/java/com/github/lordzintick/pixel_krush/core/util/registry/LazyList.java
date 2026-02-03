package com.github.lordzintick.pixel_krush.core.util.registry;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.Consumer;

public class LazyList<T> {
    private final ArrayList<T> removeQueue = new ArrayList<>();
    private final ArrayList<T> values = new ArrayList<>();
    private final ArrayList<T> queue = new ArrayList<>();

    public <R extends T> R add(R value) {
        queue.add(value);
        return value;
    }

    public int size() {return values.size();}

    public boolean contains(T t) {return values.contains(t);}

    public <R extends T> R get(int index) {
        return (R) values.get(index);
    }

    public <R extends T> void forEach(Consumer<R> consumer) {
        values.forEach((Consumer<? super T>) consumer);
    }

    public <R extends T> Iterator<R> iterator() {
        return (Iterator<R>) values.iterator();
    }

    public <R extends T> void remove(R value) {
        if (removeQueue.contains(value))
            throw new IllegalArgumentException("This value is already queued for removal!");
        if (!values.contains(value))
            throw new IllegalArgumentException("This value is not in the list yet!");

        removeQueue.add(value);
    }

    public void flush() {
        values.removeAll(removeQueue);
        removeQueue.clear();
        values.addAll(queue);
        queue.clear();
    }
}
