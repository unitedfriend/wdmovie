package com.bw.movie.register.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.bw.movie.R;
import com.bw.movie.activity.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends BaseActivity {
    @BindView(R.id.sign_text_nick)
    EditText signTextNick;
    @BindView(R.id.sign_text_sex)
    EditText signTextSex;
    @BindView(R.id.sign_text_date)
    EditText signTextDate;
    @BindView(R.id.sign_text_phone)
    EditText signTextPhone;
    @BindView(R.id.sign_text_email)
    EditText signTextEmail;
    @BindView(R.id.sign_text_pwd)
    EditText signTextPwd;
    @BindView(R.id.sign_but)
    Button signBut;

    /**
     * 加载数据
     */
    @Override
    protected void initData() {

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
        return R.layout.activity_register;
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

    @OnClick(R.id.sign_but)
    public void onViewClicked() {

    }
}
