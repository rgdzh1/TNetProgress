package com.jkt.netprogress;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by 天哥哥
 */
public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }


    public void btnOkHttp(View view) {
        startActivity(new Intent(this, OkHttpActivity.class));
    }

    public void btnRetrofit(View view) {
        startActivity(new Intent(this, RetrofitActivity.class));
    }
}
