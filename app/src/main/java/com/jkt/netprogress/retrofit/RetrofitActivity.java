package com.jkt.netprogress.retrofit;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.jkt.netprogress.FileUtil;
import com.jkt.netprogress.R;
import com.jkt.tnetprogress.DownloadInterceptor;
import com.jkt.tnetprogress.OnDownloadListener;
import com.jkt.tnetprogress.OnUploadListener;
import com.jkt.tnetprogress.ProgressInfo;
import com.jkt.tnetprogress.UploadInterceptor;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
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
        //无论下载上传家监听,都是配置OkHttp的拦截器,一个是new DownloadInterceptor(this)
        //另一个是  new UploadInterceptor(this)
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
        //59行url对应图片获取长度失败,可以做失败测试
        //失败原因服务器走了gzip压缩导致content-length为-1,会走长度获取失败回调
        //避免压缩方法,request添加头信息.key=Accept-Encoding value=identity
        //mDownloadUrl = "http://pic1.win4000.com/wallpaper/a/568cd27741af5.jpg";
        //2017-9-29日期,mDownloadUrl长度可以获取成功,如果失败则回调长度失败回调并且终止进度回调
        //如果mDownloadUrl在你测试情况下获取长度失败,你可以换取图片尝试获得相关进度信息.
        mDownloadUrl = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1506664930570&di=6dace2ed4c25580c7446a4ef0807b928&imgtype=0&src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F6%2F57eb31505b1fd.jpg";
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
                .getDownload(new Subscriber<ResponseBody>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        mDownloadBN.setEnabled(false);
                        mIV.setImageBitmap(null);
                    }

                    @Override
                    public void onCompleted() {
                        Log.i("read", "completed  ");

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("read", "download  error" + e.toString());
                    }

                    @Override
                    public void onNext(ResponseBody body) {
                        try {
                            Log.i("read", "next");
                            byte[] bytes = body.bytes();
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            mIV.setImageBitmap(bitmap);
                            mDownloadBN.setEnabled(true);
                            mDownloadBN.setText("下载");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                },  new DownloadInterceptor(this));
    }

    private void upload() {
        File file = FileUtil.getFromAssets(this, "a.jpg");
        RequestBody body = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        //因为上传的路径、参数并不正确,所以会走失败,这里主要演示获取进度
        HttpMethods.getInstance().getUpload(new Subscriber<okhttp3.Response>() {
            @Override
            public void onStart() {
                super.onStart();
                mUploadBN.setEnabled(false);
            }

            @Override
            public void onCompleted() {
                Log.i("write", "completed  ");

            }

            @Override
            public void onError(Throwable e) {
                Log.i("write", "upload error   " + e.toString());
                //因为路径和参数等不正确 所以会走失败回调,这边是模拟操作
                mUploadBN.setEnabled(true);
                mUploadBN.setText("上传");
            }

            @Override
            public void onNext(okhttp3.Response s) {
                Log.i("write", "upload next");
                mUploadBN.setEnabled(true);
                mUploadBN.setText("上传");

            }
        }, body, new UploadInterceptor(this));
    }

    @Override
    public void onDownLoadProgress(ProgressInfo info) {
        Log.i("onDownLoadProgress", " " + info.getUrl());
        //注意info的url不包含参数键值对(所以contains比equal方法更合适),打印查看
        if (mDownloadUrl.contains(info.getUrl())) {
            if (info.getPercentFloat() == 1) {
                mDownloadBN.setText("下载完成   总尺寸" + String.format("Size : %s", FileUtil.getFileSize(info.getContentLength())));
                mDownloadBN.setEnabled(true);
                return;
            }
            mDownloadBN.setText("下载:" + info.getPercentString());
        }
    }

    @Override
    public void onDownLoadGetContentLengthFail(ProgressInfo info) {
        Toast.makeText(this, "获取进度失败", Toast.LENGTH_SHORT).show();
        //注意info的url不包含参数键值对(所以contains比equal方法更合适),打印查看
        if (mDownloadUrl.contains(info.getUrl())) {
            //toast在发版时候应该去掉
            Toast.makeText(this, "获取进度失败", Toast.LENGTH_SHORT).show();
            mDownloadBN.setText("下载中...");
        }
    }

    @Override
    public void onUpLoadProgress(ProgressInfo info) {
        //注意info的url不包含参数键值对(所以contains比equal方法更合适),打印查看
        if (mUploadUrl.contains(info.getUrl())) {
            if (info.getPercentFloat() == 1) {
                mUploadBN.setText("上传完成   总尺寸" + String.format("Size : %s", FileUtil.getFileSize(info.getContentLength())));
                mUploadBN.setEnabled(true);
                return;
            }
            mUploadBN.setText("上传:" + info.getPercentString());
        }
    }

    @Override
    public void onUploadGetContentLengthFail(ProgressInfo info) {
        //注意info的url不包含参数键值对(所以contains比equal方法更合适),打印查看
        if (mUploadUrl.contains(info.getUrl())) {
            //toast在发版时候应该去掉
            Toast.makeText(this, "获取上传进度失败", Toast.LENGTH_SHORT).show();
            mUploadBN.setText("上传中...");
        }
    }


}
