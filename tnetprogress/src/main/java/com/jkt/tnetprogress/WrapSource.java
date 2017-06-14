package com.jkt.tnetprogress;

import java.io.IOException;

import okio.Buffer;
import okio.Source;
import okio.Timeout;

/**
 * Created by Allen at 2017/6/13 10:52
 */
public class WrapSource implements Source {
    private Source mSource;
    private ProgressInfo mInfo;
    private OnDownloadListener mListener;

    public WrapSource(Source source, ProgressInfo info, OnDownloadListener listener) {
        mSource = source;
        mInfo = info;
        mListener = listener;
    }

    @Override
    public long read(Buffer sink, long byteCount) throws IOException {
        mInfo.setCurrentLength(byteCount);
        mListener.onDownLoadProgress(mInfo);
        return mSource.read(sink,byteCount);
    }

    @Override
    public Timeout timeout() {
        return mSource.timeout();
    }

    @Override
    public void close() throws IOException {
          mSource.close();
    }
}
