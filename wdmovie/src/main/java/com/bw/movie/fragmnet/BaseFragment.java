package com.bw.movie.fragmnet;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.bw.movie.R;
import com.bw.movie.finals.BaseFinal;
import com.bw.movie.mvp.presenter.PresenterImpl;
import com.bw.movie.mvp.view.IView;
import com.bw.movie.util.CircularLoading;
import com.bw.movie.util.NetUtil;
import com.bw.movie.util.ToastUtil;

import java.util.Map;

/**
 * 基类的Fragment
 */
public abstract class BaseFragment extends Fragment implements IView {

    private PresenterImpl presenter;
    private Dialog mCircularLoading;
    private AlertDialog alertDialog;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getLayoutResId(),container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new PresenterImpl(this);
        //初始化view
        initView(view);
        //初始化数据
        initData();

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
            if(mCircularLoading==null){
                mCircularLoading = CircularLoading.showLoadDialog(getActivity(), "加载中...", true);
            }

            presenter.requestPost(url,map,clazz);
        }
    }
    /**
     * get请求
     * */
    protected void doNetWorkGetRequest(String url,Class clazz){
        if(presenter!=null){
            if(mCircularLoading==null){
                mCircularLoading = CircularLoading.showLoadDialog(getActivity(), "加载中...", true);
            }

            presenter.requestGet(url,clazz);
        }
    }
    /**
     * v层请求成功的方法
     * */
    @Override
    public void requestSuccess(Object o) {
        CircularLoading.closeDialog(mCircularLoading);
        mCircularLoading=null;
        netSuccess(o);
    }
    /**
     * v层请求失败的方法
     * */
    @Override
    public void requestFail(String error) {
        CircularLoading.closeDialog(mCircularLoading);
        if(error.equals(BaseFinal.NET_WORK)){
            ToastUtil.showToast(getResources().getString(R.string.no_net_work));
            if(alertDialog==null){
                alertDialog = NetUtil.showNotNetWork(getActivity());
            }else{
                alertDialog.dismiss();
                alertDialog=null;
                alertDialog=NetUtil.showNotNetWork(getActivity());
            }
        }
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
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }
}
