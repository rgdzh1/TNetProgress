package com.jkt.netprogress.retrofit;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Allen at 2017/6/15 11:35
 */
public interface IApi {
    @GET("wallpaper/a/568cd27741af5.jpg")
    public Observable<String> getDownload();

    @POST("uc/services/rest")
    public Observable<String> getUpload(@Body RequestBody requestBody);

}
