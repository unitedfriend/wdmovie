package com.bw.movie.home.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bw.movie.R;
import com.bw.movie.fragmnet.BaseFragment;
import com.bw.movie.home.adapter.FilmRecycleAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FilmFragment extends BaseFragment {
    @BindView(R.id.home_recycle)
    RecyclerView homeRecycle;
    Unbinder unbinder;

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(View view) {
        unbinder = ButterKnife.bind(this, view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        homeRecycle.setLayoutManager(layoutManager);
        FilmRecycleAdapter adapter = new FilmRecycleAdapter(getActivity());
        homeRecycle.setAdapter(adapter);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_film;
    }

    @Override
    protected void netSuccess(Object object) {

    }

    @Override
    protected void netFail(String s) {

    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
