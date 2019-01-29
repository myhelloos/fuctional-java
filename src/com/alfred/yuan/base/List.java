package com.alfred.yuan.base;

import com.alfred.yuan.corecursive.TailCall;
import com.alfred.yuan.function.Function;

/**
 * Created by alfred_yuan on 2019-01-29
 */
public abstract class List<E> {
    /**
     * get head of list
     *
     * @return head of list
     */
    public abstract E head();

    /**
     * get tail of list
     *
     * @return tail of list
     */
    public abstract List<E> tail();

    /**
     * is list empty?
     *
     * @return is list empty
     */
    public abstract boolean isEmpty();

    /**
     * adding an element at the beginning of a list
     *
     * @param head the new header
     * @return the list with an more new header`
     */
    public abstract List<E> cons(E head);

    /**
     * replacing the first element of a List with a new value
     *
     * @param e the new value
     * @return the list with replaced header
     */
    public abstract List<E> setHead(E e);

    /**
     * removes the first n elements from a list
     *
     * @param n the n elements
     * @return the list removed first n elements
     */
    public abstract List<E> drop(int n);

    /**
     * remove elements from the head of the List as long as a condition holds true
     *
     * @param predictor the condition function accept the header
     * @return header removed list
     */
    public abstract List<E> dropWhile(Function<E, Boolean> predictor);

    /**
     * reverse it
     *
     * @return a list with the elements in reverse order
     */
    public abstract List<E> reverse();

    /**
     * remove the last element from a list
     *
     * @return the resulting list without last element
     */
    public abstract List<E> init();

    /**
     * compute the length of a list
     *
     * @return the length
     */
    public abstract int size();

    /**
     * fold from the left side
     *
     * @param identity  the default result if empty
     * @param operation operation applied to get the final result
     * @param <U>       the result type
     * @return the result
     */
    public abstract <U> U foldLeft(U identity, Function<U, Function<E, U>> operation);

    private List() {
    }

    @SuppressWarnings(value = "unchecked")
    public static final List NIL = new Nil();

    @SuppressWarnings(value = "rawtypes")
    public static <E> List<E> list() {
        return NIL;
    }

    @SafeVarargs
    public static <E> List<E> list(E... es) {
        List<E> list = list();
        for (int i = es.length - 1; i >= 0; --i) {
            list = new Cons<>(es[i], list);
        }
        return list;
    }

    public static <E> List<E> setHead(List<E> list, E head) {
        return list.setHead(head);
    }

    public static <E> List<E> drop(List<E> list, int n) {
        return list.drop(n);
    }

    public static <E> List<E> dropWhile(List<E> list, Function<E, Boolean> predictor) {
        return list.dropWhile(predictor);
    }

    public static <E> List<E> concat(List<E> first, List<E> second) {
        return first.isEmpty()
            ? second
            : new Cons<>(first.head(), concat(first.tail(), second));
    }

    public static <E, U> U foldRight(List<E> list, U identity, Function<E, Function<U, U>> operator) {
        return list.isEmpty()
            ? identity
            : operator.apply(list.head()).apply(foldRight(list.tail(), identity, operator));
    }

    public static <E, U> U foldRightViaFlodLeft(List<E> list, U identity, Function<E, Function<U, U>> operator) {
        return list.reverse().foldLeft(identity, x -> y -> operator.apply(y).apply(x));
    }

    public static <E> List<E> reverse(List<E> list) {
        return list.foldLeft(list(), acc -> acc::cons);
    }

    private static class Nil<E> extends List<E> {

        @Override
        public E head() {
            throw new IllegalStateException("head called on empty list");
        }

        @Override
        public List<E> tail() {
            throw new IllegalStateException("tail called on empty list");
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public List<E> cons(E e) {
            return new Cons<>(e, this);
        }

        @Override
        public List<E> setHead(E head) {
            throw new IllegalStateException("setHead called on an empty list");
        }

        @Override
        public List<E> drop(int n) {
            return this;
        }

        @Override
        public List<E> dropWhile(Function<E, Boolean> predictor) {
            return this;
        }

        @Override
        public List<E> reverse() {
            return this;
        }

        @Override
        public List<E> init() {
            throw new IllegalStateException("init called on an empty list");
        }

        @Override
        public int size() {
            return 0;
        }

        @Override
        public <U> U foldLeft(U identity, Function<U, Function<E, U>> operation) {
            return identity;
        }

        @Override
        public String toString() {
            return "[NIL]";
        }
    }

    private static class Cons<E> extends List<E> {

        private final E head;
        private final List<E> tail;

        public Cons(E head, List<E> tail) {
            this.head = head;
            this.tail = tail;
        }

        @Override
        public E head() {
            return head;
        }

        @Override
        public List<E> tail() {
            return tail;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public List<E> cons(E e) {
            return new Cons<>(e, this);
        }

        @Override
        public List<E> setHead(E head) {
            return new Cons<>(head, this.tail());
        }

        @Override
        public List<E> drop(int n) {
            return n <= 0
                ? this
                : drop_(this, n).eval();
        }

        @Override
        public List<E> dropWhile(Function<E, Boolean> predictor) {
            return dropWhile_(this, predictor).eval();
        }

        @Override
        public List<E> reverse() {
            return reverse_(list(), this).eval();
        }

        @Override
        public List<E> init() {
            return reverse().tail().reverse();
        }

        @Override
        public int size() {
            return foldLeft(0, size -> __ -> size + 1);
        }

        @Override
        public <U> U foldLeft(U identity, Function<U, Function<E, U>> operation) {
            return foldLeft_(identity, this, operation).eval();
        }

        @Override
        public String toString() {
            return String.format(
                "[%sNIL]"
                , toString_(new StringBuilder(), this).eval());
        }

        private TailCall<List<E>> drop_(List<E> acc, int n) {
            return n <= 0 || acc.isEmpty()
                ? TailCall.ret(acc)
                : TailCall.sus((() -> drop_(acc.tail(), n - 1)));
        }

        private TailCall<List<E>> dropWhile_(List<E> acc, Function<E, Boolean> predictor) {
            return !acc.isEmpty() && predictor.apply(acc.head())
                ? TailCall.sus(() -> dropWhile_(acc.tail(), predictor))
                : TailCall.ret(acc);
        }

        private TailCall<List<E>> reverse_(List<E> acc, List<E> list) {
            return list.isEmpty()
                ? TailCall.ret(acc)
                : TailCall.sus(() -> reverse_(new Cons<>(list.head(), acc), list.tail()));
        }

        private <U> TailCall<U> foldLeft_(U acc, List<E> list, Function<U, Function<E, U>> operation) {
            return list.isEmpty()
                ? TailCall.ret(acc)
                : TailCall.sus(() -> foldLeft_(
                    operation.apply(acc).apply(list.head())
                    , list.tail()
                    , operation));
        }

        private TailCall<StringBuilder> toString_(StringBuilder acc, List<E> list) {
            return list.isEmpty()
                ? TailCall.ret(acc)
                : TailCall.sus(() -> toString_(
                    acc.append(list.head()).append(", ")
                    , list.tail()));
        }
    }
}
