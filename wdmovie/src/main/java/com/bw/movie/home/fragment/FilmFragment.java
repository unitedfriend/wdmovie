package com.bw.movie.home.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bw.movie.R;
import com.bw.movie.api.Apis;
import com.bw.movie.fragmnet.BaseFragment;
import com.bw.movie.home.activity.FilmDetailsActivity;
import com.bw.movie.home.activity.MovieListActivity;
import com.bw.movie.home.adapter.FilmRecycleAdapter;
import com.bw.movie.home.bean.AllBean;
import com.bw.movie.home.bean.HotBean;
import com.bw.movie.home.bean.ShowBean;
import com.bw.movie.home.bean.ShowingBean;
import com.bw.movie.util.ToastUtil;
import com.zaaach.citypicker.CityPicker;
import com.zaaach.citypicker.adapter.OnPickListener;
import com.zaaach.citypicker.model.City;
import com.zaaach.citypicker.model.HotCity;
import com.zaaach.citypicker.model.LocateState;
import com.zaaach.citypicker.model.LocatedCity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;


/**
 *  @author Tang
 *  @time 2019/1/28  9:12
 *  @describe 电影首页的电影页面
 */
public class FilmFragment extends BaseFragment {
    @BindView(R.id.home_recycle)
    RecyclerView homeRecycle;
    Unbinder unbinder;
    private final int PAGE=1;
    private final int COUNT=10;
    private AllBean allBean;
private int tag;
    private FilmRecycleAdapter adapter;

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(View view) {
        unbinder = ButterKnife.bind(this, view);
        tag=0;
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        homeRecycle.setLayoutManager(layoutManager);
        adapter = new FilmRecycleAdapter(getActivity());
        homeRecycle.setAdapter(adapter);
        //创建一个Bean得到所有网络请求的数据
        allBean = new AllBean();
        doNetWorkGetRequest(String.format(Apis.URL_FIND_HOT_MOVIE_LIST_GET,PAGE,COUNT),HotBean.class);
        doNetWorkGetRequest(String.format(Apis.URL_FIND_RELEASE_MOVIE_LIST_GET,PAGE,COUNT),ShowingBean.class);
        doNetWorkGetRequest(String.format(Apis.URL_FIND_COMING_SOON_MOVIE_LIST_GET,PAGE,COUNT),ShowBean.class);

        adapter.setFilmCallBack(new FilmRecycleAdapter.FilmCallBack() {
            @Override
            public void addressCallBack() {



                //地址点击事件

                List<HotCity> hotCities = new ArrayList<>();
                hotCities.add(new HotCity("北京", "北京", "101010100")); //code为城市代码
                hotCities.add(new HotCity("上海", "上海", "101020100"));
                hotCities.add(new HotCity("广州", "广东", "101280101"));
                hotCities.add(new HotCity("深圳", "广东", "101280601"));
                hotCities.add(new HotCity("杭州", "浙江", "101210101"));
                CityPicker.from(getActivity()) //activity或者fragment
                        .enableAnimation(false)	//启用动画效果，默认无
                        //自定义动画
                        .setLocatedCity(new LocatedCity("杭州", "浙江", "101210101")).setHotCities(hotCities)  //APP自身已定位的城市，传null会自动定位（默认）
  	                    //指定热门城市
                        .setOnPickListener(new OnPickListener() {
                            @Override
                            public void onPick(int position, City data) {
                                adapter.setLocation(data.getName());
                            }

                            @Override
                            public void onCancel(){

                            }

                            @Override
                            public void onLocate() {
                                //定位接口，需要APP自身实现，这里模拟一下定位
                                ToastUtil.showToast("启动");
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        CityPicker.from(getActivity()).locateComplete(new LocatedCity("深圳", "广东", "101280601"), LocateState.SUCCESS);
                                    }
                                }, 3000);
                            }
                        })
                        .show();


            }

            @Override
            public void searchCallBack(String s) {
                //搜索按钮击事件
              //  startActivity(new Intent(getActivity(),MovieListActivity.class).putExtra("type","search").putExtra("name",s));
            }

            @Override
            public void bannerCallBack(String id) {
                //轮播图图片点击事件
                startActivity(new Intent(getActivity(),FilmDetailsActivity.class).putExtra("id",id));
            }

            @Override
            public void hotSkipGroup() {
                //热门电影点击事件
                startActivity(new Intent(getActivity(),MovieListActivity.class).putExtra("type","hot"));
            }

            @Override
            public void showingSkipGroup() {
                //正在上映点击事件
                startActivity(new Intent(getActivity(),MovieListActivity.class).putExtra("type","showing"));
            }

            @Override
            public void showSkipGroup() {
                //即将上映点击事件
                startActivity(new Intent(getActivity(),MovieListActivity.class).putExtra("type","show"));
            }

            @Override
            public void detailsCallBack(String id) {
                //点击影片展示详情
                startActivity(new Intent(getActivity(),FilmDetailsActivity.class).putExtra("id",id));
            }
        });
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_film;
    }

    @Override
    protected void netSuccess(Object object) {
        if(object instanceof HotBean){
            ++tag;
            HotBean hotBean = (HotBean) object;
            allBean.setHot(hotBean);
        }else if(object instanceof ShowingBean){
            ++tag;
            ShowingBean showingBean = (ShowingBean) object;
            allBean.setShowing(showingBean);
        }else if(object instanceof ShowBean){
            ++tag;
            ShowBean showBean = (ShowBean) object;
            allBean.setShow(showBean);
        }
        if(tag==3){
            tag=0;
            adapter.setAllBean(allBean);
        }
    }

    @Override
    protected void netFail(String s) {
        String s1 = s;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
