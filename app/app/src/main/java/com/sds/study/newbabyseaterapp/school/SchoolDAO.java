package com.sds.study.newbabyseaterapp.school;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by CANET on 2017-01-09.
 */

public class SchoolDAO{

    SQLiteDatabase db;
    Context context;

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
            try{
                count = rs.getCount();
            }catch(Exception e){
                e.printStackTrace();
            }finally{
                rs.close();
            }
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

        String sql = "insert into school(school_name,address,lat,lon,school_tel,max_stu_num,teacher_num,cctv_num,has_schoolbus)";
        sql += " values(?,?,?,?,?,?,?,?,?)";

        db.execSQL(sql, new String[]{

                school_name, address, lat, lon, school_tel, max_stu_num, teacher_num, cctv_num, has_schoolbus

        });

    }

}
