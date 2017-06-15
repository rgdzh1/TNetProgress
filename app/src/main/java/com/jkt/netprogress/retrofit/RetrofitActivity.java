package com.jkt.netprogress.retrofit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.jkt.netprogress.R;

public class RetrofitActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mDownloadBN;
    private ImageView mIV;
    private Button mUploadBN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        initObjects();
        initListeners();
    }



    private void initViews() {
        setContentView(R.layout.activity_retrofit);
        mDownloadBN = (Button) findViewById(R.id.okHttp_download_bn);
        mIV = (ImageView) findViewById(R.id.okHttp_iv);
        mUploadBN = (Button) findViewById(R.id.okHttp_upload_bn);
    }
    private void initObjects() {

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

    }

    private void upload() {

    }

}
