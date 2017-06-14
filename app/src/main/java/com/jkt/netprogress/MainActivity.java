package com.jkt.netprogress;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jkt.tnetprogress.DownloadInterceptor;
import com.jkt.tnetprogress.OnDownloadListener;
import com.jkt.tnetprogress.OnUploadListener;
import com.jkt.tnetprogress.ProgressInfo;
import com.jkt.tnetprogress.UploadInterceptor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements OnDownloadListener, View.OnClickListener, OnUploadListener {

    private OkHttpClient mDownClient;
    private Request mDownRequest;
    private Button mDownloadBN;
    private ImageView mIV;
    private Request mUploadRequest;
    private TextView mUploadTV;
    private OkHttpClient mUploadClient;
    private TextView mDownloadTV;
    private Button mUploadBN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        initObject();
        initListeners();
    }


    private void initViews() {
        setContentView(R.layout.activity_main);
        mDownloadBN = (Button) findViewById(R.id.main_download_bn);
        mDownloadTV = (TextView) findViewById(R.id.main_download_tv);
        mIV = (ImageView) findViewById(R.id.main_iv);
        mUploadBN = (Button) findViewById(R.id.main_upload_bn);
        mUploadTV = (TextView) findViewById(R.id.main_upload_tv);
    }

    private void initObject() {
        String downloadUrl = "https://github.com/HoldMyOwn/TDialog/blob/master/preview/all.gif?raw=true";
        String uploadUrl = "http://v.polyv.net/uc/services/rest";
        mDownRequest = new Request.Builder()
                .url(downloadUrl)
                .build();
        mDownClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(new DownloadInterceptor(this))
                .build();
        File file = getFromAssets("a.jpg");
        mUploadRequest = new Request.Builder()
                .url(uploadUrl)
                .post(RequestBody.create(MediaType.parse("multipart/form-data"), file))
                .build();
        mUploadClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(new UploadInterceptor(this))
                .build();
    }

    private void initListeners() {
        mDownloadBN.setOnClickListener(this);
        mUploadBN.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_download_bn:
                downland();
                break;
            case R.id.main_upload_bn:
                upload();
                break;
        }
    }

    private void upload() {
         new Thread(new Runnable() {
             @Override
             public void run() {
                 runOnUiThread(new Runnable() {
                     @Override
                     public void run() {
                         mUploadBN.setEnabled(false);
                         mUploadBN.setText("上传中...");
                     }
                 });
                 try {
                     Response response = mUploadClient.newCall(mUploadRequest).execute();
                     Log.i("write",response.isSuccessful()+"   "+response.toString()+"  "+response.request().body().getClass());
                 } catch (IOException e) {
                     e.printStackTrace();
                 }
                 runOnUiThread(new Runnable() {
                     @Override
                     public void run() {
                         mUploadBN.setEnabled(true);
                         mUploadBN.setText("上传完成");
                     }
                 });
             }
         }).start();
    }


    public void downland() {

        new Thread(new Runnable() {

            private Bitmap mBitmap;

            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mDownloadBN.setEnabled(false);
                        mDownloadBN.setText("下载中...");
                        mIV.setImageBitmap(null);
                    }
                });
                try {
                    Response response = mDownClient.newCall(mDownRequest).execute();
                    byte[] bytes = response.body().bytes();
                    mBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mIV.setImageBitmap(mBitmap);
                        mDownloadBN.setText("下载完成");
                        mDownloadBN.setEnabled(true);
                    }
                });
            }
        }).start();
    }


    public File getFromAssets(String fileName) {
        File file = new File(getCacheDir(), "a.gif");
        try {
            InputStream inputStream = getResources().getAssets().open(fileName);
            FileOutputStream outputStream = new FileOutputStream(file);
            byte[] bytes = new byte[8 * 1024];
            int length = 0;
            while ((length = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, length);
                outputStream.flush();
            }
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    @Override
    public void onDownLoadProgress(ProgressInfo info) {
        Log.i("progressInfo", "down  " + info.getCurrentLength() + "----" + info.getContentLength() + "----" + info.getPercent());
    }

    @Override
    public void onUpLoadProgress(ProgressInfo info) {
        Log.i("progressInfo", "up  "+ info.getCurrentLength() + "----" + info.getContentLength() + "----" + info.getPercent());

    }
}
