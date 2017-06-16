package com.jkt.netprogress.retrofit;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
                  .getDownload(new Subscriber<ResponseBody>() {
                      @Override
                      public void onStart() {
                          super.onStart();
                          mDownloadBN.setEnabled(false);
                          mIV.setImageBitmap(null);
                      }

                      @Override
                      public void onCompleted() {
                          Log.i("read","completed  ");

                      }

                      @Override
                      public void onError(Throwable e) {
                          Log.i("read","download  error"+e.toString());
                      }

                      @Override
                      public void onNext(ResponseBody body) {
                          try {
                              Log.i("read","next");
                              byte[]  bytes = body.bytes();
                              Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                              mIV.setImageBitmap(bitmap);
                          } catch (IOException e) {
                              e.printStackTrace();
                          }
                      }
                  },new DownloadInterceptor(this));
    }

    private void upload() {
        File file = FileUtil.getFromAssets(this,"a.jpg");
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
               Log.i("write","completed  ");

           }

           @Override
           public void onError(Throwable e) {
               Log.i("write","upload error   "+e.toString());

           }

           @Override
           public void onNext(okhttp3.Response s) {
               Log.i("write","upload next");

           }
       },body,new UploadInterceptor(this));
    }

    @Override
    public void onDownLoadProgress(ProgressInfo info) {
        //注意info的url不包含参数键值对,打印查看
        Log.i("read","progress"+info.getCurrentLength()+"   "+info.getContentLength());
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
