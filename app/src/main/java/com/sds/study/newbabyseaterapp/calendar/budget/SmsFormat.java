package com.sds.study.newbabyseaterapp.calendar.budget;

import android.util.Log;

/**
 * Created by CANET on 2017-01-06.
 */

public class SmsFormat{

    String getSms;
    String TAG;

    public SmsFormat(String getSms){

        this.getSms = getSms;
        TAG = this.getClass().getName()+"/Canet";

    }

    public String getCard(String sms, int line ){

        String[] msgarr = sms.split("\n");

        for(int i=0 ; i<msgarr.length ; i++){

            Log.d(TAG, i+"번째 라인 : "+msgarr[i]);

        }

        return msgarr[line];

    }

    public String getDate(String sms, int line ){

        String[] msgarr = sms.split("\n");

        for(int i=0 ; i<msgarr.length ; i++){

            Log.d(TAG, i+"번째 라인 : "+msgarr[i]);

        }

        return msgarr[line];

    }

    public String getPay(String sms, int line ){

        String[] msgarr = sms.split("\n");

        for(int i=0 ; i<msgarr.length ; i++){

            Log.d(TAG, i+"번째 라인 : "+msgarr[i]);

        }

        return msgarr[line];

    }

    public String getPlace(String sms, int line ){

        String[] msgarr = sms.split("\n");

        for(int i=0 ; i<msgarr.length ; i++){

            Log.d(TAG, i+"번째 라인 : "+msgarr[i]);

        }

        return msgarr[line];

    }

}
