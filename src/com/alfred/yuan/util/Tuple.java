package com.alfred.yuan.util;

/**
 * Created by alfred_yuan on 2019-01-27
 */
public class Tuple<T, U> {

    public final T _1;
    public final U _2;

    public Tuple(T t, U u) {
        this._1 = t;
        this._2 = u;
    }
}