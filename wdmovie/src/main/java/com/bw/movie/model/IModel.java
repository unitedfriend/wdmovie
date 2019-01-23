package com.bw.movie.model;

import java.util.Map;

/**
 * m层接口
 * guoxinyu
 * */
public interface IModel {
    /**
     * get请求
     * */
    void getRequest(String url,Class clazz,MyCallBack myCallBack);
    /**
     * post请求
     * */
    void postRequest(String url, Map<String,String> map,Class clazz,MyCallBack myCallBack);
}
