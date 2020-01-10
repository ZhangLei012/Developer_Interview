package com.github.keyboard3.developerinterview.callback;



public interface Callback<T> {
    void success(T item);
    void fail(Throwable error);
}
