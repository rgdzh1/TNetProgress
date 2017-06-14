package com.jkt.tnetprogress;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Created by Allen at 2017/6/13 18:14
 */
public class DownloadInterceptor implements Interceptor {
    private OnDownloadListener mListener;

    public DownloadInterceptor( OnDownloadListener listener) {
        mListener = listener;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = wrapResponse(chain.proceed(chain.request()));
        return response;
    }

    private Response wrapResponse(Response response) {
        if (response == null || response.body() == null) {
            return response;
        }
        Response wrapResponse = getWrapResponse(response);
        return wrapResponse;
    }

    private Response getWrapResponse(Response response) {
        HttpUrl url = response.request().url();
        ProgressInfo info = new ProgressInfo();
        info.setTime(System.currentTimeMillis()+"");
        info.setUrl(url.toString());
        Response.Builder builder = response.newBuilder();
        return builder.body(new WrapResponseBody(response.body(),info,mListener)).build();
    }


}
