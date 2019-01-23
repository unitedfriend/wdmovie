package com.bw.movie.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.bw.movie.application.MyApplication;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RetrofitManager {
    private final String BASE_URL = "http://mobile.bwstudent.com/";
    private final BaseApis baseApis;

    /**
     * 静态内部类的单例模式
     */
    public static RetrofitManager getInstance() {
        return SingletonHolder.instance;
    }

    private static class SingletonHolder {
        private static final RetrofitManager instance = new RetrofitManager();
    }

    //无参构造
    private RetrofitManager() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.readTimeout(15, TimeUnit.SECONDS);
        builder.writeTimeout(15, TimeUnit.SECONDS);
        builder.connectTimeout(15, TimeUnit.SECONDS);
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                SharedPreferences preferences = MyApplication.getApplication().getSharedPreferences("User", Context.MODE_PRIVATE);
                String ak = preferences.getString("ak", "0110010010000");
                String Content_Type = preferences.getString("Content-Type", "application/x-www-form-urlencoded");
                String userId = preferences.getString("userId", "");
                String sessionId = preferences.getString("sessionId", "");
                Request.Builder builder1 = request.newBuilder();
                builder1.method(request.method(), request.body());
                builder1.addHeader("ak", ak);
                builder1.addHeader("Content_Type", Content_Type);
                if (!TextUtils.isEmpty(userId) && !TextUtils.isEmpty(sessionId)) {
                    builder1.addHeader("userId", userId);
                    builder1.addHeader("sessionId", sessionId);
                }

                Request build = builder1.build();

                return chain.proceed(build);
            }
        });
        builder.retryOnConnectionFailure(true);
        OkHttpClient build = builder.build();
        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .client(build)
                .build();
        baseApis = retrofit.create(BaseApis.class);
    }

    /**
     * get
     */
    public void get(String url, HttpListener listener) {
        baseApis.get(url)
                //后台执行在哪个线程
                .subscribeOn(Schedulers.io())
                //最终完成后执行在哪个线程
                .observeOn(AndroidSchedulers.mainThread())
                //设置rxjava
                .subscribe(getObserver(listener));
    }

    /**
     * 普通post
     */
    public void post(String url, Map<String, String> map, HttpListener listener) {
        if (map == null) {
            map = new HashMap<>();
        }
        baseApis.post(url, map)
                // 后台执行在哪个线程
                .subscribeOn(Schedulers.io())
                //最终完成后执行在哪个线程
                .observeOn(AndroidSchedulers.mainThread())
                //设置rxjava
                .subscribe(getObserver(listener));

    }


    /**
     * 上传头像
     */
    public static MultipartBody filesMultipar(Map<String, String> map) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            File file = new File(entry.getValue());
            builder.addFormDataPart(entry.getKey(), "tp.png",
                    RequestBody.create(MediaType.parse("multipart/form-data"), file));
        }
        builder.setType(MultipartBody.FORM);
        MultipartBody multipartBody = builder.build();
        return multipartBody;
    }

    public void imagePost(String url, Map<String, String> map, HttpListener listener) {
        if (map == null) {
            map = new HashMap<>();
        }
        MultipartBody multipartBody = filesMultipar(map);
        baseApis.imagePost(url, multipartBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver(listener));
    }

    /**
     * 观察者
     */

    private Observer getObserver(final HttpListener listener) {
        Observer observer = new Observer<ResponseBody>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (listener != null) {
                    listener.onFail(e.getMessage());
                }
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    String data = responseBody.string();
                    if (null != listener) {
                        listener.onSuccess(data);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    if (listener != null) {
                        listener.onFail(e.getMessage());
                    }
                }
            }
        };
        return observer;
    }

    //定义接口
    public interface HttpListener {
        void onSuccess(String data);

        void onFail(String error);
    }
}
