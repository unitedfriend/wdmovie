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
import com.bw.movie.my.adaper.ObligationAdaper;
import com.bw.movie.my.adaper.StocksAdaper;
import com.bw.movie.my.bean.ObligationBean;
import com.bw.movie.util.ToastUtil;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class StocksFragment extends BaseFragment {
    @BindView(R.id.xrecycleview)
    XRecyclerView xrecycleview;
    Unbinder unbinder;
    private int mPage;
    private int mCount = 5;
    private int status = 2;
    private StocksAdaper stocksAdaper;

    @Override
    protected void initData() {
        doNetWorkGetRequest(String.format(Apis.URL_FIND_USER_BUY_TICLET_RECORD_LIST_GET, mPage, mCount, status), ObligationBean.class);
    }

    @Override
    protected void initView(View view) {
        mPage = 1;
        unbinder = ButterKnife.bind(this, view);
        //创建布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        xrecycleview.setLayoutManager(layoutManager);
        //创建适配器
        stocksAdaper = new StocksAdaper(getActivity());
        xrecycleview.setAdapter(stocksAdaper);
        //设置支持刷新加载
        xrecycleview.setPullRefreshEnabled(true);
        xrecycleview.setLoadingMoreEnabled(true);
        xrecycleview.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                mPage=1;
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
        return R.layout.stocks_fragment;
    }

    @Override
    protected void netSuccess(Object object) {
        if(object instanceof ObligationBean){
            ObligationBean obligationBean = (ObligationBean) object;
            if(obligationBean == null || !obligationBean.isSuccess()){
                ToastUtil.showToast(obligationBean.getMessage());
            }else{
                if(mPage==1){
                    stocksAdaper.setmResult(obligationBean.getResult());
                }else{
                    stocksAdaper.addmResult(obligationBean.getResult());
                }
                mPage++;
                xrecycleview.loadMoreComplete();
                xrecycleview.refreshComplete();
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
