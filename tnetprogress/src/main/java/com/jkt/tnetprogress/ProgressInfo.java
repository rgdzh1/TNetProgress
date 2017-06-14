package com.jkt.tnetprogress;

/**
 * Created by Allen at 2017/6/13 17:27
 */
public class ProgressInfo {
    public String mUrl;
    public String mTime;
    public long mContentLength;
    public long mCurrentLength;
    public float mPercent;

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public String getTime() {
        return mTime;
    }

    public void setTime(String time) {
        mTime = time;
    }

    public long getContentLength() {
        return mContentLength;
    }

    public void setContentLength(long contentLength) {
        mContentLength = contentLength;
    }

    public long getCurrentLength() {
        return mCurrentLength;
    }

    public void setCurrentLength(long currentLength) {
        mCurrentLength = currentLength;
    }

    public float getPercent() {
        return mContentLength==0?0:mCurrentLength/(float)mContentLength;
    }

}
