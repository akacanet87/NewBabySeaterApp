package com.sds.study.newbabyseaterapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.sds.study.newbabyseaterapp.calendar.CalendarActivity;

/**
 * Created by CANET on 2017-01-18.
 */

public class LoadingCalendarActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_load_main);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getBaseContext(), CalendarActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);

    }

}
