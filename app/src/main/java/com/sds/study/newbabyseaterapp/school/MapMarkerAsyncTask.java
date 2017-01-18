package com.sds.study.newbabyseaterapp.school;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sds.study.newbabyseaterapp.R;

import java.util.ArrayList;

/**
 * Created by CANET on 2017-01-13.
 */

public class MapMarkerAsyncTask extends AsyncTask<Void, Void, Void>{

    Context context;
    ArrayList<School> schools;
    GoogleMap googleMap;

    String TAG;

    public MapMarkerAsyncTask(Context context, ArrayList<School> schools, GoogleMap googleMap){

        this.context = context;
        this.schools = schools;
        this.googleMap = googleMap;
        TAG = this.getClass().getName()+"/Canet";

    }

    @Override
    protected void onPreExecute(){

        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... voids){

        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values){

        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Void aVoid){

        for( int i=0 ; i<schools.size() ; i++ ){

            School school = schools.get(i);

            /*StringBuffer schoolSummary = new StringBuffer();

            schoolSummary.append(school.getSchool_name() + "\n");
            schoolSummary.append("전화 : " + school.getSchool_tel() + "\n");
            schoolSummary.append("선생님 : " + school.getTeacher_num() + "명\n");
            schoolSummary.append("원생정원 : " + school.getMax_stu_num() + "명\n");
            schoolSummary.append("CCTV : " + school.getCctv_num() + "개\n");
            schoolSummary.append("통학버스 : " + school.getHas_schoolbus() + "\n");
            schoolSummary.append("주소 : " + school.getAddress());*/

            LatLng schoolPos = new LatLng(school.getLat(),school.getLon());

            googleMap.addMarker(new MarkerOptions().title(school.getSchool_name()).snippet("전화 : " + school.getSchool_tel() + "\n선생님 : \" + school.getTeacher_num() + \"명\n원생정원 : \" + school.getMax_stu_num() + \"명\nCCTV : \" + school.getCctv_num() + \"개\n통학버스 : \" + school.getHas_schoolbus() + \"\n주소 : \" + school.getAddress()").icon(BitmapDescriptorFactory.fromResource(R.drawable.school_marker)).position(schoolPos));

        }

        super.onPostExecute(aVoid);
    }
}
