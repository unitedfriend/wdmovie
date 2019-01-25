package com.bw.movie.login.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bw.movie.R;
import com.bw.movie.activity.BaseActivity;
import com.bw.movie.api.Apis;
import com.bw.movie.home.activity.HomeActivity;
import com.bw.movie.login.bean.LoginBean;
import com.bw.movie.register.activity.RegisterActivity;
import com.bw.movie.util.AccountValidatorUtil;
import com.bw.movie.util.EmptyUtil;
import com.bw.movie.util.EncryptUtil;
import com.bw.movie.util.ToastUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @作者 GXY
 * @创建日期 2019/1/24 9:06
 * @描述 登录
 */
public class LoginActivity extends BaseActivity {
    @BindView(R.id.login_text_phone)
    EditText textPhone;
    @BindView(R.id.login_text_pwd)
    EditText textPwd;
    @BindView(R.id.login_check_rem)
    CheckBox checkRem;
    @BindView(R.id.login_check_auto)
    CheckBox checkAuto;
    @BindView(R.id.login_text_sign)
    TextView textSign;
    @BindView(R.id.login_but)
    Button loginBut;
    @BindView(R.id.login_weixin)
    ImageView loginWeixin;
    @BindView(R.id.login_image_eye)
    ImageButton imageButton;
    private SharedPreferences preferences;
    private SharedPreferences.Editor edit;
    private String phone;
    private String password;
    private String encrypt;

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {

    }

    /**
     * 初始化view
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        preferences = getSharedPreferences("User",MODE_PRIVATE);
        edit = preferences.edit();

        //将记住密码的状态值取出
        boolean r_check = preferences.getBoolean("r_check", false);
        if (r_check) {
            //取值
            String phone = preferences.getString("phone", null);
            String password = preferences.getString("password", null);
            //设置值
            textPhone.setText(phone);
            textPwd.setText(password);
            checkRem.setChecked(true);
        }
        //将自动状态值取出
        boolean o_check = preferences.getBoolean("o_check", false);
        if(o_check){
           getData();
        }
        // 勾选自动登录同事勾选记住 密码
        checkAuto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    checkRem.setChecked(true);
                }else{
                    checkRem.setChecked(false);
                }
            }
        });
        //密码显示和隐藏
        imageButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    textPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    textPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                return false;
            }
        });
    }

    /**
     * 加载布局
     */
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_login;
    }
    /**
     * 请求数据
     * */
    private void getData() {
        Map<String,String> map = new HashMap<>();
        map.put("phone",textPhone.getText().toString().trim());
        map.put("pwd",encrypt);
        doNetWorkPostRequest(Apis.URL_LOGIN_POST,map,LoginBean.class);
    }
    /**
     * 成功
     * */
    @Override
    protected void netSuccess(Object object) {
        if(object instanceof LoginBean){
            LoginBean loginBean = (LoginBean) object;
            int userId = loginBean.getResult().getUserId();
            String sessionId = loginBean.getResult().getSessionId();
            if(loginBean==null || !loginBean.isSuccess()){
                ToastUtil.showToast(loginBean.getMessage());
            }else{
                ToastUtil.showToast(getResources().getString(R.string.login_successfully));
                //判断记住密码是否勾选
                if(checkRem.isChecked()){
                    //存值
                    edit.putString("phone",phone);
                    edit.putString("password",password);
                    //存入一个勾选转态
                    edit.putBoolean("r_check", true);
                    //提交
                    edit.commit();
                }else{
                    //清空
                    edit.clear();
                    //提交
                    edit.commit();
                }
                //自动登录
                if(checkAuto.isChecked()){
                    //存入一个勾选状态
                    edit.putBoolean("o_check",true);
                    //提交
                    edit.commit();
                }
                preferences.edit().putString("userId",String.valueOf(userId)).putString("sessionId",sessionId).commit();
                Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                startActivity(intent);

            }
        }
    }

    @Override
    protected void netFail(String s) {

    }

    @OnClick({R.id.login_text_sign, R.id.login_but, R.id.login_weixin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login_text_sign:
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.login_but:
                phone = textPhone.getText().toString().trim();
                password = textPwd.getText().toString().trim();
                encrypt = EncryptUtil.encrypt(password);
                //非空判断
                if(EmptyUtil.isNull(phone,password)){
                    if(AccountValidatorUtil.isPassword(password)){
                        getData();
                    }else{
                        ToastUtil.showToast(getResources().getString(R.string.is_pwd));
                    }
                }else{
                    ToastUtil.showToast(getResources().getString(R.string.is_null));
                }
                break;
            case R.id.login_weixin:
                break;
                default:
                    break;
        }
    }

}
