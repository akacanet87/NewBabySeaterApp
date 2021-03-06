package com.sds.study.newbabyseaterapp.school;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sds.study.newbabyseaterapp.LoadingCalendarActivity;
import com.sds.study.newbabyseaterapp.R;
import com.sds.study.newbabyseaterapp.calendar.CalendarActivity;

import java.util.ArrayList;

import static com.sds.study.newbabyseaterapp.calendar.CalendarActivity.db;

/**
 * Created by CANET on 2017-01-09.
 */

public class SchoolActivity extends AppCompatActivity implements OnMapReadyCallback{

    SupportMapFragment map;
    GoogleMap googleMap;
    LatLng myPoint;
    GpsInfo gps;
    ArrayList<School> schools;

    SchoolActivity schoolActivity;
    SchoolDAO schoolDAO;

    String TAG;
    double lat;
    double lng;

    boolean isSchoolsNotNull=false;

    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        TAG = this.getClass().getName()+"/Canet";
        schoolActivity = this;
        setContentView(R.layout.activity_school);
        schoolDAO = new SchoolDAO(this, db);

        initSchoolList();
        initGps();

    }

    @Override
    public void onMapReady(GoogleMap googleMap){

        this.googleMap = googleMap;

        googleMap.addMarker(new MarkerOptions().title("나의 위치").position(myPoint));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myPoint, 15f));


        initSchoolList();

        if(schools!=null){

            MapMarkerAsyncTask mapMarkerAsyncTask = new MapMarkerAsyncTask(this, schools, googleMap);
            mapMarkerAsyncTask.execute();

        }

        Log.d(TAG, "onMapReady 호출 완료");

    }

    public void initGps(){

        gps = new GpsInfo(this);
        // GPS 사용유무 가져오기
        if (gps.isGetLocation()) {

            lat = gps.getLatitude();
            lng = gps.getLongitude();

            Toast.makeText(
                    getApplicationContext(),
                    "당신의 위치 - \n위도: " + lat + "\n경도: " + lng,
                    Toast.LENGTH_SHORT).show();
        } else {
            // GPS 를 사용할수 없으므로
            gps.showSettingsAlert();
        }

        myPoint = new LatLng(lat, lng);

        map = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        map.getMapAsync(this);

        Log.d(TAG, "initGps 완료");

    }

    public void initSchoolList(){

        int count = schoolDAO.selectOne();

        Log.d(TAG, "selectOne의 크기는 : " + count);
        Log.d(TAG, "isSchoolsNotNull : " + isSchoolsNotNull);

        if(count>0&&!isSchoolsNotNull){

            schools = schoolDAO.selectAllSchools();
            isSchoolsNotNull=!isSchoolsNotNull;

        }

        Log.d(TAG, "initSchoolList 완료");

    }

    @Override
    public void onBackPressed(){

        Intent intent = new Intent(this, LoadingCalendarActivity.class);
        startActivity(intent);
        finish();

    }

    public void btnSchoolClick(View view){

        switch(view.getId()){

            case R.id.btn_calendar :

                Intent intent = new Intent(this, LoadingCalendarActivity.class);
                startActivity(intent);
                finish();

                break;

            case R.id.btn_update_db :

                if(schoolDAO.selectOne()==0){

                    CreateSchoolDBAsyncTask dbAsyncTask = new CreateSchoolDBAsyncTask(this, db);
                    dbAsyncTask.execute();

                }else{

                    updateConfirmMsg("안내", "데이터베이스 업데이트를 실행하시겠습니까?\n업데이트는 6개월마다 해주시는 것이 좋습니다.\n(1~5분 정도 소요됩니다.)");

                }


                break;

            case R.id.btn_refresh_gps :

                initGps();

                break;

        }

    }

    public void showAlertMsg(String title, String msg){

        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle(title).setMessage(msg).show();

    }

    public void updateConfirmMsg(String title, String msg){

        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle(title).setMessage(msg).setCancelable(false).setPositiveButton("확인",
                new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        // 'YES'
                        CreateSchoolDBAsyncTask dbAsyncTask = new CreateSchoolDBAsyncTask(schoolActivity, db);
                        dbAsyncTask.execute();

                    }
                }).setNegativeButton("취소",
                new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        // 'No'
                        return;
                    }
                }).show();

    }

}
