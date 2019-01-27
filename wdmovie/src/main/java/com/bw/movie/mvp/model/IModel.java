package com.bw.movie.mvp.model;

import java.util.Map;

/**
  * @作者 GXY
  * @创建日期 2019/1/23 18:40
  * @描述 m层接口
  *
  */
public interface IModel {
    /**
     * get请求
     * */
    void getRequest(String url,Class clazz,MyCallBack myCallBack);
    /**
     * post请求
     * */
    void postRequest(String url, Map<String,String> map,Class clazz,MyCallBack myCallBack);
    /**
     * 上传头像
     * */
    void ImagePostrRequest(String url,Map<String,String> map,Class clazz,MyCallBack myCallBack);

}
