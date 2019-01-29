package com.alfred.yuan.base;

/**
 * Created by alfred_yuan on 2019-01-29
 */
public abstract class List<E> {
    public abstract E head();

    public abstract List<E> tail();

    public abstract boolean isEmpty();

    private List() {
    }

    @SuppressWarnings("unchecked")
    public static final List NIL = new Nil();

    @SuppressWarnings("rawtypes")
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
    }
}
