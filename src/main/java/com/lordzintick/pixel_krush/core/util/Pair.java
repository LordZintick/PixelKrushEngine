package com.lordzintick.pixel_krush.core.util;

import com.badlogic.gdx.utils.OrderedMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class Pair<F, S> {
    private final F first;
    private final S second;

    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    public F first() {return first;}
    public S second() {return second;}

    public static <F, S> List<Pair<F, S>> transformMap(Map<F, S> map) {
        ArrayList<Pair<F, S>> pairList = new ArrayList<>();
        for (F key : map.keySet()) {
            pairList.add(new Pair<>(key, map.get(key)));
        }
        return pairList;
    }

    public static <F, S> List<Pair<F, S>> transformOrderedMap(OrderedMap<F, S> map) {
        ArrayList<Pair<F, S>> pairList = new ArrayList<>();
        for (F key : map.keys()) {
            pairList.add(new Pair<>(key, map.get(key)));
        }
        return pairList;
    }
}
