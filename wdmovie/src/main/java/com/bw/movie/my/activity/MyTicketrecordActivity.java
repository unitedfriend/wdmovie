package com.bw.movie.my.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.bw.movie.R;
import com.bw.movie.activity.BaseActivity;
import com.bw.movie.my.adaper.AttentionViewPagerAdaper;
import com.bw.movie.my.adaper.RecordViewPagerAdaper;
import com.bw.movie.my.fragment.AttentionCinemaFragment;
import com.bw.movie.my.fragment.AttentionMovieFragment;
import com.bw.movie.my.fragment.ObligationFragment;
import com.bw.movie.my.fragment.StocksFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @作者 GXY
 * @创建日期 2019/1/27 18:39
 * @描述 购票记录
 */
public class MyTicketrecordActivity extends BaseActivity {
    @BindView(R.id.obligation)
    RadioButton obligation;
    @BindView(R.id.stocks)
    RadioButton stocks;
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;
    @BindView(R.id.myviewpager)
    ViewPager myviewpager;
    @BindView(R.id.attentlayout)
    ConstraintLayout attentlayout;
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
        ObligationFragment obligationFragment = new ObligationFragment();
        StocksFragment stocksFragment = new StocksFragment();
        list.add(obligationFragment);
        list.add(stocksFragment);
        RecordViewPagerAdaper recordViewPagerAdaper = new RecordViewPagerAdaper(getSupportFragmentManager(),list);
        myviewpager.setAdapter(recordViewPagerAdaper);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_my_ticketrecord;
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
                    case R.id.obligation:
                        obligation.setTextColor(Color.WHITE);
                        stocks.setTextColor(Color.BLACK);
                        myviewpager.setCurrentItem(0,false);
                        break;
                    case R.id.stocks:
                        obligation.setTextColor(Color.BLACK);
                        stocks.setTextColor(Color.WHITE);
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
                        radioGroup.check(R.id.obligation);
                        break;
                    case 1:
                        radioGroup.check(R.id.stocks);
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
    @OnClick(R.id.returnbutton)
    public void onViewClicked() {
        finish();
    }
}
