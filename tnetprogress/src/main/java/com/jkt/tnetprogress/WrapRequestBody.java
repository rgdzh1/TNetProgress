package com.jkt.tnetprogress;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;
import okio.Okio;

/**
 * Created by Allen at 2017/6/13 17:01
 */
public class WrapRequestBody extends RequestBody {
    private RequestBody mRequestBody;
    private OnUploadListener mListener;
    private ProgressInfo mInfo;


    public WrapRequestBody(RequestBody requestBody, ProgressInfo info, OnUploadListener listener) {
        mRequestBody = requestBody;
        mListener = listener;
        mInfo = info;
    }


    @Override
    public MediaType contentType() {
        return mRequestBody.contentType();
    }

    @Override
    public long contentLength() throws IOException {
        try {
            return mRequestBody.contentLength();
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        mInfo.setContentLength(contentLength());
        WrapSink wrapSink = new WrapSink(sink, mInfo, mListener);
        BufferedSink buffer = Okio.buffer(wrapSink);
        mRequestBody.writeTo(buffer);
        buffer.flush();
    }
}
