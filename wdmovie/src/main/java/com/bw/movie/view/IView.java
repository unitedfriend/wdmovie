package com.bw.movie.view;

public interface IView<E> {
    void requestSuccess(E e);
    void requestFail(String error);
}
