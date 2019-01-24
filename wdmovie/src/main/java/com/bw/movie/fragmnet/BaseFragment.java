package com.bw.movie.fragmnet;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bw.movie.mvp.presenter.PresenterImpl;
import com.bw.movie.mvp.view.IView;
import com.bw.movie.util.CircularLoading;

import java.util.Map;

/**
 * 基类的Fragment
 */
public abstract class BaseFragment extends Fragment implements IView {

    private PresenterImpl presenter;
    private Dialog mCircularLoading;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getLayoutResId(),container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //初始化view
        initView(view);
        //初始化数据
        initData();
        presenter = new PresenterImpl(this);
    }
    /**
     * 初始化数据
     * */
    protected abstract void initData();
    /**
     * 初始化view
     * */
    protected abstract void initView(View view);

    /**
     * 加载布局
     * */
    protected abstract int getLayoutResId();
    /**
     * post请求
     * */
    protected void doNetWorkPostRequest(String url, Map<String,String> map, Class clazz){
        if(presenter!=null){
            mCircularLoading = CircularLoading.showLoadDialog(getActivity(), "加载中...", true);
            presenter.requestPost(url,map,clazz);
        }
    }
    /**
     * get请求
     * */
    protected void doNetWorkGetRequest(String url,Class clazz){
        if(presenter!=null){
            mCircularLoading = CircularLoading.showLoadDialog(getActivity(), "加载中...", true);
            presenter.requestGet(url,clazz);
        }
    }
    /**
     * v层请求成功的方法
     * */
    @Override
    public void requestSuccess(Object o) {
        CircularLoading.closeDialog(mCircularLoading);
        netSuccess(o);
    }
    /**
     * v层请求失败的方法
     * */
    @Override
    public void requestFail(String error) {
        CircularLoading.closeDialog(mCircularLoading);
        netFail(error);
    }
    /**
     * 成功
     * */
    protected abstract void netSuccess(Object object);
    /**
     * 失败
     * */
    protected abstract void netFail(String s);

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDetach();
    }
}
