package com.bw.movie.mvp.model;
/**
  * @作者 GXY
  * @创建日期 2019/1/23 18:41
  * @描述 向p层传递数据的接口
  *
  */
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
