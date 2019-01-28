package com.alfred.yuan.utilities;

import com.alfred.yuan.corecursive.TailCall;
import com.alfred.yuan.function.Function;
import com.alfred.yuan.function.base.Effect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by alfred_yuan on 2019-01-27
 */
public interface CollectionUtilities {
    static <T> List<T> list() {
        return Collections.emptyList();
    }

    static <T> List<T> list(T t) {
        return Collections.singletonList(t);
    }

    static <T> List<T> list(List<T> ts) {
        return Collections.unmodifiableList(copy(ts));
    }

    @SafeVarargs
    static <T> List<T> list(T... ts) {
        return Collections.unmodifiableList(Arrays.asList(Arrays.copyOf(ts, ts.length)));
    }

    static <T> T head(List<T> list) {
        if (list.size() == 0) {
            throw new IllegalStateException("head of empty list");
        }
        return list.get(0);
    }

    static <T> List<T> tail(List<T> list) {
        if (list.size() == 0) {
            throw new IllegalStateException("tail of empty list");
        }
        List<T> workList = copy(list);
        workList.remove(0);
        return Collections.unmodifiableList(workList);
    }

    static <T> List<T> prepend(T t, List<T> list) {
        List<T> ts = copy(list);
        ts.add(0, t);
        return Collections.unmodifiableList(ts);
    }

    static <T> List<T> append(List<T> list, T t) {
        List<T> ts = copy(list);
        ts.add(t);
        return Collections.unmodifiableList(ts);
    }

    static <T, U> U flodLeft(
        List<T> ts
        , U identity
        , Function<U, Function<T, U>> accumulator) {
        return flodLeft_(identity, ts, accumulator).eval();
    }

    private static <T, U> TailCall<U> flodLeft_(
        U acc,
        List<T> ts
        , Function<U, Function<T, U>> accumulator
    ) {
        return ts.isEmpty()
            ? TailCall.ret(acc)
            : TailCall.sus(() -> flodLeft_(accumulator.apply(acc).apply(head(ts)), tail(ts), accumulator));
    }

    /**
     * <b>when considering using fold-Right, you should do one of the following:</b>
     * <ol>
     *     <li>Not care about performance</li>
     *     <li>Change the function (if possible) and use foldLeft</li>
     *     <li>Use foldRight only with small lists</li>
     *     <li>Use an imperative implementation</li>
     * </ol>
     */
    static <T, U> U flodRight(
        List<T> ts
        , U identity
        , Function<T, Function<U, U>> accumulator) {
        return flodRight_(identity, reverse(ts), accumulator).eval();
    }

    private static <T, U> TailCall<U> flodRight_(
        U acc
        , List<T> ts
        , Function<T, Function<U, U>> accumulator
    ) {
        return ts.isEmpty()
            ? TailCall.ret(acc)
            : TailCall.sus(() -> flodRight_(accumulator.apply(head(ts)).apply(acc), tail(ts), accumulator));
    }

    static <T> List<T> reverse(List<T> ts) {
        return flodLeft(
            ts
            , list()
            , x -> y -> prepend(y, x)
        );
    }

    static <T, U> List<U> map(List<T> list, Function<T, U> f) {
        return flodLeft(
            list
            , list()
            , x -> y -> append(x, f.apply(y))
        );
    }

    static <T> void forEach(Collection<T> ts, Effect<T> e) {
        for (T t : ts) {
            e.apply(t);
        }
    }

    static List<Integer> range(int start, int end) {
        return range_(list(), start, end).eval();
    }

    private static TailCall<List<Integer>> range_(List<Integer> acc, int start, int end) {
        return start >= end
            ? TailCall.ret(acc)
            : TailCall.sus(() -> range_(append(acc, start), start + 1, end));
    }

    static <T> List<T> unflod(T seed, Function<T, T> next, Function<T, Boolean> predictor) {
        List<T> result = new ArrayList<>();
        T temp = seed;
        while (predictor.apply(temp)) {
            result = append(result, temp);
            temp = next.apply(temp);
        }
        return result;
    }

    private static <T> List<T> copy(List<T> ts) {
        return new ArrayList<>(ts);
    }
}
