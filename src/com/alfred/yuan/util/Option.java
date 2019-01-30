package com.alfred.yuan.util;

import com.alfred.yuan.base.Supplier;
import com.alfred.yuan.function.Function;

import java.util.Objects;

/**
 * Created by alfred_yuan on 2019-01-30
 */
public abstract class Option<E> {

    public static <E> Option<E> some(E e) {
        return new Some<>(e);
    }

    @SuppressWarnings(value = "unchecked")
    public static <E> Option<E> none() {
        return none;
    }

    /**
     * takes a function from E to U as its argument and returns a function from Option<E> to Option<U>
     *
     * @param function a function from E to U
     * @param <E>      function argument type
     * @param <U>      function result type
     * @return a function from Option<E> to Option<U>
     */
    public static <E, U> Function<Option<E>, Option<U>> lift(Function<E, U> function) {
        return e -> {
            try {
                return e.map(function);
            } catch (Exception exp) {
                return Option.none();
            }
        };
    }

    /**
     * takes a function from E to U as its argument and returns a function from E to Option<U>
     *
     * @param function a function from E to U
     * @param <E>      function argument type
     * @param <U>      function result type
     * @return a function from E to Option<U>
     */
    public static <E, U> Function<E, Option<U>> hlift(Function<E, U> function) {
        return x -> {
            try {
                return Option.some(x).map(function);
            } catch (Exception e) {
                return Option.none();
            }
        };
    }

    /**
     * taking as its arguments an Option<A>, an Option<B>, and a function from (A, B) to C in curried form, and returning an Option<C>
     *
     * @param e        first argument
     * @param f        second argument
     * @param function the function
     * @param <E>      first argument type
     * @param <F>      second argument type
     * @param <U>      the result type
     * @return U
     */
    public static <E, F, U> Option<U> map2(
        Option<E> e
        , Option<F> f
        , Function<E, Function<F, U>> function) {
        return e.flatMap(ex -> f.map(fx -> function.apply(ex).apply(fx)));
    }

    public static <E, F, G, U> Option<U> map3(
        Option<E> e
        , Option<F> f
        , Option<G> g
        , Function<E, Function<F, Function<G, U>>> function
    ) {
        return e.flatMap(ex -> f.flatMap(fx -> g.map(gx -> function.apply(ex).apply(fx).apply(gx))));
    }

    @SuppressWarnings(value = "rawtypes")
    private static Option none = new None();

    /**
     * get the wrapped value or throw exception
     *
     * @return the wrapped value
     */
    protected abstract E getOrThrow();

    /**
     * return either the contained value if it exists, or a provided default one otherwise.
     *
     * @param defaultValue the lazy default value supplier
     * @return return either the contained value if it exists, or a provided default one otherwise
     */
    public abstract E getOrElse(Supplier<E> defaultValue);

    /**
     * return either the contained value if it exists, or a provided default one otherwise.
     *
     * @param defaultValue the lazy default value supplier
     * @return Option<E>
     */
    public Option<E> orElse(Supplier<Option<E>> defaultValue) {
        return map(x -> this).getOrElse(defaultValue);
    }

    public Option<E> filter(Function<E, Boolean> predictor) {
        return flatMap(x -> predictor.apply(x)
            ? this
            : none());
    }

    /**
     * change an Option<E> into an Option<U> by applying a function from E to U
     *
     * @param operation a function from E to U
     * @param <U>       the result type
     * @return Option<U>
     */
    public abstract <U> Option<U> map(Function<E, U> operation);

    /**
     * takes as an argument a function from E to Option<U> and returns an Option<U>
     *
     * @param operation a function from E to Option<U> and returns an Option<U>
     * @param <U>       the result type
     * @return Option<U>
     */
    public abstract <U> Option<U> flatMap(Function<E, Option<U>> operation);

    private static class None<E> extends Option<E> {
        private None() {}


        @Override
        protected E getOrThrow() {
            throw new IllegalStateException("get called on None");
        }

        @Override
        public E getOrElse(Supplier<E> defaultValue) {
            return defaultValue.get();
        }

        @Override
        public <U> Option<U> map(Function<E, U> operation) {
            return none();
        }

        @Override
        public <U> Option<U> flatMap(Function<E, Option<U>> operation) {
            return none();
        }

        @Override
        public String toString() {
            return "None";
        }

        @Override
        public boolean equals(Object obj) {
            return this == obj || obj instanceof None;
        }

        @Override
        public int hashCode() {
            return 0;
        }
    }

    private static class Some<E> extends Option<E> {
        private final E value;

        private Some(E value) {
            this.value = value;
        }

        @Override
        protected E getOrThrow() {
            return value;
        }

        @Override
        public E getOrElse(Supplier<E> defaultValue) {
            return value;
        }

        @Override
        public <U> Option<U> map(Function<E, U> operation) {
            return some(operation.apply(value));
        }

        @Override
        public <U> Option<U> flatMap(Function<E, Option<U>> operation) {
            return map(operation).getOrElse(Option::none);
        }

        @Override
        public String toString() {
            return String.format("Some(%s)", value);
        }

        @Override
        public boolean equals(Object obj) {
            return (this == obj || obj instanceof Some)
                && this.value.equals(((Some<?>) obj).value);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(this.value);
        }
    }
}
