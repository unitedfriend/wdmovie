package com.bw.movie.application;



import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.tencent.bugly.crashreport.CrashReport;

public class MyApplication extends Application {
    private static Context mContext;
    private RefWatcher refWatcher;
    @Override
    public void onCreate() {
        super.onCreate();
        //LeakCanary内存泄露检测
        refWatcher = LeakCanary.install(this);
        //设置磁盘缓存
        DiskCacheConfig cacheConfig = DiskCacheConfig.newBuilder(this)
                .setBaseDirectoryName("images")
                .setBaseDirectoryPath(Environment.getExternalStorageDirectory())
                .build();
        //设置磁盘缓存的配置,生成配置文件
        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(this)
                .setMainDiskCacheConfig(cacheConfig)
                .build();
        Fresco.initialize(this, config);

        mContext = getApplicationContext();

        //android 7.0调用相机闪退问题
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
        CrashReport.initCrashReport(getApplicationContext(), "7fb01ba51c", false);
    }
    public static Context getApplication() {
        return mContext;
    }
    //LeakCanary内存泄露检测的方法
    public static RefWatcher getRefWatcher(Context mContext){
        MyApplication myApplication  = (MyApplication) mContext.getApplicationContext();
        return myApplication.refWatcher;
    }

}
