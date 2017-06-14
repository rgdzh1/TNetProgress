package com.jkt.netprogress;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.jkt.tnetprogress.DownloadInterceptor;
import com.jkt.tnetprogress.OnDownloadListener;
import com.jkt.tnetprogress.ProgressInfo;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements OnDownloadListener, Callback {

    private OkHttpClient mClient;
    private Request mRequest;
    private Button mButton;
    private ImageView mIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mButton = (Button) findViewById(R.id.main_bn);
        mIV = (ImageView) findViewById(R.id.main_iv);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnDownland(v);
            }
        });
        initObject();
    }

    private void initObject() {
        String url = "https://github.com/HoldMyOwn/TDialog/blob/master/preview/all.gif?raw=true";
//        String url = "http://android.xzstatic.com/2017/03/weixin6.5.6.apk";
        mRequest = new Request.Builder()
                .url(url)
                .build();
        mClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(new DownloadInterceptor(this))
                .build();

    }

    public void btnDownland(View view) {

        new Thread(new Runnable() {

            private Bitmap mBitmap;

            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mButton.setEnabled(false);
                    }
                });
                try {
                    Response response = mClient.newCall(mRequest).execute();
                    byte[] bytes = response.body().bytes();
                    mBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mIV.setImageBitmap(mBitmap);
                        mButton.setEnabled(true);
                    }
                });
            }
        }).start();
    }

    @Override
    public void onDownLoadProgress(ProgressInfo info) {
        Log.i("progressInfo",Thread.currentThread().getName()+ info.getCurrentLength() + "----" + info.getContentLength() + "----" + info.getPercent());
    }

    @Override
    public void onFailure(Call call, IOException e) {
        Log.i("progressInfo", "失败   " + e.toString());

    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        Log.i("progressInfo", "完毕");

    }
}
