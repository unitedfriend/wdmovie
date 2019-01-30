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
import com.bw.movie.my.adaper.AttentionMovieAdaper;
import com.bw.movie.my.bean.AttentionMovieBean;
import com.bw.movie.util.ToastUtil;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @作者 GXY
 * @创建日期 2019/1/28 8:22
 * @描述 我的关注里的电影页面
 */
public class AttentionMovieFragment extends BaseFragment {
    @BindView(R.id.movierecycleview)
    XRecyclerView movierecycleview;
    Unbinder unbinder;
    private int mPage;
    private int mCount = 5;
    private AttentionMovieAdaper movieAdaper;

    @Override
    protected void initData() {
        doNetWorkGetRequest(String.format(Apis.URL_FIND_MOVIE_PAGE_LIST_GET, mPage, mCount), AttentionMovieBean.class);
    }

    @Override
    protected void initView(View view) {
        mPage = 1;
        unbinder = ButterKnife.bind(this, view);
        //创建布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        movierecycleview.setLayoutManager(layoutManager);
        //创建适配器
        movieAdaper = new AttentionMovieAdaper(getActivity());
        movierecycleview.setAdapter(movieAdaper);
        //设置支持刷新加载
        movierecycleview.setLoadingMoreEnabled(true);
        movierecycleview.setPullRefreshEnabled(true);
        movierecycleview.setLoadingListener(new XRecyclerView.LoadingListener() {
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
        return R.layout.attention_movie_fragment;
    }

    @Override
    protected void netSuccess(Object object) {
        if(object instanceof AttentionMovieBean){
            AttentionMovieBean movieBean = (AttentionMovieBean) object;
            if(movieBean==null || !movieBean.isSuccess()){
                ToastUtil.showToast(movieBean.getMessage());
            }else{
                if(mPage == 1){
                    movieAdaper.setmResult(movieBean.getResult());
                }else{
                    movieAdaper.setmResult(movieBean.getResult());
                }
                mPage++;
                movierecycleview.refreshComplete();
                movierecycleview.loadMoreComplete();
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
