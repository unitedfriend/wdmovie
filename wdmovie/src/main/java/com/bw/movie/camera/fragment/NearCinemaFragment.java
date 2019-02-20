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
import com.bw.movie.camera.adaper.NeardAdaper;
import com.bw.movie.camera.adaper.RecommendAdaper;
import com.bw.movie.camera.bean.CancelFollowCineamBean;
import com.bw.movie.camera.bean.FollowCinemaBean;
import com.bw.movie.camera.bean.NearBean;
import com.bw.movie.camera.bean.RecommendBean;
import com.bw.movie.fragmnet.BaseFragment;
import com.bw.movie.login.activity.LoginActivity;
import com.bw.movie.util.ToastUtil;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @作者 GXY
 * @创建日期 2019/1/30 19:59
 * @描述 附近影院Fragment
 */
public class NearCinemaFragment extends BaseFragment {
    @BindView(R.id.xrecycleview)
    XRecyclerView xrecycleview;
    Unbinder unbinder;
    private int mPage;
    private int mCount = 5;
    /*private String longitude = "116.30551391385724";
    private String latitude = "40.04571807462411";*/
    private NeardAdaper neardAdaper;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor edit;

    @Override
    protected void initData() {
        sharedPreferences = getActivity().getSharedPreferences("Location", Context.MODE_PRIVATE);
        edit = sharedPreferences.edit();
        String latitude = sharedPreferences.getString("latitude", null);
        String longitude = sharedPreferences.getString("longitude", null);
        if(latitude!=null && longitude!=null){
            doNetWorkGetRequest(String.format(Apis.URL_FIND_NEAR_BY_CINEMAS_GET,longitude,latitude,mPage,mCount),NearBean.class);
        }
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
        neardAdaper = new NeardAdaper(getActivity());
        xrecycleview.setAdapter(neardAdaper);
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
        final SharedPreferences user = getActivity().getSharedPreferences("User", Context.MODE_PRIVATE);
       neardAdaper.setCallBackNear(new NeardAdaper.CallBackNear() {
           @Override
           public void callTrueBeak(int id, boolean b, int position) {

               String userId = user.getString("userId", null);
               if(userId==null){
                   startActivity(new Intent(getActivity(),LoginActivity.class));
                   neardAdaper.setIsOk(!b);
                   return;
               }
               doNetWorkGetRequest(String.format(Apis.URL_FOLLOW_CINEAM_GET,id),FollowCinemaBean.class);
           }

           @Override
           public void callFalseBeak(int id, boolean b, int position) {

               String userId = user.getString("userId", null);
               if(userId==null){
                   startActivity(new Intent(getActivity(),LoginActivity.class));
                   neardAdaper.setIsOk(!b);
                   return;
               }
               doNetWorkGetRequest(String.format(Apis.URL_CANCEL_FOLLOW_CINEAM_GET,id),CancelFollowCineamBean.class);
           }
       });
        //跳转到排期
        neardAdaper.setCallBackList(new NeardAdaper.CallBackList() {
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
        return R.layout.near_cinema_fragment;
    }

    @Override
    protected void netSuccess(Object object) {
        if (object instanceof NearBean) {
            NearBean nearBean = (NearBean) object;
            if (nearBean == null || !nearBean.isSuccess()) {
                ToastUtil.showToast(nearBean.getMessage());
            } else {
                if (mPage == 1) {
                    neardAdaper.setmResult(nearBean.getResult());
                } else {
                    neardAdaper.addmResult(nearBean.getResult());
                }
                mPage++;
                xrecycleview.loadMoreComplete();
                xrecycleview.refreshComplete();
            }
        } else if (object instanceof FollowCinemaBean) {
            FollowCinemaBean followCinemaBean = (FollowCinemaBean) object;
            ToastUtil.showToast(followCinemaBean.getMessage());
        } else if (object instanceof CancelFollowCineamBean) {
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
