package com.jkt.tnetprogress;

import java.io.IOException;

import okio.Buffer;
import okio.ForwardingSink;
import okio.Sink;

/**
 * Created by Allen at 2017/6/13 10:52
 */
public class WrapSink extends ForwardingSink{
    public OnUploadListener mListener;
    public ProgressInfo mInfo;
    public WrapSink(Sink delegate,ProgressInfo info,OnUploadListener listener) {
        super(delegate);
        mInfo=info;
        mListener=listener;
    }

    @Override
    public void write(Buffer source, long byteCount) throws IOException {
        super.write(source, byteCount);
        mInfo.setCurrentLength(byteCount);
        mListener.onUpLoadProgress(mInfo);
    }
}
