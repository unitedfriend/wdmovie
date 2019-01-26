package com.bw.movie.my.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bw.movie.R;
import com.bw.movie.activity.BaseActivity;
import com.bw.movie.my.bean.MyMessageBean;
import com.facebook.drawee.view.SimpleDraweeView;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @作者 GXY
 * @创建日期 2019/1/25 19:07
 * @描述 我的信息Activity
 */
public class MyMessageActivity extends BaseActivity {
    @BindView(R.id.usericon_image)
    SimpleDraweeView usericonImage;
    @BindView(R.id.nickname)
    TextView userNickname;
    @BindView(R.id.sex)
    TextView userSex;
    @BindView(R.id.birth)
    TextView birth;
    @BindView(R.id.phonenumber)
    TextView phonenumber;
    @BindView(R.id.emailnum)
    TextView emailnum;
    @BindView(R.id.resetbutton)
    ImageButton resetbutton;
    @BindView(R.id.returnbutton)
    ImageButton returnbutton;

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        Intent intent = getIntent();
        MyMessageBean.ResultBean result = (MyMessageBean.ResultBean) intent.getSerializableExtra("result");
        String headPic = result.getHeadPic();
        String nickName = result.getNickName();
        int sex = result.getSex();
        String birthday = result.getBirthday();
        String phone = result.getPhone();
        usericonImage.setImageURI(Uri.parse(headPic));
        userNickname.setText(nickName);
        birth.setText(birthday);
        phonenumber.setText(phone);
        if (sex == 1) {
            userSex.setText("男");
        }else if(sex == 2){
            userSex.setText("女");
        }
    }

    /**
     * 初始化view
     */
    @Override
    protected void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this);
    }

    /**
     * 加载布局
     */
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_my_message;
    }

    /**
     * 成功
     */
    @Override
    protected void netSuccess(Object object) {

    }

    /**
     * 失败
     */
    @Override
    protected void netFail(String s) {

    }

    @OnClick({R.id.usericon_image, R.id.nickname, R.id.resetbutton, R.id.returnbutton})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.usericon_image:

                break;
            case R.id.nickname:
                break;
            case R.id.resetbutton:
                break;
            case R.id.returnbutton:
                finish();
                break;
                default:
                    break;
        }
    }
}
