package com.jkt.tnetprogress;

import android.support.annotation.Nullable;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.BufferedSource;
import okio.Okio;

/**
 * Created by Allen at 2017/6/13 17:15
 */
public class WrapResponseBody extends ResponseBody {
    private ResponseBody mResponseBody;
    private OnDownloadListener mListener;
    private ProgressInfo mInfo;

    public WrapResponseBody(ResponseBody responseBody, ProgressInfo info, OnDownloadListener listener) {
        mResponseBody = responseBody;
        mInfo = info;
        mListener = listener;
    }

    @Nullable
    @Override
    public MediaType contentType() {
        return mResponseBody.contentType();
    }

    @Override
    public long contentLength() {
        return mResponseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        mInfo.setContentLength(contentLength());
        WrapSource wrapSource = new WrapSource(mResponseBody.source(), mInfo, mListener);
        return Okio.buffer(wrapSource);
    }
}
