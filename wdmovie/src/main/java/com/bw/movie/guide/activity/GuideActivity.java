package com.bw.movie.guide.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bw.movie.R;
import com.bw.movie.activity.BaseActivity;
import com.bw.movie.guide.adapter.GuideViewPageAdapter;
import com.bw.movie.guide.bean.GuideBean;
import com.bw.movie.home.activity.HomeActivity;
import com.bw.movie.login.activity.LoginActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GuideActivity extends BaseActivity {
    @BindView(R.id.viewPage)
    ViewPager viewPage;
    @BindView(R.id.image)
    ImageView image;
    private SharedPreferences sharedPreferences;
    private boolean tag;


    /**
     * @author Tang
     * @time 2019/1/23  16:50
     * @describe 设置是否是第一次登陆,是的话展示引导页,否则直接跳转 ,设置倒计时2秒展示
     */
    @Override
    protected void initData() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                //显示dialog
                if (tag) {
                    startActivity(new Intent(GuideActivity.this, HomeActivity.class));
                    finish();
                } else {
                    image.setVisibility(View.INVISIBLE);
                }

            }
        }, 2000);


    }

    /**
     * @author Tang
     * @time 2019/1/23  16:50
     * @describe 加载viewpage,pageAdapter里如果是最后一页点击时跳转至登录页面
     */
    @Override
    protected void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this);

         SharedPreferences getlocation = getSharedPreferences("Location", Context.MODE_PRIVATE);
         getlocation.edit().clear().commit();

        sharedPreferences = getSharedPreferences("Guide", MODE_PRIVATE);
        tag = sharedPreferences.getBoolean("tag", false);

        GuideViewPageAdapter pageAdapter = new GuideViewPageAdapter(GuideActivity.this);
        viewPage.setAdapter(pageAdapter);
        List<GuideBean> list = new ArrayList<>();
        list.add(new GuideBean(R.mipmap.icon1, R.string.guide_text, R.string.guide_1_text1));
        list.add(new GuideBean(R.mipmap.icon2, R.string.guide_text, R.string.guide_2_text1));
        list.add(new GuideBean(R.mipmap.icon3, R.string.guide_text, R.string.guide_3_text1));
        list.add(new GuideBean(R.mipmap.icon, R.string.guide_text4, R.string.guide_4_text1));
        pageAdapter.setmList(list);
        pageAdapter.setCallBack(new GuideViewPageAdapter.GuideCallBack() {
            @Override
            public void callback() {
                SharedPreferences.Editor edit = sharedPreferences.edit();
                edit.putBoolean("tag", true);
                edit.commit();
                startActivity(new Intent(GuideActivity.this, HomeActivity.class));
                finish();
            }
        });
    }

    /**
     * @author Tang
     * @time 2019/1/23  16:50
     * @describe
     */
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_guide;
    }

    @Override
    protected void netSuccess(Object object) {

    }

    @Override
    protected void netFail(String s) {

    }


}
