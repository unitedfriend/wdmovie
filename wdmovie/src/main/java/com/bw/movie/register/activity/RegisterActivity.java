package com.bw.movie.register.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.bw.movie.R;
import com.bw.movie.activity.BaseActivity;
import com.bw.movie.api.Apis;
import com.bw.movie.home.activity.HomeActivity;
import com.bw.movie.login.bean.LoginBean;
import com.bw.movie.register.bean.RegisterBean;
import com.bw.movie.util.AccountValidatorUtil;
import com.bw.movie.util.EmptyUtil;
import com.bw.movie.util.EncryptUtil;
import com.bw.movie.util.ToastUtil;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @作者 GXY
 * @创建日期 2019/1/24 20:43
 * @描述 注册Activity
 */
public class RegisterActivity extends BaseActivity {
    @BindView(R.id.sign_text_nick)
    EditText textNick;
    @BindView(R.id.sign_text_sex)
    EditText textSex;
    @BindView(R.id.sign_text_date)
    EditText textDate;
    @BindView(R.id.sign_text_phone)
    EditText textPhone;
    @BindView(R.id.sign_text_email)
    EditText textEmail;
    @BindView(R.id.sign_text_pwd)
    EditText textPwd;
    @BindView(R.id.sign_but)
    Button signBut;
    private String phone;
    private String pwd;
    private SharedPreferences preferences;
    private SharedPreferences.Editor edit;
    public InputFilter[] emojiFilters = {emojiFilter};
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
        preferences = getSharedPreferences("User", MODE_PRIVATE);
        edit = preferences.edit();
        textNick.setFilters(emojiFilters);
        textSex.setFilters(emojiFilters);
        textDate.setFilters(emojiFilters);
        textEmail.setFilters(emojiFilters);
        textEmail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        getDateTime();
    }
    /**
     * 禁止输入框输入表情
     * */
    private static InputFilter emojiFilter = new InputFilter() {
        Pattern emoji = Pattern.compile(
                "[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
                Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest,
                                   int dstart,
                                   int dend) {
            Matcher emojiMatcher = emoji.matcher(source);
            if (emojiMatcher.find()) {
                return "";
            }
            return null;
        }
    };
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
        if (object instanceof RegisterBean) {
            RegisterBean registerBean = (RegisterBean) object;
            if (registerBean == null || !registerBean.isSuccess()) {
                ToastUtil.showToast(registerBean.getMessage());
            } else {
                ToastUtil.showToast(getResources().getString(R.string.register_successfully));
                Map<String, String> map = new HashMap<>();
                map.put("phone", phone);
                map.put("pwd", EncryptUtil.encrypt(pwd));
                doNetWorkPostRequest(Apis.URL_LOGIN_POST, map, LoginBean.class);
            }
        } else if (object instanceof LoginBean) {
            LoginBean loginBean = (LoginBean) object;
            if (loginBean == null || !loginBean.isSuccess()) {
                ToastUtil.showToast(loginBean.getMessage());
            } else {
                ToastUtil.showToast(getResources().getString(R.string.login_successfully));
                int userId = loginBean.getResult().getUserId();
                String sessionId = loginBean.getResult().getSessionId();
                //将userId和sessionId保存到本地
                preferences.edit().putString("userId", String.valueOf(userId)).putString("sessionId", sessionId).commit();
                Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        }
    }

    /**
     * 失败
     */
    @Override
    protected void netFail(String s) {
        ToastUtil.showToast(s);
    }

    @OnClick(R.id.sign_but)
    public void onViewClicked() {
        String nick = textNick.getText().toString().trim();
        String sex = textSex.getText().toString().trim();
        String date = textDate.getText().toString().trim();
        String email = textEmail.getText().toString().trim();
        phone = textPhone.getText().toString().trim();
        pwd = textPwd.getText().toString().trim();
        if (EmptyUtil.loginNull(nick, sex, date, email, phone, pwd)) {
            if (AccountValidatorUtil.isMobile(phone)) {
                if (AccountValidatorUtil.isEmail(email)) {
                    if (AccountValidatorUtil.isPassword(pwd)) {
                        int mSex = 1;
                        if (sex.equals("女")) {
                            mSex = 2;
                        }
                        Map<String, String> map = new HashMap<>();
                        map.put("nickName", nick);
                        map.put("sex", String.valueOf(mSex));
                        map.put("birthday", date);
                        map.put("phone", phone);
                        map.put("email", email);
                        map.put("pwd", EncryptUtil.encrypt(pwd));
                        map.put("pwd2", EncryptUtil.encrypt(pwd));
                        doNetWorkPostRequest(Apis.URL_REGISTER_USER_POST, map, RegisterBean.class);
                    } else {
                        ToastUtil.showToast(getResources().getString(R.string.is_pwd));
                    }
                } else {
                    ToastUtil.showToast(getResources().getString(R.string.register_email));
                }
            } else {
                ToastUtil.showToast(getResources().getString(R.string.register_phone));
            }
        } else {
            ToastUtil.showToast(getResources().getString(R.string.is_null));
        }
    }

    private void getDateTime() {
        //日期第三方
        textDate.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    showDatePickDlg();
                    return true;
                }
                return false;
            }
        });
        textDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDatePickDlg();
                }
            }
        });
    }

    //第三方控件  日期格式
    protected void showDatePickDlg() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                RegisterActivity.this.textDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
}
