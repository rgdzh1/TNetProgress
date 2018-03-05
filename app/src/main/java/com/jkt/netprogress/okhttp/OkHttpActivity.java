package com.jkt.netprogress.okhttp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpActivity extends AppCompatActivity implements OnDownloadListener, View.OnClickListener, OnUploadListener {

    private OkHttpClient mDownClient;
    private OkHttpClient mUploadClient;
    private Request mDownRequest;
    private Request mUploadRequest;
    private Button mDownloadBN;
    private Button mUploadBN;
    private ImageView mIV;
    private String mDownloadUrl;
    private String mUploadUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //无论下载上传家监听,都是配置OkHttp的拦截器,一个是new DownloadInterceptor(this)
        //另一个是  new UploadInterceptor(this)
        initViews();
        initObject();
        initListeners();
    }


    private void initViews() {
        setContentView(R.layout.activity_okhttp);
        mDownloadBN = (Button) findViewById(R.id.okHttp_download_bn);
        mIV = (ImageView) findViewById(R.id.okHttp_iv);
        mUploadBN = (Button) findViewById(R.id.okHttp_upload_bn);
    }

    private void initObject() {
        //63行url对应图片获取长度失败,可以做失败测试
        //失败原因服务器走了gzip压缩导致content-length为-1,会走长度获取失败回调
        //避免压缩方法,request添加头信息.key=Accept-Encoding value=identity
//        mDownloadUrl = "http://pic1.win4000.com/wallpaper/a/568cd27741af5.jpg";
        //2017-9-29日期,mDownloadUrl长度可以获取成功,如果失败则回调长度失败回调并且终止进度回调
        //如果mDownloadUrl在你测试情况下获取长度失败,你可以换取图片尝试获得相关进度信息.
        mDownloadUrl = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1506664930570&di=6dace2ed4c25580c7446a4ef0807b928&imgtype=0&src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F6%2F57eb31505b1fd.jpg";
        mUploadUrl = "http://v.polyv.net/uc/services/rest";
        mDownRequest = new Request.Builder()
                .url(mDownloadUrl)
                //下面语句要求不进行zip压缩
                .addHeader("Accept-Encoding","identity")
                .build();
        mDownClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(new DownloadInterceptor(this))
                .build();
        File file = FileUtil.getFromAssets(this, "a.jpg");
        mUploadRequest = new Request.Builder()
                .url(mUploadUrl)
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
            case R.id.okHttp_download_bn:
                downland();
                break;
            case R.id.okHttp_upload_bn:
                upload();
                break;
        }
    }

    private void upload() {
        //因为上传的路径、参数并不正确,  response.isSuccessful()为false,这里主要演示获取进度
        mUploadBN.setEnabled(false);
        mUploadBN.setText("上传中...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mUploadClient.newCall(mUploadRequest).execute();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mUploadBN.setEnabled(true);
                            mUploadBN.setText("上传");
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }


    public void downland() {
        mDownloadBN.setEnabled(false);
        mDownloadBN.setText("下载中...");
        mIV.setImageBitmap(null);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //注意必须对response的body获取字节或者文件,如果不做处理,那么默认是进行接口的连接.
                    //并没有进行字节的读取动作,内部的source的read方法也就不会执行,没有进度回调
                    Response response = mDownClient.newCall(mDownRequest).execute();
                    byte[] bytes = response.body().bytes();
                    final Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mDownloadBN.setEnabled(true);
                            mDownloadBN.setText("下载");
                            mIV.setImageBitmap(bitmap);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    @Override
    public void onDownLoadProgress(ProgressInfo info) {
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
