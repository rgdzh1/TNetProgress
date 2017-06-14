package com.jkt.tnetprogress;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
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
        Request request = wrapRequest(chain.request());
        Response response = wrapResponse(chain.proceed(request));
        return response;
    }

    private Response wrapResponse(Response response) {
        if (response == null || response.body() == null) {
            return response;
        }
        Response.Builder builder = response.newBuilder();
        ProgressInfo info = new ProgressInfo();
        info.setTime(System.currentTimeMillis()+"");
        builder.body(new WrapResponseBody(response.body(),info,mListener));
        return response;
    }

    private Request wrapRequest(Request request) {
        if (request == null || request.body() == null) {
            return request;
        }
        return request;
    }

}
