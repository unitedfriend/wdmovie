package com.bw.movie.wxapi;

import android.content.Intent;
import android.os.Bundle;

import com.bw.movie.R;
import com.bw.movie.activity.BaseActivity;
import com.bw.movie.application.MyApplication;
import com.bw.movie.util.ToastUtil;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler {
    private IWXAPI mIwxapi;
    @Override
    protected void initData() {

    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mIwxapi = WXAPIFactory.createWXAPI(MyApplication.getApplication(), WeiXinUtil.APP_ID);
        mIwxapi.handleIntent(getIntent(), this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_wxpay;
    }

    @Override
    protected void netSuccess(Object object) {

    }

    @Override
    protected void netFail(String s) {

    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        if (baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            int errCord = baseResp.errCode;
            if (errCord == 0) {
                ToastUtil.showToast("支付成功！");

            } else if (errCord == -1) {
                ToastUtil.showToast("支付失败");
            } else {
                ToastUtil.showToast("用户取消了");
            }
            //这里接收到了返回的状态码可以进行相应的操作，如果不想在这个页面操作可以把状态码存在本地然后finish掉这个页面，这样就回到了你调起支付的那个页面
            //获取到你刚刚存到本地的状态码进行相应的操作就可以了
            finish();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        mIwxapi.handleIntent(intent, this);
    }
}
