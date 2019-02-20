package com.bw.movie.my.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.bw.movie.R;
import com.bw.movie.api.Apis;
import com.bw.movie.fragmnet.BaseFragment;
import com.bw.movie.login.activity.LoginActivity;
import com.bw.movie.my.activity.MyTicketrecordActivity;
import com.bw.movie.my.adaper.ObligationAdaper;
import com.bw.movie.my.bean.ObligationBean;
import com.bw.movie.seat.activity.SeatActivity;
import com.bw.movie.util.ToastUtil;
import com.bw.movie.wxapi.AliPayBean;
import com.bw.movie.wxapi.PayResult;
import com.bw.movie.wxapi.WXPayBean;
import com.bw.movie.wxapi.WeiXinUtil;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @作者 GXY
 * @创建日期 2019/1/28 19:21
 * @描述 用户购票记录查询列表  待付款
 */
public class ObligationFragment extends BaseFragment {
    @BindView(R.id.xrecycleview)
    XRecyclerView xrecycleview;
    Unbinder unbinder;
    private int mPage;
    private int mCount = 5;
    private int status = 1;
    private ObligationAdaper obligationAdaper;
    private PopupWindow popupWindow;
    private int pay = 1;
    private TextView confirm_pay;
    private double totalPrice;
    @Override
    protected void initData() {
        doNetWorkGetRequest(String.format(Apis.URL_FIND_USER_BUY_TICLET_RECORD_LIST_GET,mPage,mCount,status), ObligationBean.class);
    }
    /**
      * @作者 GXY
      * @创建日期 2019/2/20 18:49
      * @描述  创建handler
      */
    MyHandler myHandler = new MyHandler((MyTicketrecordActivity) getActivity());
    private static class MyHandler extends Handler {
        private final WeakReference<MyTicketrecordActivity> mActivity;
        public MyHandler(MyTicketrecordActivity activity){
            mActivity = new WeakReference<MyTicketrecordActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(mActivity.get() == null){
                PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                ToastUtil.showToast(payResult.getMemo());
                return;
            }
        }
    }
    @Override
    protected void initView(View view) {
        mPage = 1;
        unbinder = ButterKnife.bind(this, view);
        //创建布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        xrecycleview.setLayoutManager(layoutManager);
        //创建适配器
        obligationAdaper = new ObligationAdaper(getActivity());
        xrecycleview.setAdapter(obligationAdaper);
        //设置支持刷新加载
        xrecycleview.setPullRefreshEnabled(true);
        xrecycleview.setLoadingMoreEnabled(true);
        xrecycleview.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                mPage=1;
                initData();
            }

            @Override
            public void onLoadMore() {
                initData();
            }
        });
        obligationAdaper.setCallBackObligation(new ObligationAdaper.CallBackObligation() {
            @Override
            public void callBack(String orderId, int amount, double price) {
                //获取pop支付弹框
                getPayPopvView(orderId,amount,price);
                //支付
                popupWindow.showAtLocation(View.inflate(getActivity(), R.layout.activity_my_message, null),
                        Gravity.BOTTOM, 0, 0);
            }
        });
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.obligation_fragment;
    }

    @Override
    protected void netSuccess(Object object) {
        if(object instanceof ObligationBean){
            ObligationBean obligationBean = (ObligationBean) object;
            if(obligationBean == null || !obligationBean.isSuccess()){
                ToastUtil.showToast(obligationBean.getMessage());
            }else{
                if(mPage==1){
                    obligationAdaper.setmResult(obligationBean.getResult());
                }else{
                    obligationAdaper.addmResult(obligationBean.getResult());
                }
                mPage++;
                xrecycleview.loadMoreComplete();
                xrecycleview.refreshComplete();
            }
        }else if (object instanceof WXPayBean) {
            WXPayBean wxPayBean = (WXPayBean) object;
            if (wxPayBean == null || !wxPayBean.isSuccess()) {
                ToastUtil.showToast(wxPayBean.getMessage());
            } else {
                ToastUtil.showToast(wxPayBean.getMessage());
                WeiXinUtil.weiXinPay(wxPayBean);
                popupWindow.dismiss();
            }
        }else if(object instanceof AliPayBean){
            AliPayBean aliPayBean = (AliPayBean) object;
            if(aliPayBean == null || !aliPayBean.isSuccess()){
                ToastUtil.showToast(aliPayBean.getMessage());
                if(aliPayBean.getMessage().equals("请先登陆")){
                    startActivity(new Intent(getActivity(),LoginActivity.class));
                    return;
                }
            }else{
                ToastUtil.showToast(aliPayBean.getMessage());
                final String result = aliPayBean.getResult();
                final Runnable payRunnnable = new Runnable() {
                    @Override
                    public void run() {
                        PayTask payTask = new PayTask(getActivity());
                        Map<String, String> map = payTask.payV2(result, true);
                        Message message = new Message();
                        message.obj = map;
                        myHandler.sendMessage(message);
                    }
                };
                // 必须异步调用
                Thread payThread = new Thread(payRunnnable);
                payThread.start();
                popupWindow.dismiss();
            }
        }
    }
    /**
      * @作者 GXY
      * @创建日期 2019/2/14 16:10
      * @描述 选择支付方式
      *
      */
    private void getPayPopvView(final String orderId, final int amount, final double price ) {
        View view = View.inflate(getActivity(), R.layout.pay_popupwindow_view, null);
        ImageView detail_down = view.findViewById(R.id.detail_down);
        RadioGroup radiogroup = view.findViewById(R.id.radiogroup);
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
                        confirm_pay.setText("微信支付" + amount*price + "元");
                        break;
                    case R.id.pay_alipay:
                        pay = 2;
                        confirm_pay.setText("支付宝支付" + amount*price + "元");
                        break;
                    default:
                        break;
                }
            }
        });
        if (pay == 1) {
            confirm_pay.setText("微信支付" + amount*price + "元");
        } else if (pay == 2) {
            confirm_pay.setText("支付宝支付" + amount*price + "元");
        }
        //支付
        confirm_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pay == 1) {
                    Map<String, String> map = new HashMap<>();
                    map.put("payType", String.valueOf(1));
                    map.put("orderId", orderId);
                    doNetWorkPostRequest(Apis.URL_PAY_POST, map, WXPayBean.class);
                } else if (pay == 2) {
                    Map<String, String> map = new HashMap<>();
                    map.put("payType", String.valueOf(2));
                    map.put("orderId", orderId);
                    doNetWorkPostRequest(Apis.URL_PAY_POST, map, AliPayBean.class);
                }
            }
        });
    }
    @Override
    protected void netFail(String s) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        myHandler.removeCallbacksAndMessages(null);
    }
}
