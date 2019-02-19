package com.bw.movie.my.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.bw.movie.R;
import com.bw.movie.activity.BaseActivity;
import com.bw.movie.api.Apis;
import com.bw.movie.application.MyApplication;
import com.bw.movie.login.activity.LoginActivity;
import com.bw.movie.my.bean.UpdatePwdBean;
import com.bw.movie.util.EncryptUtil;
import com.bw.movie.util.ToastUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @作者 GXY
 * @创建日期 2019/1/27 10:21
 * @描述 重置密码
 */
public class UpdatePasswordActivity extends BaseActivity {
    @BindView(R.id.editloadpwd)
    EditText editloadpwd;
    @BindView(R.id.editnewpwd)
    EditText editnewpwd;
    @BindView(R.id.editnewpwd2)
    EditText editnewpwd2;
    @BindView(R.id.confirm)
    Button confirm;
    @BindView(R.id.returnbutton)
    ImageButton returnbutton;
    private String loadpwd;
    private String newpwd;
    private String newpwd2;
    @Override
    protected void initData() {

    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this);

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_update_password;
    }
    @Override
    protected void netSuccess(Object object) {
        if(object instanceof UpdatePwdBean){
            UpdatePwdBean pwdBean = (UpdatePwdBean) object;
            if(pwdBean==null || !pwdBean.isSuccess()){
                ToastUtil.showToast(pwdBean.getMessage());
            }else{
                ToastUtil.showToast(pwdBean.getMessage());
               /* Intent intent = new Intent(UpdatePasswordActivity.this,LoginActivity.class);
                startActivity(intent);*/
            }
        }
    }

    @Override
    protected void netFail(String s) {

    }

    @OnClick({R.id.confirm, R.id.returnbutton})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.confirm:
                loadpwd = editloadpwd.getText().toString().trim();
                newpwd = editnewpwd.getText().toString().trim();
                newpwd2 = editnewpwd2.getText().toString().trim();
                if(loadpwd.equals(newpwd)){
                    ToastUtil.showToast("新密码和旧密码一样");
                }else{
                    if(newpwd.equals(newpwd2)){
                        Map<String,String> map = new HashMap<>();
                        map.put("oldPwd",EncryptUtil.encrypt(loadpwd));
                        map.put("newPwd",EncryptUtil.encrypt(newpwd));
                        map.put("newPwd2",EncryptUtil.encrypt(newpwd2));
                        doNetWorkPostRequest(Apis.URL_MODIFY_USER_PWD_POST,map,UpdatePwdBean.class);
                    }else{
                        ToastUtil.showToast("两次输入密码不一致");
                    }
                }
                break;
            case R.id.returnbutton:
                finish();
                break;
                default:
                    break;
        }
    }
}
