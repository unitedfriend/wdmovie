package com.bw.movie.home.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.RadioButton;
import android.widget.RadioGroup;

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

public class HomeActivity extends BaseActivity {
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
        //film选中的动画

        //film取消选中的动画


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.film:
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
        final AnimatorSet animatorSetFilmTrue = new AnimatorSet();//组合动画
        ObjectAnimator scaleXFilmTrue = ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.2f);
        ObjectAnimator scaleYFilmTrue = ObjectAnimator.ofFloat(view, "scaleY", 1f, 1.2f);
        animatorSetFilmTrue.setDuration(0);
        animatorSetFilmTrue.setInterpolator(new DecelerateInterpolator());
        animatorSetFilmTrue.play(scaleXFilmTrue).with(scaleYFilmTrue);//两个动画同时开始
        animatorSetFilmTrue.start();
    }
/**
 *  @author Tang
 *  @time 2019/1/25  15:48
 *  @describe 使按钮缩小的属性动画
 */
    private void setShrink(View view){
        final AnimatorSet animatorSetFilmFalse = new AnimatorSet();//组合动画
        ObjectAnimator scaleXFilmFalse = ObjectAnimator.ofFloat(view, "scaleX", 1f, 1f);
        ObjectAnimator scaleYFilmFalse = ObjectAnimator.ofFloat(view, "scaleY", 1f, 1f);
        animatorSetFilmFalse .setDuration(0);
        animatorSetFilmFalse .setInterpolator(new DecelerateInterpolator());
        animatorSetFilmFalse .play(scaleXFilmFalse).with(scaleYFilmFalse);//两个动画同时开始
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


}
