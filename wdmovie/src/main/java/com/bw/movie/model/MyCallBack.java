package com.bw.movie.model;

public interface MyCallBack<E> {
    /**
     * 成功
     * */
    void onSuccess(E data);
    /**
     * 失败
     * */
    void onFail(String error);
}
