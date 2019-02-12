package com.bw.movie.seat.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bw.movie.R;
import com.bw.movie.activity.BaseActivity;
import com.bw.movie.seat.customview.SeatTable;

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
    /**
      * @作者 GXY
      * @创建日期 2019/2/12 15:21
      * @描述 初始化数据
      *
      */
    @Override
    protected void initData() {
        //选座
        selectView();
    }
    /**
      * @作者 GXY
      * @创建日期 2019/2/12 15:24
      * @描述 选座
      *
      */
    private void selectView() {
       /* //影厅座位总数
        int seatsTotal = resultBean.getSeatsTotal();
        //已售座位数
        seatsUseCount = resultBean.getSeatsUseCount();
        Random rand = new Random();
        num = rand.nextInt(9);
        num1 = rand.nextInt(9);
        Log.i("TAG",num+"========================"+num1);
        //设置屏幕名称
        seattable.setScreenName(resultBean.getScreeningHall()+"荧幕");
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
                for (int i =0;i<seatsUseCount;i++){
                    if(isValidSeat(num,num1)){
                        if (row == num+i && column == num1+i) {
                            return true;
                        }
                    }else{
                        num++;
                    }

                }
                return false;
            }

            @Override
            public void checked(int row, int column) {
                totalPrice+=resultBean.getPrice();
                String totalprice = String.format("%.2f", totalPrice);
                price.setText(totalprice+"");
                numCount++;
            }

            @Override
            public void unCheck(int row, int column) {
                totalPrice-=resultBean.getPrice();
                String totalprice = String.format("%.2f", totalPrice);
                price.setText(totalprice+"");
                numCount--;

            }

            @Override
            public String[] checkedSeatTxt(int row, int column) {
                return null;
            }

        });
        //设置影厅总座位数
        if (seatsTotal<=10){
            seattable.setData(1, seatsTotal+1);
        } else if (seatsTotal>10||seatsTotal<=100){
            seattable.setData(5, seatsTotal/10+1);
        }else if (seatsTotal>100){
            seattable.setData(10, seatsTotal/10+1);
        }*/
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
  *
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
        spannableString.setSpan(sizeSpan02, spannableString.length()-1, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        price.setText(spannableString);
    }
    /**
      * @作者 GXY
      * @创建日期 2019/2/12 15:22
      * @描述 加载布局
      *
      */
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_seat;
    }
    /**
      * @作者 GXY
      * @创建日期 2019/2/12 15:22
      * @描述 成功
      *
      */
    @Override
    protected void netSuccess(Object object) {

    }
    /**
      * @作者 GXY
      * @创建日期 2019/2/12 15:23
      * @描述 失败
      *
      */
    @Override
    protected void netFail(String s) {

    }
    @OnClick({R.id.pay_ok, R.id.pay_fail})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pay_ok:
                break;
            case R.id.pay_fail:
                break;
                default:
                    break;
        }
    }
}
