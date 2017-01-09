package com.sds.study.newbabyseaterapp;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

/**
 * Created by CANET on 2017-01-09.
 */

public class CheckPermission{

    Activity activity;

    String TAG;

    public CheckPermission(Activity activity){

        TAG = this.getClass().getName()+"/Canet";
        this.activity = activity;

    }

    public void checkPermissions( String manifestPermission, int permissionNum ){

        Log.d(TAG, "checkPermission 진입");
        Log.d(TAG, "permissionNum : "+permissionNum);

        int permission= ContextCompat.checkSelfPermission(activity, manifestPermission);
        if(permission == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(activity ,new String[]{
                    manifestPermission
            },permissionNum);
        }

    }

}
