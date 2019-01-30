package com.bw.movie.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.bw.movie.application.MyApplication;

/**
  * @作者 GXY
  * @创建日期 2019/1/24 9:07
  * @描述 判断网络是否可用的工具类
  *
  */
public class NetUtil {
    //判断是否有网络
    public static boolean hasNetWork(){
        ConnectivityManager cm = (ConnectivityManager) MyApplication.getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
        return activeNetworkInfo!=null&&activeNetworkInfo.isAvailable();
    }

    //弹框提示网络的方法
    public static AlertDialog showNotNetWork(final Context context) {
        final AlertDialog show = new AlertDialog.Builder(context)
                .setTitle("无网络")
                .setMessage("当前网络不可用，是否去设置？")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent("android.settings.WIRELESS_SETTINGS");
                        context.startActivity(intent);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
        return show;
    }

}
