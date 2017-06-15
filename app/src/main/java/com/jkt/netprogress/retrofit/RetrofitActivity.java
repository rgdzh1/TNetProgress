package com.jkt.netprogress.retrofit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.jkt.netprogress.FileUtil;
import com.jkt.netprogress.R;
import com.jkt.tnetprogress.DownloadInterceptor;
import com.jkt.tnetprogress.OnDownloadListener;
import com.jkt.tnetprogress.OnUploadListener;
import com.jkt.tnetprogress.ProgressInfo;
import com.jkt.tnetprogress.UploadInterceptor;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Subscriber;

public class RetrofitActivity extends AppCompatActivity implements View.OnClickListener, OnDownloadListener, OnUploadListener {

    private Button mDownloadBN;
    private ImageView mIV;
    private Button mUploadBN;
    private String mDownloadUrl;
    private String mUploadUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        initObjects();
        initListeners();
    }



    private void initViews() {
        setContentView(R.layout.activity_retrofit);
        mDownloadBN = (Button) findViewById(R.id.retrofit_download_bn);
        mIV = (ImageView) findViewById(R.id.retrofit_iv);
        mUploadBN = (Button) findViewById(R.id.retrofit_upload_bn);
    }
    private void initObjects() {
        mDownloadUrl = "http://pic1.win4000.com/wallpaper/a/568cd27741af5.jpg";
        mUploadUrl = "http://v.polyv.net/uc/services/rest";
    }
    private void initListeners() {
        mDownloadBN.setOnClickListener(this);
        mUploadBN.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.retrofit_download_bn:
                downland();
                break;
            case R.id.retrofit_upload_bn:
                upload();
                break;
        }
    }


    private void downland() {

        HttpMethods.getInstance()
                  .getDownload(new Subscriber<String>() {
                      @Override
                      public void onCompleted() {

                      }

                      @Override
                      public void onError(Throwable e) {

                      }

                      @Override
                      public void onNext(String s) {

                      }
                  },new DownloadInterceptor(this));
    }

    private void upload() {
        File file = FileUtil.getFromAssets(this,"a.jpg");
        RequestBody body = RequestBody.create(MediaType.parse("multipart/form-data"), file);
       HttpMethods.getInstance().getUpload(new Subscriber<String>() {
           @Override
           public void onCompleted() {

           }

           @Override
           public void onError(Throwable e) {

           }

           @Override
           public void onNext(String s) {

           }
       },body,new UploadInterceptor(this));
    }

    @Override
    public void onDownLoadProgress(ProgressInfo info) {
        //注意info的url不包含参数键值对,打印查看
        if (mDownloadUrl.equals(info.getUrl())) {
            if (info.getPercentFloat() == 1) {
                mDownloadBN.setText("下载完成   总尺寸" + String.format("Size : %s", FileUtil.getFileSize(info.getContentLength())));
                mDownloadBN.setEnabled(true);
                return;
            }
            mDownloadBN.setText("下载:" + info.getPercentString());
        }
    }

    @Override
    public void onUpLoadProgress(ProgressInfo info) {
        //注意info的url不包含参数键值对,打印查看
        if (mUploadUrl.equals(info.getUrl())) {
            if (info.getPercentFloat() == 1) {
                mUploadBN.setText("上传完成   总尺寸" + String.format("Size : %s", FileUtil.getFileSize(info.getContentLength())));
                mUploadBN.setEnabled(true);
                return;
            }
            mUploadBN.setText("上传:" + info.getPercentString());
        }
    }


}
