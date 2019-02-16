package com.bw.movie.register.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
    TextView textDate;
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
        textSex.setFilters(emojiFilters);
        textDate.setFilters(emojiFilters);
        textPwd.setFilters(emojiFilters);

        textDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //收回软件盘
                InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(RegisterActivity.this.getWindow().getDecorView().getWindowToken(), 0);

                //获取系统时间
                Calendar calendar = Calendar.getInstance();
                final int year = calendar.get(Calendar.YEAR);
                final int month = calendar.get(Calendar.MONTH);
                final int day = calendar.get(Calendar.DAY_OF_MONTH);

                Calendar startDate = Calendar.getInstance();
                Calendar endDate = Calendar.getInstance();
                calendar.set(year - 10, month, day);
                startDate.set(year - 100, 0, 1);
                endDate.set(year, month, day);
                TimePickerView pvTime = new TimePickerBuilder(RegisterActivity.this, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        String time = sDateFormat.format(date);
                        textDate.setText(time + "");
                    }
                })
                        .setType(new boolean[]{true, true, true, false, false, false})
                        // 默认全部显示
                        .setCancelText("取消")
                        //取消按钮文字
                        .setSubmitText("确定")
                        //确认按钮文字
                        .setOutSideCancelable(true)
                        //点击屏幕，点在控件外部范围时，是否取消显示
                        .setRangDate(startDate, endDate)
                        //起始终止年月日设定
                        .setDate(calendar)
                        //设置默认时间
                        .isCenterLabel(false)
                        //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                        .build();
                pvTime.show();
            }
        });
    }
    /**
     * 禁止输入框输入表情
     */
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
                /*Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                startActivity(intent);*/
                finish();
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
}
