package com.bw.movie.home.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.bw.movie.R;
import com.bw.movie.activity.BaseActivity;
import com.bw.movie.api.Apis;
import com.bw.movie.application.MyApplication;
import com.bw.movie.home.adapter.FilmRecycleAdapter;
import com.bw.movie.home.adapter.MovieListAdapter;
import com.bw.movie.home.bean.MyAddressBean;
import com.bw.movie.home.fragment.ListHotFragment;
import com.bw.movie.home.fragment.ListShowFragment;
import com.bw.movie.home.fragment.ListShowingFragment;
import com.bw.movie.util.ToastUtil;
import com.zaaach.citypicker.CityPicker;
import com.zaaach.citypicker.adapter.OnPickListener;
import com.zaaach.citypicker.model.City;
import com.zaaach.citypicker.model.HotCity;
import com.zaaach.citypicker.model.LocateState;
import com.zaaach.citypicker.model.LocatedCity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.tencent.bugly.Bugly.enable;

/**
 *  @author Tang
 *  @time 2019/1/28  8:58
 *  @describe 电影分组页面
 */
public class MovieListActivity extends BaseActivity {
    @BindView(R.id.locationImage)
    ImageView locationImage;
    @BindView(R.id.locationText)
    TextView locationText;
    @BindView(R.id.searchImage)
    ImageView searchImage;
    @BindView(R.id.searchEditText)
    EditText searchEditText;
    @BindView(R.id.searchText)
    TextView searchText;
    @BindView(R.id.searchViewGroup)
    ConstraintLayout searchViewGroup;
    @BindView(R.id.viewPage)
    ViewPager viewPage;
    @BindView(R.id.back)
    ImageView back;
    boolean b = true;
    @BindView(R.id.hotBut)
    RadioButton hotBut;
    @BindView(R.id.showingBut)
    RadioButton showingBut;
    @BindView(R.id.showBut)
    RadioButton showBut;
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;
    //声明mlocationClient对象
    public AMapLocationClient mlocationClient;
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;
    private String city;
    private String adCode;
    private String cityCode;
    private String province;
    private List<HotCity> hotCities;
    private int anim;
    private int theme;
    private double longitude;
    private double latitude;
    private String cityName;
    @Override
    protected void initData() {

        SharedPreferences location = getSharedPreferences("Location", Context.MODE_PRIVATE);
        String mlatitude = location.getString("latitude", null);
        String mlongitude = location.getString("longitude", null);
        String mcity = location.getString("city", null);
        String mcityCode = location.getString("cityCode", null);
        locationText.setText(mcity);

        //开始定位，这里模拟一下定位
        mlocationClient = new AMapLocationClient(MovieListActivity.this);
        //设置定位监听
        mlocationClient.setLocationListener(new AMapLocationListener() {


            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if(aMapLocation!=null){
                    if(aMapLocation.getErrorCode() == 0){
                        //获取纬度
                        latitude = aMapLocation.getLatitude();
                        //获取经度
                        longitude = aMapLocation.getLongitude();
                        //城市信息
                        MovieListActivity.this.city = aMapLocation.getCity();
                        //省信息
                        province = aMapLocation.getProvince();
                        //城市编码
                        MovieListActivity.this.cityCode = aMapLocation.getCityCode();
                        //地区编码
                        adCode = aMapLocation.getAdCode();


                        CityPicker.from(MovieListActivity.this).locateComplete(new LocatedCity(MovieListActivity.this.city, province, MovieListActivity.this.cityCode), LocateState.SUCCESS);

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
       /* locationImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               hotCities = new ArrayList<>();
                hotCities.add(new HotCity("北京", "北京", "101010100"));
                hotCities.add(new HotCity("上海", "上海", "101020100"));
                hotCities.add(new HotCity("广州", "广东", "101280101"));
                hotCities.add(new HotCity("深圳", "广东", "101280601"));
                hotCities.add(new HotCity("杭州", "浙江", "101210101"));

                CityPicker.from(MovieListActivity.this)
                        .enableAnimation(enable)
                        .setAnimationStyle(anim)
                        .setLocatedCity(null)
                        .setHotCities(hotCities)
                        .setOnPickListener(new OnPickListener() {

                            @Override
                            public void onPick(int position, City data) {
                                 cityName = data.getName();
                                locationText.setText(data.getName());
                                doNetWorkGetRequest(String.format(Apis.URL_LOCATION_GET,data.getName()),MyAddressBean.class);

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
                                        CityPicker.from(MovieListActivity.this).locateComplete(new LocatedCity(MovieListActivity.this.city, province, MovieListActivity.this.cityCode), LocateState.SUCCESS);
                                    }
                                }, 1000);
                                locationText.setText(city);
                            }
                        })
                        .show();
            }
        });*/


    }
/**
 *  @author Tang
 *  @time 2019/1/28  8:59
 *  @describe 加载视图
 */
    @Override
    protected void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        //加载搜索框动画
        initSearchClick();
        //加载tablayout
        initRadioButton();
        initViewPage();
        //判断是从哪个组过来的
        initTypeGroup();
    }
/**
 *  @author Tang
 *  @time 2019/1/28  9:00
 *  @describe 判断是从哪个组跳转过来的,选中这个组
 */
    private void initTypeGroup() {
        String type = getIntent().getStringExtra("type");
        switch (type){
            case "hot":
                radioGroup.check(R.id.hotBut);
                break;
            case "showing":
                radioGroup.check(R.id.showingBut);
                break;
            case "show":
                radioGroup.check(R.id.showBut);
                break;
        }
    }
    /**
     *  @author Tang
     *  @time 2019/1/28  9:01
     *  @describe 创建fragment,加入viewpage
     */
    private void initViewPage() {
        List<Fragment> list = new ArrayList<>();
        if (list.size() <= 0) {
            final ListHotFragment listHotFragment = new ListHotFragment();
            final ListShowingFragment listShowingFragment = new ListShowingFragment();
            ListShowFragment listShowFragment = new ListShowFragment();
            list.add(listHotFragment);
            list.add(listShowingFragment);
            list.add(listShowFragment);
            listHotFragment.setListHotCallBack(new ListHotFragment.ListHotCallBack() {
                @Override
                public void callBack() {
                    listShowingFragment.onResume();
                }
            });
            listShowingFragment.setListShowingCallBack(new ListShowingFragment.ListShowingCallBack() {
                @Override
                public void callBack() {
                    listHotFragment.onResume();
                }
            });
        }
        MovieListAdapter listAdapter = new MovieListAdapter(getSupportFragmentManager(), list);
        viewPage.setAdapter(listAdapter);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


/**
 *  @author Tang
 *  @time 2019/1/28  9:02
 *  @describe RadioButton与ViewPage做联动,并给RadioButton做背景切换
 */
    private void initRadioButton() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.hotBut:
                        hotBut.setTextColor(Color.WHITE);
                        showingBut.setTextColor(Color.BLACK);
                        showBut.setTextColor(Color.BLACK);
                        viewPage.setCurrentItem(0,false);
                        break;
                    case R.id.showingBut:
                        hotBut.setTextColor(Color.BLACK);
                        showingBut.setTextColor(Color.WHITE);
                        showBut.setTextColor(Color.BLACK);
                        viewPage.setCurrentItem(1,false);
                        break;
                    case R.id.showBut:
                        hotBut.setTextColor(Color.BLACK);
                        showingBut.setTextColor(Color.BLACK);
                        showBut.setTextColor(Color.WHITE);
                        viewPage.setCurrentItem(2,false);
                        break;
                }
            }
        });
        viewPage.setOffscreenPageLimit(2);
        //viewpage切换时切换对应button
        viewPage.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                switch (i){
                    case 0:
                        radioGroup.check(R.id.hotBut);
                        break;
                    case 1:
                        radioGroup.check(R.id.showingBut);
                        break;
                    case 2:
                        radioGroup.check(R.id.showBut);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    /**
     * @author Tang
     * @time 2019/1/27  10:58
     * @describe 加载动画, 并设置点击事件
     */
    private void initSearchClick() {
        setTranslationOut(searchViewGroup, 0);
        searchEditText.setVisibility(View.GONE);
        searchText.setVisibility(View.GONE);
        searchImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (b) {
                    b = !b;
                    setTranslationInit(searchViewGroup, 500);
                    searchEditText.setVisibility(View.VISIBLE);
                    searchText.setVisibility(View.VISIBLE);
                } else {
                    b = !b;
                    setTranslationOut(searchViewGroup, 500);
                    searchEditText.setVisibility(View.GONE);
                    searchText.setVisibility(View.GONE);
                }
            }
        });
        searchText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b = !b;
                setTranslationOut(searchViewGroup, 500);
                searchEditText.setVisibility(View.GONE);
                searchText.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_movielist;
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        initData();
    }

    @Override
    protected void netSuccess(Object object) {
        if(object instanceof MyAddressBean){
            MyAddressBean object1 = (MyAddressBean) object;
            MyAddressBean.ResultBean result = object1.getResult();
            double lat = result.getLocation().getLat();
            double lng = result.getLocation().getLng();
            locationText.setText(cityName);
            SharedPreferences.Editor location = getSharedPreferences("Location", Context.MODE_PRIVATE).edit();
            location.putString("latitude",lat+"");
            location.putString("longitude",lng+"");
            location.putString("city",cityName);
            location.commit();
        }

    }

    @Override
    protected void netFail(String s) {

    }

    /**
     * @author Tang
     * @time 2019/1/26  9:29
     * @describe 平移动画, 使搜索框出现在屏幕右侧, 实现隐藏效果
     */
    private void setTranslationOut(View view, int time) {
        ObjectAnimator translationY = new ObjectAnimator().ofFloat(view, "translationX", 0, 510f);
        AnimatorSet animatorSet = new AnimatorSet();  //组合动画
        animatorSet.playTogether(translationY); //设置动画
        animatorSet.setDuration(time);  //设置动画时间
        animatorSet.start();
    }

    /**
     * @author Tang
     * @time 2019/1/26  9:29
     * @describe 平移动画, 使搜索框出现在屏幕中间实现点击弹出的效果
     */
    private void setTranslationInit(View view, int time) {
        ObjectAnimator translationY = new ObjectAnimator().ofFloat(view, "translationX", 510f, 0);
        AnimatorSet animatorSet = new AnimatorSet();  //组合动画
        animatorSet.playTogether(translationY); //设置动画
        animatorSet.setDuration(time);  //设置动画时间
        animatorSet.start();
    }


}
