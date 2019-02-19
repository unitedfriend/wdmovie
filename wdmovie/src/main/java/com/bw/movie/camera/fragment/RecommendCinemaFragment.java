package com.bw.movie.camera.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bw.movie.R;
import com.bw.movie.api.Apis;
import com.bw.movie.camera.activity.CinemaDateListActivity;
import com.bw.movie.camera.adaper.RecommendAdaper;
import com.bw.movie.camera.bean.CancelFollowCineamBean;
import com.bw.movie.camera.bean.FollowCinemaBean;
import com.bw.movie.camera.bean.RecommendBean;
import com.bw.movie.fragmnet.BaseFragment;
import com.bw.movie.home.bean.AttentionBean;
import com.bw.movie.login.activity.LoginActivity;
import com.bw.movie.util.ToastUtil;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @作者 GXY
 * @创建日期 2019/1/29 10:33
 * @描述 推荐影院
 */
public class RecommendCinemaFragment extends BaseFragment {
    @BindView(R.id.xrecycleview)
    XRecyclerView xrecycleview;
    Unbinder unbinder;
    private int mPage;
    private int mCount = 5;
    private RecommendAdaper recommendAdaper;
    @Override
    protected void initData() {
        doNetWorkGetRequest(String.format(Apis.URL_FIND_RECOMMEND_CINEMAS_GET,mPage,mCount),RecommendBean.class);
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
        recommendAdaper = new RecommendAdaper(getActivity());
        xrecycleview.setAdapter(recommendAdaper);
        //设置支持刷新和加载
        xrecycleview.setLoadingMoreEnabled(true);
        xrecycleview.setPullRefreshEnabled(true);
        xrecycleview.setLoadingListener(new XRecyclerView.LoadingListener() {
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
        //关注
        recommendAdaper.setCallBackRecommend(new RecommendAdaper.CallBackRecommend() {
            @Override
            public void callBeak(int id, boolean b, int position) {
                SharedPreferences user = getActivity().getSharedPreferences("User", Context.MODE_PRIVATE);
                String userId = user.getString("userId", null);
                if(userId==null){
                    startActivity(new Intent(getActivity(),LoginActivity.class));
                    recommendAdaper.setIsOk(!b);
                    return;
                }
                if(b){
                    doNetWorkGetRequest(String.format(Apis.URL_FOLLOW_CINEAM_GET,id),FollowCinemaBean.class);
                }else {
                    doNetWorkGetRequest(String.format(Apis.URL_CANCEL_FOLLOW_CINEAM_GET,id),CancelFollowCineamBean.class);
                }
            }
        });
        //跳转到排期
       recommendAdaper.setCallBackList(new RecommendAdaper.CallBackList() {
           @Override
           public void callBack(int id) {
               Intent intent = new Intent(getActivity(),CinemaDateListActivity.class);
               intent.putExtra("id",id);
               startActivity(intent);
           }
       });
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.recommend_cinema_fragment;
    }

    @Override
    protected void netSuccess(Object object) {
        if(object instanceof RecommendBean){
            RecommendBean recommendBean = (RecommendBean) object;
            if(recommendBean == null || !recommendBean.isSuccess()){
                ToastUtil.showToast(recommendBean.getMessage());
            }else{
                if(mPage == 1){
                    recommendAdaper.setmResult(recommendBean.getResult());
                }else{
                    recommendAdaper.addmResult(recommendBean.getResult());
                }
                mPage++;
                xrecycleview.loadMoreComplete();
                xrecycleview.refreshComplete();
            }
        }else if(object instanceof FollowCinemaBean){
            FollowCinemaBean followCinemaBean = (FollowCinemaBean) object;
            ToastUtil.showToast(followCinemaBean.getMessage());
            if(followCinemaBean.getMessage().equals("请先登陆")){
                ToastUtil.showToast("请先登陆");
                startActivity(new Intent(getActivity(),LoginActivity.class));
            }
        }else if(object instanceof CancelFollowCineamBean){
            CancelFollowCineamBean cancelFollowCineamBean = (CancelFollowCineamBean) object;
            ToastUtil.showToast(cancelFollowCineamBean.getMessage());
        }

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
