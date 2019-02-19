package com.bw.movie.seat.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bw.movie.R;
import com.bw.movie.activity.BaseActivity;
import com.bw.movie.api.Apis;
import com.bw.movie.camera.bean.CinemaMovieListBean;
import com.bw.movie.login.activity.LoginActivity;
import com.bw.movie.seat.bean.OrderBean;
import com.bw.movie.seat.customview.SeatTable;
import com.bw.movie.util.Md5Utils;
import com.bw.movie.util.ToastUtil;
import com.bw.movie.wxapi.WXPayBean;
import com.bw.movie.wxapi.WeiXinUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @作者 GXY
 * @创建日期 2019/2/12 13:35
 * @描述 选座Activity
 */
public class SeatActivity extends BaseActivity {
    @BindView(R.id.seat_image)
    ImageView seatImage;
    @BindView(R.id.bg_image)
    ImageView bgImage;
    @BindView(R.id.cinema_name)
    TextView cinemaName;
    @BindView(R.id.cinema_address)
    TextView cinemaAddress;
    @BindView(R.id.film_name)
    TextView filmName;
    @BindView(R.id.tv_text)
    TextView tvText;
    @BindView(R.id.linear_layout)
    LinearLayout linearLayout;
    @BindView(R.id.seattable)
    SeatTable seattable;
    @BindView(R.id.text_total)
    TextView textTotal;
    @BindView(R.id.price)
    TextView price;
    @BindView(R.id.pay_ok)
    ImageView payOk;
    @BindView(R.id.pay_fail)
    ImageView payFail;
    @BindView(R.id.relative_layout)
    RelativeLayout relativeLayout;
    private SharedPreferences.Editor edit;
    private SharedPreferences preferences;
    private CinemaMovieListBean.ResultBean resultBean;
    private int num;
    private int num1;
    private int seatsUseCount;
    private double totalPrice;
    private int numCount = 0;
    private PopupWindow popupWindow;
    private int pay = 1;
    private String orderId;
    private TextView confirm_pay;

    /**
     * @作者 GXY
     * @创建日期 2019/2/12 15:21
     * @描述 初始化数据
     */
    @Override
    protected void initData() {
        Intent intent = getIntent();
        String movieName = intent.getStringExtra("movieName");
        String name = intent.getStringExtra("name");
        String address = intent.getStringExtra("address");
        resultBean = (CinemaMovieListBean.ResultBean) intent.getSerializableExtra("resultBean");
        cinemaName.setText(name);
        cinemaAddress.setText(address);
        filmName.setText(movieName);
        tvText.setText("\t" + resultBean.getBeginTime() + "-" + resultBean.getEndTime() + "\t\t\t" + resultBean.getScreeningHall());
        //选座
        selectView();
    }

    /**
     * @作者 GXY
     * @创建日期 2019/2/12 15:24
     * @描述 选座
     */
    private void selectView() {
        //影厅座位总数
        int seatsTotal = resultBean.getSeatsTotal();
        //已售座位数
        seatsUseCount = resultBean.getSeatsUseCount();
        Random rand = new Random();
        num = rand.nextInt(9);
        num1 = rand.nextInt(9);
        //设置屏幕名称
        seattable.setScreenName(resultBean.getScreeningHall() + "荧幕");
        //设置最多选中
        seattable.setMaxSelected(3);
        seattable.setSeatChecker(new SeatTable.SeatChecker() {
            @Override
            public boolean isValidSeat(int row, int column) {
                if (column == 2) {
                    return false;
                }
                return true;
            }

            @Override
            public boolean isSold(int row, int column) {
                for (int i = 0; i < seatsUseCount; i++) {
                    if (isValidSeat(num, num1)) {
                        if (row == num + i && column == num1 + i) {
                            return true;
                        }
                    } else {
                        num++;
                    }

                }
                return false;
            }

            @Override
            public void checked(int row, int column) {
                totalPrice += resultBean.getPrice();
                String totalprice = String.format("%.2f", totalPrice);
                price.setText(totalprice + "");
                numCount++;
            }

            @Override
            public void unCheck(int row, int column) {
                totalPrice -= resultBean.getPrice();
                String totalprice = String.format("%.2f", totalPrice);
                price.setText(totalprice + "");
                numCount--;
            }

            @Override
            public String[] checkedSeatTxt(int row, int column) {
                return null;
            }
        });
        //设置影厅总座位数
        if (seatsTotal <= 10) {
            seattable.setData(1, seatsTotal + 1);
        } else if (seatsTotal > 10 || seatsTotal <= 100) {
            seattable.setData(5, seatsTotal / 10 + 1);
        } else if (seatsTotal > 100) {
            seattable.setData(10, seatsTotal / 10 + 1);
        }
    }

