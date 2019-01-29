package com.alfred.yuan.base;

/**
 * Created by alfred_yuan on 2019-01-27
 */
public interface Result<T> {
    void bind(Effect<T> success, Effect<String> failure);

    static <T> Result<T> failure(String message) {
        return new Failure<>(message);
    }

    static <T> Result<T> success(T value) {
        return new Success<>(value);
    }

    class Success<T> implements Result<T> {
        private final T value;

        public Success(T value) {
            this.value = value;
        }

        @Override
        public void bind(Effect<T> success, Effect<String> failure) {
            success.apply(value);
        }
    }

    class Failure<T> implements Result<T> {
        private final String errorMessage;

        public Failure(String s) {
            this.errorMessage = s;
        }

        public String getMessage() {
            return this.errorMessage;
        }

        @Override
        public void bind(Effect<T> success, Effect<String> failure) {
            failure.apply(errorMessage);
        }
    }
}
