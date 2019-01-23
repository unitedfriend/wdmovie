package com.bw.movie.network;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.PartMap;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;
/**
 * 网络请求方式接口
 * guoxinyu
 * */
public interface BaseApis<E> {
    /**
     * Observable被观察者
     * */
    @GET
    Observable<ResponseBody> get(@Url String url);

    @POST
    Observable<ResponseBody> post(@Url String url, @QueryMap Map<String, String> map);

    @POST
    Observable<ResponseBody> imagePost(@Url String url, @Body MultipartBody multipartBody);
}
