package com.bw.movie.mvp.model;

import com.bw.movie.finals.BaseFinal;
import com.bw.movie.network.RetrofitManager;
import com.bw.movie.util.NetUtil;
import com.google.gson.Gson;
import java.util.Map;
/**
  * @作者 GXY
  * @创建日期 2019/1/23 18:39
  * @描述 m层实现类
  */
public class ModelImpl implements IModel {
    /**
     * get请求
     * */
    @Override
    public void getRequest(String url, final Class clazz, final MyCallBack myCallBack) {
        if(!NetUtil.hasNetWork()){
            myCallBack.onFail(BaseFinal.NET_WORK);
        }else{
            RetrofitManager.getInstance().get(url, new RetrofitManager.HttpListener() {
                @Override
                public void onSuccess(String data) {
                    try {
                        Object o = new Gson().fromJson(data, clazz);
                        if(myCallBack!=null){
                            myCallBack.onSuccess(o);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        if(myCallBack!=null){
                            myCallBack.onFail(e.getMessage());
                        }
                    }
                }

                @Override
                public void onFail(String error) {
                    if(myCallBack!=null){
                        myCallBack.onFail(error);
                    }
                }
            });
        }
    }
    /**
     * post请求
     * */
    @Override
    public void postRequest(String url, Map<String, String> map, final Class clazz, final MyCallBack myCallBack) {
        if(!NetUtil.hasNetWork()){
            myCallBack.onFail(BaseFinal.NET_WORK);
        }else{
            RetrofitManager.getInstance().post(url, map, new RetrofitManager.HttpListener() {
                @Override
                public void onSuccess(String data) {
                    try {
                        Object o = new Gson().fromJson(data, clazz);
                        if(myCallBack!=null){
                            myCallBack.onSuccess(o);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        if(myCallBack!=null){
                            myCallBack.onFail(e.getMessage());
                        }
                    }
                }
                @Override
                public void onFail(String error) {
                    if(myCallBack!=null){
                        myCallBack.onFail(error);
                    }
                }
            });
        }
    }
    /**
     * 上传头像
     * */
    @Override
    public void ImagePostrRequest(String url, Map<String, String> map, final Class clazz, final MyCallBack myCallBack) {
        if(!NetUtil.hasNetWork()){
            myCallBack.onFail(BaseFinal.NET_WORK);
        }else{
            RetrofitManager.getInstance().imagePost(url, map, new RetrofitManager.HttpListener() {
                @Override
                public void onSuccess(String data) {
                    try {
                        Object o = new Gson().fromJson(data, clazz);
                        if(myCallBack!=null){
                            myCallBack.onSuccess(o);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        if(myCallBack!=null){
                            myCallBack.onFail(e.getMessage());
                        }
                    }
                }
                @Override
                public void onFail(String error) {
                    if(myCallBack!=null){
                        myCallBack.onFail(error);
                    }
                }
            });
        }
    }
}
