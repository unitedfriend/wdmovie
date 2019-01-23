package com.bw.movie.presenter;

import java.util.Map;

/**
 * p层接口
 * guoxinyu
 * */
public interface IPresenter {
    /**
     * get请求
     * */
    void requestGet(String url,Class clazz);
    /**
     * post请求
     * */
    void requestPost(String url,Map<String,String> map,Class clazz);
}
