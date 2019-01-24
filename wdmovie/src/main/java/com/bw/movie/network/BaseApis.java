package com.bw.movie.network;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.PartMap;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;
/**
  * @作者 GXY
  * @创建日期 2019/1/24 9:46
  * @描述 网络请求方式接口
  *
  */
public interface BaseApis<E> {
    /**
     * Observable被观察者
     * */
    @GET
    Observable<ResponseBody> get(@Url String url);

    @FormUrlEncoded
    @POST
    Observable<ResponseBody> post(@Url String url, @FieldMap Map<String, String> map);

    @POST
    Observable<ResponseBody> imagePost(@Url String url, @Body MultipartBody multipartBody);
}
