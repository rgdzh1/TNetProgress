package com.jkt.tnetprogress;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Allen at 2017/6/13 18:14
 */
public class UploadInterceptor implements Interceptor {
    private OnUploadListener mListener;

    public UploadInterceptor(OnUploadListener listener) {
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
        return response;
    }

    private Request wrapRequest(Request request) {
        if (request == null || request.body() == null) {
            return request;
        }
        Request.Builder builder = request.newBuilder();
        ProgressInfo info = new ProgressInfo();
        HttpUrl url = request.url();
        info.setUrl(url.toString());
        info.setTime(System.currentTimeMillis()+"");
        builder.method(request.method(),new WrapRequestBody(request.body(),info,mListener));
        return request;
    }

}
