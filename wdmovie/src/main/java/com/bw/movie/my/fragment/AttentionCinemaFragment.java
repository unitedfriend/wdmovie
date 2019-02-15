package com.bw.movie.my.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bw.movie.R;
import com.bw.movie.api.Apis;
import com.bw.movie.fragmnet.BaseFragment;
import com.bw.movie.my.adaper.AttentionCinemaAdaper;
import com.bw.movie.my.bean.AttentionCinemaBean;
import com.bw.movie.util.ToastUtil;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
/**
  * @作者 GXY
  * @创建日期 2019/2/14 14:39
  * @描述 关注影院
  *
  */
public class AttentionCinemaFragment extends BaseFragment {
    @BindView(R.id.cinemarecycleview)
    XRecyclerView cinemarecycleview;
    Unbinder unbinder;
    private int mPage;
    private int mCount = 5;
    private AttentionCinemaAdaper attentionCinemaAdaper;

    @Override
    protected void initData() {
        doNetWorkGetRequest(String.format(Apis.URL_FIND_CINEAM_PAGE_LIST_GET,mPage,mCount),AttentionCinemaBean.class);
    }

    @Override
    protected void initView(View view) {
        mPage = 1;
        unbinder = ButterKnife.bind(this, view);
        //创建布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        cinemarecycleview.setLayoutManager(layoutManager);
        //创建适配器
        attentionCinemaAdaper = new AttentionCinemaAdaper(getActivity());
        cinemarecycleview.setAdapter(attentionCinemaAdaper);
        cinemarecycleview.setLoadingMoreEnabled(true);
        cinemarecycleview.setPullRefreshEnabled(true);
        cinemarecycleview.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                mPage = 1;
                initData();
            }

            @Override
            public void onLoadMore() {
                initData();
            }
        });
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.attention_cinema_fragment;
    }

    @Override
    protected void netSuccess(Object object) {
        if(object instanceof AttentionCinemaBean){
            AttentionCinemaBean cinemaBean = (AttentionCinemaBean) object;
            if(cinemaBean == null || !cinemaBean.isSuccess()){
                ToastUtil.showToast(cinemaBean.getMessage());
            }else{
                if(mPage == 1){
                    attentionCinemaAdaper.setmRsult(cinemaBean.getResult());
                }else{
                    attentionCinemaAdaper.addmResult(cinemaBean.getResult());
                }
                mPage++;
                cinemarecycleview.refreshComplete();
                cinemarecycleview.loadMoreComplete();
            }
        }
    }

    @Override
    protected void netFail(String s) {

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
