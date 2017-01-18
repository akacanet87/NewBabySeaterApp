package com.sds.study.newbabyseaterapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.sds.study.newbabyseaterapp.school.SchoolActivity;

/**
 * Created by CANET on 2017-01-18.
 */

public class LoadingSchoolActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_load_map);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getBaseContext(), SchoolActivity.class);
                startActivity(intent);
                finish();
            }
        }, 30000);

    }

}
