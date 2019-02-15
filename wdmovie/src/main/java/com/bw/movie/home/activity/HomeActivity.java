package com.bw.movie.home.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bw.movie.R;
import com.bw.movie.activity.BaseActivity;
import com.bw.movie.home.adapter.HomeViewPageAdapter;
import com.bw.movie.home.fragment.CinemaFragment;
import com.bw.movie.home.fragment.FilmFragment;
import com.bw.movie.home.fragment.MyFragment;
import com.bw.movie.home.view.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
 *  @author Tang
 *  @time 2019/1/28  9:04
 *  @describe 首页的activity,有三个页面
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

    @Override
    protected void initData() {
        initSelector();

    }
    /**
     *  @author Tang
     *  @time 2019/1/25  14:49
     *  @describe 设置点击放大的动画效果
     */
    private void initSelector() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.film:
                        //点击同时设置其他按钮回归原状并切换页面
                        setMagnify(film);
                        setShrink(cinema);
                        setShrink(my);
                        homeViewpage.setCurrentItem(0,false);
                        break;
                    case R.id.cinema:
                        setMagnify(cinema);
                        setShrink(film);
                        setShrink(my);
                        homeViewpage.setCurrentItem(1,false);
                        break;
                    case R.id.my:
                        setMagnify(my);
                        setShrink(film);
                        setShrink(cinema);
                        homeViewpage.setCurrentItem(2,false);
                        break;
                        default:
                            break;
                }
            }
        });

        film.setChecked(true);
    }
    /**
     *  @author Tang
     *  @time 2019/1/25  15:47
     *  @describe 使按钮变大的属性动画
     */
    private void setMagnify(View view){
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
     *  @author Tang
     *  @time 2019/1/25  15:48
     *  @describe 使按钮缩小的属性动画
     */
    private void setShrink(View view){
        //组合动画
        final AnimatorSet animatorSetFilmFalse = new AnimatorSet();
        ObjectAnimator scaleXFilmFalse = ObjectAnimator.ofFloat(view, "scaleX", 1f, 1f);
        ObjectAnimator scaleYFilmFalse = ObjectAnimator.ofFloat(view, "scaleY", 1f, 1f);
        animatorSetFilmFalse .setDuration(0);
        animatorSetFilmFalse .setInterpolator(new DecelerateInterpolator());
        //两个动画同时开始
        animatorSetFilmFalse .play(scaleXFilmFalse).with(scaleYFilmFalse);
        animatorSetFilmFalse.start();
    }
    /**
     *  @author Tang
     *  @time 2019/1/25  14:36
     *  @describe 加载viewpage布局,
     */
    @Override
    protected void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        List<Fragment>list = new ArrayList<>();
        CinemaFragment cinemaFragment = new CinemaFragment();
        FilmFragment filmFragment = new FilmFragment();
        MyFragment myFragment = new MyFragment();
        list.add(filmFragment);
        list.add(cinemaFragment);
        list.add(myFragment);
        HomeViewPageAdapter pageAdapter = new HomeViewPageAdapter(getSupportFragmentManager(),list);
        homeViewpage.setAdapter(pageAdapter);
        homeViewpage.setOffscreenPageLimit(2);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_home;
    }

    @Override
    protected void netSuccess(Object object) {

    }

    @Override
    protected void netFail(String s) {

    }

    //监听返回键
    private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){

            if((System.currentTimeMillis()-exitTime) > 2000){
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
