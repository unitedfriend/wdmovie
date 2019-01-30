package com.bw.movie.my.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.bw.movie.R;
import com.bw.movie.activity.BaseActivity;
import com.bw.movie.my.adaper.AttentionViewPagerAdaper;
import com.bw.movie.my.fragment.AttentionCinemaFragment;
import com.bw.movie.my.fragment.AttentionMovieFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @作者 GXY
 * @创建日期 2019/1/27 18:33
 * @描述 我的关注
 */
public class MyAttentionActivity extends BaseActivity {
    @BindView(R.id.attentionmovie)
    RadioButton attentionmovie;
    @BindView(R.id.attentioncinema)
    RadioButton attentioncinema;
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;
    @BindView(R.id.myviewpager)
    ViewPager myviewpager;
    @BindView(R.id.returnbutton)
    ImageButton returnbutton;

    @Override
    protected void initData() {
        initCliick();
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        List<Fragment> list = new ArrayList<>();
        AttentionMovieFragment movieFragment = new AttentionMovieFragment();
        AttentionCinemaFragment cinemaFragment = new AttentionCinemaFragment();
        list.add(movieFragment);
        list.add(cinemaFragment);
        AttentionViewPagerAdaper attentionViewPagerAdaper = new AttentionViewPagerAdaper(getSupportFragmentManager(),list);
        myviewpager.setAdapter(attentionViewPagerAdaper);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_my_attention;
    }

    @Override
    protected void netSuccess(Object object) {

    }

    @Override
    protected void netFail(String s) {

    }
    /**
     * 点击切换和滑动切换
     * */
    private void initCliick(){
        //点击切换
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.attentionmovie:
                        attentionmovie.setTextColor(Color.WHITE);
                        attentioncinema.setTextColor(Color.BLACK);
                        myviewpager.setCurrentItem(0,false);
                    break;
                    case R.id.attentioncinema:
                        attentionmovie.setTextColor(Color.BLACK);
                        attentioncinema.setTextColor(Color.WHITE);
                        myviewpager.setCurrentItem(1,false);
                        break;
                        default:
                            break;
                }
            }
        });
        //滑动切换
        myviewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                switch (i){
                    case 0:
                        radioGroup.check(R.id.attentionmovie);
                        break;
                    case 1:
                        radioGroup.check(R.id.attentioncinema);
                        break;
                        default:
                            break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }
    /**
     * 点击返回
     * */
    @OnClick(R.id.returnbutton)
    public void onViewClicked() {
        finish();
    }
}
