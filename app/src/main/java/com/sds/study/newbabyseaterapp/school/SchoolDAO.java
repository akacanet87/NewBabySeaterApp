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
        TAG = this.getClass().getName()+"/Canet";

    }

    public SchoolDAO(SQLiteDatabase db){

        this.db = db;
        TAG = this.getClass().getName()+"/Canet";

    }

    public int selectOne(){

        int count=0;

        String sql="select date_id from diary where date_id=1";

        Cursor rs = db.rawQuery(sql, null);

        if (rs != null) {
            try {
                count = rs.getCount();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                rs.close();
            }
        }

        return count;

    }

}
