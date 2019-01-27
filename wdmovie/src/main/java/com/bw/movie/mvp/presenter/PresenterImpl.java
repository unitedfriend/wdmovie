package com.bw.movie.mvp.presenter;

import com.bw.movie.mvp.model.ModelImpl;
import com.bw.movie.mvp.model.MyCallBack;
import com.bw.movie.mvp.view.IView;

import java.util.Map;
/**
  * @作者 GXY
  * @创建日期 2019/1/23 18:42
  * @描述 p层实现类
  */

public class PresenterImpl implements IPresenter {
    private IView mIView;
    private ModelImpl model;

    public PresenterImpl(IView iView) {
        mIView = iView;
        model = new ModelImpl();
    }

    /**
     * get请求
     * */
    @Override
    public void requestGet(String url, Class clazz) {
        model.getRequest(url, clazz, new MyCallBack() {
            @Override
            public void onSuccess(Object data) {
                mIView.requestSuccess(data);
            }
            @Override
            public void onFail(String error) {
                mIView.requestFail(error);
            }
        });
    }
    /**
     * post请求
     * */
    @Override
    public void requestPost(String url, Map<String, String> map, Class clazz) {
        model.postRequest(url, map, clazz, new MyCallBack() {
            @Override
            public void onSuccess(Object data) {
                mIView.requestSuccess(data);
            }

            @Override
            public void onFail(String error) {
                mIView.requestFail(error);
            }
        });
    }
        /**
         * 上传头像
         * */
    @Override
    public void imageRequestPost(String url, Map<String, String> map, Class clazz) {
        model.ImagePostrRequest(url, map, clazz, new MyCallBack() {
            @Override
            public void onSuccess(Object data) {
                mIView.requestSuccess(data);
            }

            @Override
            public void onFail(String error) {
                mIView.requestFail(error);
            }
        });
    }


    public void onDetach(){
        if(mIView!=null){
            mIView = null;
        }
        if(model!=null){
            model = null;
        }
    }
}
