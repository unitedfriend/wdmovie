package com.bw.movie.wxapi;

import android.content.Context;

import com.bw.movie.application.MyApplication;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
  * @作者 GXY
  * @创建日期 2019/1/28 11:41
  * @描述 微信工具类
  *
  */
public class WeiXinUtil {
    // APP_ID 替换为你的应用从官方网站申请到的合法appID
    public static  String APP_ID = "wxb3852e6a6b7d9516";

    // IWXAPI 是第三方app和微信通信的openApi接口
    private WeiXinUtil() {

    }
    public  static  boolean success(Context context){
        //判断是否安装过微信
        if (WeiXinUtil.reg(context).isWXAppInstalled()) {
            return  true;
        }else {
            return false;
        }
    }
    public static IWXAPI reg(Context context) {
        if (context != null) {
            //AppConst.WEIXIN.APP_ID是指你应用在微信开放平台上的AppID，记得替换。
            IWXAPI wxapi = WXAPIFactory.createWXAPI(context, APP_ID, true);
            //注册到微信
            wxapi.registerApp(APP_ID);
            return wxapi;
        } else {
            return null;
        }
    }

    /**
      * @作者 GXY
      * @创建日期 2019/2/13 14:45
      * @描述 微信支付
      *
      */
    //支付
    public static void  weiXinPay(WXPayBean bean){
        IWXAPI wxapi = WXAPIFactory.createWXAPI(MyApplication.getApplication(), APP_ID, true);
        //注册到微信
        wxapi.registerApp(APP_ID);
        PayReq payReq = new PayReq();
        payReq.appId=bean.getAppId();
        payReq.prepayId=bean.getPrepayId();
        payReq.partnerId=bean.getPartnerId();
        payReq.nonceStr=bean.getNonceStr();
        payReq.timeStamp=bean.getTimeStamp();
        payReq.sign=bean.getSign();
        payReq.packageValue=bean.getPackageValue();
        wxapi.sendReq(payReq);
    }
}
