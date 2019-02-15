package com.bw.movie.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;

/**
  * @作者 GXY
  * @创建日期 2019/1/28 15:50
  * @描述 版本升级
  *
  */
public class VersionUtil {
    /**
      * @作者 GXY
      * @创建日期 2019/2/14 16:31
      * @描述 调用第三方浏览器打开
      *
      */
    public static  void openBrowser(Context context,String url){
        final Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        // 注意此处的判断intent.resolveActivity()可以返回显示该Intent的Activity对应的组件名
        // 官方解释 : Name of the component implementing an activity that can display the intent
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            final ComponentName componentName = intent.resolveActivity(context.getPackageManager());
            context.startActivity(Intent.createChooser(intent, "请选择浏览器"));
        } else {
            ToastUtil.showToast("请下载浏览器");
        }
    }
}
