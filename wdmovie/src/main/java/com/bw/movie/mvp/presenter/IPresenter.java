package com.bw.movie.mvp.presenter;

import java.util.Map;

/**
  * @作者 GXY
  * @创建日期 2019/1/23 18:41
  * @描述 p层接口
  *
  */

public interface IPresenter {
    /**
     * get请求
     * */
    void requestGet(String url,Class clazz);
    /**
     * post请求
     * */
    void requestPost(String url,Map<String,String> map,Class clazz);
    /***
     * 上传头像
     */
    void imageRequestPost(String url,Map<String,String> map,Class clazz);

}
