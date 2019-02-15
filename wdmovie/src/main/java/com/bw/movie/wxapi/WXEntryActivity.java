package com.bw.movie.wxapi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.bw.movie.R;
import com.bw.movie.activity.BaseActivity;
import com.bw.movie.api.Apis;
import com.bw.movie.home.activity.HomeActivity;
import com.bw.movie.util.ToastUtil;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

import java.util.HashMap;
import java.util.Map;

public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler {
    private SharedPreferences preferences;
    private SharedPreferences.Editor edit;
    @Override
    protected void initData() {

    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        WeiXinUtil.reg(WXEntryActivity.this).handleIntent(getIntent(), this);
        preferences = getSharedPreferences("User", MODE_PRIVATE);
        edit = preferences.edit();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_wxentry;
    }

    @Override
    protected void netSuccess(Object object) {
        if (object instanceof WeiXinBean) {
            WeiXinBean xinBean = (WeiXinBean) object;
            if (xinBean == null || !xinBean.isSuccess()) {
                ToastUtil.showToast(xinBean.getMessage());
            } else {
                int userId = xinBean.getResult().getUserId();
                String sessionId = xinBean.getResult().getSessionId();
                //将userId和sessionId保存到本地
                preferences.edit().putString("userId", String.valueOf(userId)).putString("sessionId", sessionId).commit();
                /*Intent intent = new Intent(WXEntryActivity.this, HomeActivity.class);
                intent.putExtra("pic", xinBean.getResult().getUserInfo().getHeadPic());
                intent.putExtra("name", xinBean.getResult().getUserInfo().getNickName());
                startActivity(intent);*/
                finish();
            }
        }
    }

    @Override
    protected void netFail(String s) {

    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(final BaseResp baseResp) {
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                //主线程
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //得到code
                        String code = ((SendAuth.Resp) baseResp).code;
                        Map<String, String> map = new HashMap<>();
                        map.put("code", code);
                        doNetWorkPostRequest(Apis.URL_WE_CHAT_BINDING_LOGIN_POST, map, WeiXinBean.class);
                    }
                });
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:

                break;
            default:
                break;
        }

    }

}
