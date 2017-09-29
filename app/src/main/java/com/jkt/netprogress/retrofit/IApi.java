package com.jkt.netprogress.retrofit;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Allen at 2017/6/15 11:35
 */
public interface IApi {
    @GET("timg?image&quality=80&size=b9999_10000&sec=1506664930570&di=6dace2ed4c25580c7446a4ef0807b928&imgtype=0&src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F6%2F57eb31505b1fd.jpg")
    public Observable<ResponseBody> getDownload();

    @POST("uc/services/rest")
    public Observable<ResponseBody> getUpload(@Body RequestBody requestBody);

}
