package com.bw.movie.home.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bw.movie.R;
import com.bw.movie.activity.BaseActivity;
import com.bw.movie.api.Apis;
import com.bw.movie.home.adapter.HomeViewPageAdapter;
import com.bw.movie.home.fragment.CinemaFragment;
import com.bw.movie.home.fragment.FilmFragment;
import com.bw.movie.home.fragment.MyFragment;
import com.bw.movie.home.view.NoScrollViewPager;
import com.bw.movie.my.bean.PushTokenBean;
import com.bw.movie.util.ToastUtil;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Tang
 * @time 2019/1/28  9:04
 * @describe 首页的activity, 有三个页面
 */
public class HomeActivity extends BaseActivity {
    //获取id
    @BindView(R.id.home_viewpage)
    NoScrollViewPager homeViewpage;
    @BindView(R.id.film)
    RadioButton film;
    @BindView(R.id.cinema)
    RadioButton cinema;
    @BindView(R.id.my)
    RadioButton my;
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;

    private CinemaFragment cinemaFragment;
    private FilmFragment filmFragment;

    @Override
    protected void initData() {
        initSelector();
    }

    /**
     * @author Tang
     * @time 2019/1/25  14:49
     * @describe 设置点击放大的动画效果
     */
    private void initSelector() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.film:
                        //点击同时设置其他按钮回归原状并切换页面
                        setMagnify(film);
                        setShrink(cinema);
                        setShrink(my);
                        homeViewpage.setCurrentItem(0, false);
                        break;
                    case R.id.cinema:
                        setMagnify(cinema);
                        setShrink(film);
                        setShrink(my);
                        cinemaFragment.getInit();
                        homeViewpage.setCurrentItem(1, false);
                        break;
                    case R.id.my:
                        setMagnify(my);
                        setShrink(film);
                        setShrink(cinema);
                        homeViewpage.setCurrentItem(2, false);
                        break;
                    default:
                        break;
                }
            }
        });
        film.setChecked(true);
    }

    /**
     * @author Tang
     * @time 2019/1/25  15:47
     * @describe 使按钮变大的属性动画
     */
    private void setMagnify(View view) {
        //组合动画
        final AnimatorSet animatorSetFilmTrue = new AnimatorSet();
        ObjectAnimator scaleXFilmTrue = ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.2f);
        ObjectAnimator scaleYFilmTrue = ObjectAnimator.ofFloat(view, "scaleY", 1f, 1.2f);
        animatorSetFilmTrue.setDuration(0);
        animatorSetFilmTrue.setInterpolator(new DecelerateInterpolator());
        //两个动画同时开始
        animatorSetFilmTrue.play(scaleXFilmTrue).with(scaleYFilmTrue);
        animatorSetFilmTrue.start();
    }

    /**
     * @author Tang
     * @time 2019/1/25  15:48
     * @describe 使按钮缩小的属性动画
     */
    private void setShrink(View view) {
        //组合动画
        final AnimatorSet animatorSetFilmFalse = new AnimatorSet();
        ObjectAnimator scaleXFilmFalse = ObjectAnimator.ofFloat(view, "scaleX", 1f, 1f);
        ObjectAnimator scaleYFilmFalse = ObjectAnimator.ofFloat(view, "scaleY", 1f, 1f);
        animatorSetFilmFalse.setDuration(0);
        animatorSetFilmFalse.setInterpolator(new DecelerateInterpolator());
        //两个动画同时开始
        animatorSetFilmFalse.play(scaleXFilmFalse).with(scaleYFilmFalse);
        animatorSetFilmFalse.start();
    }

    /**
     * @author Tang
     * @time 2019/1/25  14:36
     * @describe 加载viewpage布局,
     */
    @Override
    protected void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        List<Fragment> list = new ArrayList<>();
        cinemaFragment = new CinemaFragment();
        filmFragment = new FilmFragment();
        MyFragment myFragment = new MyFragment();
        list.add(filmFragment);
        list.add(cinemaFragment);
        list.add(myFragment);
        HomeViewPageAdapter pageAdapter = new HomeViewPageAdapter(getSupportFragmentManager(), list);
        homeViewpage.setAdapter(pageAdapter);
        homeViewpage.setOffscreenPageLimit(2);
        getPushToken();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_home;
    }
    /**
      * @作者 GXY
      * @创建日期 2019/2/19 15:57
      * @描述 信鸽推送
      *
      */
    private void getPushToken() {
        XGPushConfig.setAccessId(getApplicationContext(), 2100300660);
        XGPushConfig.setAccessKey(getApplicationContext(), "A44FJ9N7N9EY");

        XGPushConfig.enableDebug(this,true);
        XGPushConfig.enableOtherPush(getApplicationContext(), true);
        XGPushConfig.setHuaweiDebug(true);
        XGPushManager.registerPush(this, new XGIOperateCallback() {
            @Override
            public void onSuccess(Object data, int flag) {
                //token在设备卸载重装的时候有可能会变
                Log.d("gxy", "注册成功，设备token为：" + data);
                //ToastUtil.showToast(data + "");
                String token = (String) data;
                Map<String, String> map = new HashMap<>();
                map.put("token", token);
                map.put("os", String.valueOf(1));
                doNetWorkPostRequest(Apis.URL_UP_LOAD_PUSH_TOKEN_POST, map, PushTokenBean.class);
            }

            @Override
            public void onFail(Object data, int errCode, String msg) {
                //Log.d("TPush", "注册失败，错误码：" + errCode + ",错误信息：" + msg);
            }
        });
    }

    @Override
    protected void netSuccess(Object object) {
        if (object instanceof PushTokenBean) {
            PushTokenBean pushTokenBean = (PushTokenBean) object;
            if (pushTokenBean == null || !pushTokenBean.isSuccess()) {
                ToastUtil.showToast(pushTokenBean.getMessage());
            } else {
                ToastUtil.showToast(pushTokenBean.getMessage());
            }
        }
    }

    @Override
    protected void netFail(String s) {

    }

    //监听返回键
    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {

            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }
}
