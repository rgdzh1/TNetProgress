package com.jkt.tnetprogress;

import android.os.Handler;
import android.os.Looper;

import java.io.IOException;

import okio.Buffer;
import okio.ForwardingSource;
import okio.Source;

/**
 * Created by Allen at 2017/6/13 10:52
 */
public class WrapSource extends ForwardingSource {
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private Source mSource;
    private ProgressInfo mInfo;
    private OnDownloadListener mListener;

    public WrapSource(Source source, ProgressInfo info, OnDownloadListener listener) {
        super(source);
        mSource = source;
        mInfo = info;
        mListener = listener;
    }

    @Override
    public long read(Buffer sink, long byteCount) throws IOException {
        long read = super.read(sink, byteCount);
        if (read != -1) {
            long l = mInfo.getCurrentLength() + read;
            mInfo.setCurrentLength(l);
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mListener.onDownLoadProgress(mInfo);
                }
            });
        }
        return read;
    }

}
