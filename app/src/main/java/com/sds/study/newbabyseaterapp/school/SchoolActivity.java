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
import com.sds.study.newbabyseaterapp.R;
import com.sds.study.newbabyseaterapp.calendar.CalendarActivity;

import static com.sds.study.newbabyseaterapp.calendar.CalendarActivity.db;

/**
 * Created by CANET on 2017-01-09.
 */

public class SchoolActivity extends AppCompatActivity implements OnMapReadyCallback{

    SupportMapFragment map;
    GoogleMap googleMap;
    LatLng myPoint;
    GpsInfo gps;
    SchoolDAO schoolDAO;

    SchoolActivity schoolActivity;

    String TAG;
    double lat;
    double lng;

    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        TAG = this.getClass().getName()+"/Canet";
        schoolActivity = this;
        setContentView(R.layout.activity_school);
        schoolDAO = new SchoolDAO(this, db);

        initGps();

    }

    @Override
    public void onMapReady(GoogleMap googleMap){

        Log.d(TAG, "onMapReady 호출됨");

        this.googleMap = googleMap;

        googleMap.addMarker(new MarkerOptions().title("내 마커").position(myPoint));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myPoint, 15f));

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

    @Override
    public void onBackPressed(){

        Intent intent = new Intent(this, CalendarActivity.class);
        startActivity(intent);
        finish();

    }

    public void btnSchoolClick(View view){

        switch(view.getId()){

            case R.id.btn_calendar :

                Intent intent = new Intent(this, CalendarActivity.class);
                startActivity(intent);
                finish();

                break;

            case R.id.btn_update_db :

                if(schoolDAO.selectOne()==0){

                    CreateSchoolDBAsyncTask dbAsyncTask = new CreateSchoolDBAsyncTask(this, db);
                    dbAsyncTask.execute();

                }else{

                    updateConfirmMsg("안내", "데이터베이스 업데이트를 실행하시겠습니까?\n(5분 정도 소요됩니다.)");

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
