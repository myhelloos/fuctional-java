package com.alfred.yuan.util;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by alfred_yuan on 2019-01-30
 */
public class Map<T, U> {
    private final ConcurrentMap<T, U> map = new ConcurrentHashMap<>();

    public static <T, U> Map<T, U> empty() {
        return new Map<>();
    }

    public static <T, U> Map<T, U> add(Map<T, U> map, T key, U value) {
        map.map.put(key, value);
        return map;
    }

    public Option<U> get(final T key) {
        return this.map.containsKey(key)
            ? Option.some(this.map.get(key))
            : Option.none();
    }

    public Map<T, U> put(T key, U value) {
        return add(this, key, value);
    }

    public Map<T, U> removeKey(T key) {
        this.map.remove(key);
        return this;
    }
}