    /**
     * @作者 GXY
     * @创建日期 2019/2/12 15:21
     * @描述 初始化view
     */
    @Override
    protected void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        preferences = getSharedPreferences("User", Context.MODE_PRIVATE);
        edit = preferences.edit();
        //设置文字样式
        getTextView();
    }

    /**
     * @作者 GXY
     * @创建日期 2019/2/12 15:08
     * @描述 设置文字样式
     */
    private void getTextView() {
        //设置颜色
        SpannableString spannableString = new SpannableString("¥\t\t0.0");
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#666666"));
        spannableString.setSpan(colorSpan, 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        //设置文字大小
        RelativeSizeSpan sizeSpan01 = new RelativeSizeSpan(0.5f);
        RelativeSizeSpan sizeSpan02 = new RelativeSizeSpan(0.5f);
        spannableString.setSpan(sizeSpan01, 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(sizeSpan02, spannableString.length() - 1, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        price.setText(spannableString);
    }

    /**
     * @作者 GXY
     * @创建日期 2019/2/12 15:22
     * @描述 加载布局
     */
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_seat;
    }

    /**
     * @作者 GXY
     * @创建日期 2019/2/13 13:52
     * @描述 选择支付方式
     */
    private void getPayPopvView() {
        View view = View.inflate(SeatActivity.this, R.layout.pay_popupwindow_view, null);
        ImageView detail_down = view.findViewById(R.id.detail_down);
        RadioGroup radiogroup = view.findViewById(R.id.radiogroup);
      /*  RadioButton pay_wx= view.findViewById(R.id.pay_wx);
        RadioButton pay_alipay= view.findViewById(R.id.pay_alipay);*/
        confirm_pay = view.findViewById(R.id.confirm_pay);
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, 550);
        //设置焦点
        popupWindow.setFocusable(true);
        //设置是否可以触摸
        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
        //关闭
        detail_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.pay_wx:
                        pay = 1;
                        confirm_pay.setText("微信支付" + totalPrice + "元");
                        break;
                    case R.id.pay_alipay:
                        pay = 2;
                        confirm_pay.setText("支付宝支付" + totalPrice + "元");
                        break;
                    default:
                        break;
                }
            }
        });
        if (pay == 1) {
            confirm_pay.setText("微信支付" + totalPrice + "元");
        } else if (pay == 2) {
            confirm_pay.setText("支付宝支付" + totalPrice + "元");
        }
        //支付
        confirm_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> map = new HashMap<>();
                map.put("payType", String.valueOf(1));
                map.put("orderId", orderId);
                doNetWorkPostRequest(Apis.URL_PAY_POST, map, WXPayBean.class);
            }
        });
    }

    /**
     * @作者 GXY
     * @创建日期 2019/2/12 15:22
     * @描述 成功
     */
    @Override
    protected void netSuccess(Object object) {
        if (object instanceof OrderBean) {
            OrderBean orderBean = (OrderBean) object;
            orderId = orderBean.getOrderId();
            if (orderBean == null || !orderBean.isSuccess()) {
                ToastUtil.showToast(orderBean.getMessage());
                if(orderBean.getMessage().equals("请先登陆")){
                    startActivity(new Intent(SeatActivity.this,LoginActivity.class));
                    return;
                }
            } else {
                ToastUtil.showToast(orderBean.getMessage());
                //获取pop支付弹框
                getPayPopvView();
                //支付
                popupWindow.showAtLocation(View.inflate(SeatActivity.this, R.layout.activity_my_message, null),
                        Gravity.BOTTOM, 0, 0);
            }
        } else if (object instanceof WXPayBean) {
            WXPayBean wxPayBean = (WXPayBean) object;
            if (wxPayBean == null || !wxPayBean.isSuccess()) {
                ToastUtil.showToast(wxPayBean.getMessage());
                if(wxPayBean.getMessage().equals("请先登陆")){
                    startActivity(new Intent(SeatActivity.this,LoginActivity.class));
                    return;
                }
            } else {
                ToastUtil.showToast(wxPayBean.getMessage());
                WeiXinUtil.weiXinPay(wxPayBean);
                //finish();
            }
        }
    }

    /**
     * @作者 GXY
     * @创建日期 2019/2/12 15:23
     * @描述 失败
     */
    @Override
    protected void netFail(String s) {

    }

    @OnClick({R.id.pay_ok, R.id.pay_fail})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pay_ok:
                Map<String, String> map = new HashMap<>();
                map.put("scheduleId", String.valueOf(resultBean.getId()));
                map.put("amount", String.valueOf(numCount));
                String userId = preferences.getString("userId", null);
                String sessionId = preferences.getString("sessionId", null);
                int id = resultBean.getId();
                map.put("sign", Md5Utils.MD5(preferences.getString("userId", null) + resultBean.getId() + numCount + "movie"));
                doNetWorkPostRequest(Apis.URL_BUY_MOVIE_TICKET_POST, map, OrderBean.class);
                break;
            case R.id.pay_fail:

                break;
            default:
                break;
        }
    }
}
