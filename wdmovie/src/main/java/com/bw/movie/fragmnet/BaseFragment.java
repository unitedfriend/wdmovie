package com.bw.movie.fragmnet;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 基类的Fragment
 */
public abstract class BaseFragment extends Fragment {
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
}
