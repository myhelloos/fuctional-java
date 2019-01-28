package com.alfred.yuan.utilities;

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
        U result = identity;
        for (T t : ts) {
            result = accumulator.apply(result).apply(t);
        }
        return result;
    }

    static <T, U> U flodRight(
            List<T> ts
            , U identity
            , Function<T, Function<U, U>> accumulator) {
//        U result = identity;
//        for (int i = ts.size(); i > 0; --i) {
//            result = accumulator.apply(ts.get(i -1)).apply(result);
//        }
//        return result;
        return ts.isEmpty()
                ? identity
                : accumulator.apply(head(ts)).apply(flodRight(tail(ts), identity, accumulator));
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
        return unflod(start, temp -> temp + 1, temp -> end > temp);
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
