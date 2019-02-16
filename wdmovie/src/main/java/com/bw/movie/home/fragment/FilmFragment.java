package com.bw.movie.home.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.bw.movie.R;
import com.bw.movie.api.Apis;
import com.bw.movie.application.MyApplication;
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
 * @author Tang
 * @time 2019/1/28  9:12
 * @describe 电影首页的电影页面
 */
public class FilmFragment extends BaseFragment{
    @BindView(R.id.home_recycle)
    RecyclerView homeRecycle;
    Unbinder unbinder;
    private final int PAGE = 1;
    private final int COUNT = 10;
    private AllBean allBean;
    private int tag;
    private FilmRecycleAdapter adapter;
    private List<HotCity> hotCities;
    private int anim;
    private int theme;
    private boolean enable;
    //声明mlocationClient对象
    public AMapLocationClient mlocationClient;
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;
    private String city;
    private String adCode;
    private String cityCode;
    private String province;
    @Override
    protected void initData() {

    }

    @Override
    protected void initView(View view) {
        unbinder = ButterKnife.bind(this, view);
        tag = 0;
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        homeRecycle.setLayoutManager(layoutManager);
        adapter = new FilmRecycleAdapter(getActivity());
        homeRecycle.setAdapter(adapter);
        //创建一个Bean得到所有网络请求的数据
        allBean = new AllBean();
        doNetWorkGetRequest(String.format(Apis.URL_FIND_HOT_MOVIE_LIST_GET, PAGE, COUNT), HotBean.class);
        doNetWorkGetRequest(String.format(Apis.URL_FIND_RELEASE_MOVIE_LIST_GET, PAGE, COUNT), ShowingBean.class);
        doNetWorkGetRequest(String.format(Apis.URL_FIND_COMING_SOON_MOVIE_LIST_GET, PAGE, COUNT), ShowBean.class);
        //开始定位，这里模拟一下定位
        mlocationClient = new AMapLocationClient(getActivity());
        //设置定位监听
        mlocationClient.setLocationListener(new AMapLocationListener() {

            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if(aMapLocation!=null){
                    if(aMapLocation.getErrorCode() == 0){
                        //获取纬度
                        double latitude = aMapLocation.getLatitude();
                        //获取经度
                        double longitude = aMapLocation.getLongitude();
                        //城市信息
                        city = aMapLocation.getCity();
                        //省信息
                        province = aMapLocation.getProvince();
                        //城市编码
                        cityCode = aMapLocation.getCityCode();
                        //地区编码
                        adCode = aMapLocation.getAdCode();
                        CityPicker.from(getActivity()).locateComplete(new LocatedCity(city, province,cityCode), LocateState.SUCCESS);
                        adapter.setLocation(String.format(city));
                    }
                }
            }
        });
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        //设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为1000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        //启动定位
        mlocationClient.startLocation();
        adapter.setFilmCallBack(new FilmRecycleAdapter.FilmCallBack() {
            @Override
            public void addressCallBack() {
                hotCities = new ArrayList<>();
                hotCities.add(new HotCity("北京", "北京", "101010100"));
                hotCities.add(new HotCity("上海", "上海", "101020100"));
                hotCities.add(new HotCity("广州", "广东", "101280101"));
                hotCities.add(new HotCity("深圳", "广东", "101280601"));
                hotCities.add(new HotCity("杭州", "浙江", "101210101"));

                CityPicker.from(getActivity())
                        .enableAnimation(enable)
                        .setAnimationStyle(anim)
                        .setLocatedCity(null)
                        .setHotCities(hotCities)
                        .setOnPickListener(new OnPickListener() {
                            @Override
                            public void onPick(int position, City data) {
                                adapter.setLocation(String.format(data.getName()));
                                Toast.makeText(
                                        MyApplication.getApplication(),
                                        String.format(data.getName()),
                                        Toast.LENGTH_SHORT)
                                        .show();
                            }

                            @Override
                            public void onCancel() {
                                ToastUtil.showToast("取消选择");
                            }

                            @Override
                            public void onLocate() {
                                mlocationClient.stopLocation();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        CityPicker.from(getActivity()).locateComplete(new LocatedCity(city, province,cityCode), LocateState.SUCCESS);
                                    }
                                }, 1000);
                                adapter.setLocation(String.format(city));
                            }
                        })
                        .show();
            }
            @Override
            public void searchCallBack(String s) {
                //搜索按钮击事件
                startActivity(new Intent(getActivity(),MovieListActivity.class).putExtra("type","search").putExtra("name",s));
            }

            @Override
            public void bannerCallBack(String id) {
                //轮播图图片点击事件
                startActivity(new Intent(getActivity(), FilmDetailsActivity.class).putExtra("id", id));
            }

            @Override
            public void hotSkipGroup() {
                //热门电影点击事件
                startActivity(new Intent(getActivity(), MovieListActivity.class).putExtra("type", "hot"));
            }

            @Override
            public void showingSkipGroup() {
                //正在上映点击事件
                startActivity(new Intent(getActivity(), MovieListActivity.class).putExtra("type", "showing"));
            }

            @Override
            public void showSkipGroup() {
                //即将上映点击事件
                startActivity(new Intent(getActivity(), MovieListActivity.class).putExtra("type", "show"));
            }

            @Override
            public void detailsCallBack(String id) {
                //点击影片展示详情
                startActivity(new Intent(getActivity(), FilmDetailsActivity.class).putExtra("id", id));
            }
        });
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_film;
    }

    @Override
    protected void netSuccess(Object object) {
        if (object instanceof HotBean) {
            ++tag;
            HotBean hotBean = (HotBean) object;
            allBean.setHot(hotBean);
        } else if (object instanceof ShowingBean) {
            ++tag;
            ShowingBean showingBean = (ShowingBean) object;
            allBean.setShowing(showingBean);
        } else if (object instanceof ShowBean) {
            ++tag;
            ShowBean showBean = (ShowBean) object;
            allBean.setShow(showBean);
        }
        if (tag == 3) {
            tag = 0;
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
