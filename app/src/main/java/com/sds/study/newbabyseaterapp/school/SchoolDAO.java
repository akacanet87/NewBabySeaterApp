package com.sds.study.newbabyseaterapp.school;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by CANET on 2017-01-09.
 */

public class SchoolDAO{

    SQLiteDatabase db;
    Context context;
    ArrayList<School> schools;

    String TAG;

    public SchoolDAO(Context context, SQLiteDatabase db){

        this.db = db;
        this.context = context;
        TAG = this.getClass().getName() + "/Canet";

    }

    public SchoolDAO(SQLiteDatabase db){

        this.db = db;
        TAG = this.getClass().getName() + "/Canet";

    }

    public int selectOne(){

        int count = 0;

        String sql = "select school_id from school where school_id=1";

        Cursor rs = db.rawQuery(sql, null);

        if(rs != null){

            count = rs.getCount();

            rs.close();

        }

        return count;

    }

    public void insertSchool(SchoolInfo schoolInfo){

        String school_name = schoolInfo.school_name;
        String address = schoolInfo.address;
        String lat = schoolInfo.lat;
        String lon = schoolInfo.lon;
        String school_tel = schoolInfo.school_tel;
        String max_stu_num = schoolInfo.max_stu_num;
        String teacher_num = schoolInfo.teacher_num;
        String cctv_num = schoolInfo.cctv_num;
        String has_schoolbus = schoolInfo.has_schoolbus;

        String sido = schoolInfo.sido;
        String sigungu = schoolInfo.sigungu;

        if(address == null || address == ""){

            address = sido + " " + sigungu;

            //Log.d(TAG, "주소 없는 곳의 주소 : " + address);

        }else if(address.startsWith("\"")){

            //Log.d(TAG, "기존 address : " + address);

            address = address.substring(1, address.length() - 1);

            //Log.d(TAG, "바뀐 address : " + address);

        }

        if(lat == null || lat == ""){

            lat = "0";

        }

        if(lon == null || lon == ""){

            lon = "0";

        }

        if(cctv_num == "-" || cctv_num == "0" || cctv_num == "N" || cctv_num == null || cctv_num == ""){

            cctv_num = "0";

        }

        if(has_schoolbus == "_" || has_schoolbus == "0" || has_schoolbus == "N" || has_schoolbus == "미운영" || has_schoolbus == "부" || has_schoolbus == "" || has_schoolbus == null){

            has_schoolbus = "N";

        }else{

            has_schoolbus = "Y";

        }

        String sql = "insert into school(school_name,address,lat,lon,school_tel,max_stu_num,teacher_num,cctv_num,has_schoolbus)";
        sql += " values(?,?,?,?,?,?,?,?,?)";

        db.execSQL(sql, new String[]{

                school_name, address, lat, lon, school_tel, max_stu_num, teacher_num, cctv_num, has_schoolbus

        });

    }

    public ArrayList<School> selectAllSchools(){

        schools = new ArrayList<>();

        String sql = "select school_name,lat,lon,address,max_stu_num,teacher_num,school_tel,cctv_num,has_schoolbus from school";

        Cursor rs = db.rawQuery(sql, null);

        if(rs != null){

            for(rs.moveToFirst(); !rs.isAfterLast(); rs.moveToNext()){

                School school = new School();

                school.setSchool_name(rs.getString(rs.getColumnIndex("school_name")));
                school.setLat(rs.getDouble(rs.getColumnIndex("lat")));
                school.setLon(rs.getDouble(rs.getColumnIndex("lon")));
                school.setAddress(rs.getString(rs.getColumnIndex("address")));
                school.setMax_stu_num(rs.getInt(rs.getColumnIndex("max_stu_num")));
                school.setTeacher_num(rs.getInt(rs.getColumnIndex("teacher_num")));
                school.setSchool_tel(rs.getString(rs.getColumnIndex("school_tel")));
                school.setCctv_num(rs.getInt(rs.getColumnIndex("cctv_num")));
                school.setHas_schoolbus(rs.getString(rs.getColumnIndex("has_schoolbus")));

                schools.add(school);

            }

            rs.close();
        }

        return schools;

    }

}
