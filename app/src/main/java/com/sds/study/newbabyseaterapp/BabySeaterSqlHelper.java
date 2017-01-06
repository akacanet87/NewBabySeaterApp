package com.sds.study.newbabyseaterapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by CANET on 2017-01-03.
 */

public class BabySeaterSqlHelper extends SQLiteOpenHelper{

    String TAG;

    //String name: 생성할 db파일명
    //int version: 최초의 버전 넘버
    public BabySeaterSqlHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        TAG=this.getClass().getName()+"/Canet";
    }

    //데이터베이스가 최초에 생성될 때 호출됨..
    //즉! 파일이 존재하지 않을 때 이 메서드가 호출됨.
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        StringBuffer diary_sql=new StringBuffer();

        diary_sql.append("create table diary(");
        diary_sql.append("diary_id integer primary key autoincrement");
        diary_sql.append(",date_id integer");
        diary_sql.append(",title varchar(30)");
        diary_sql.append(",content text");
        diary_sql.append(");");

        sqLiteDatabase.execSQL(diary_sql.toString());

        StringBuffer schedule_sql=new StringBuffer();
        schedule_sql.append("create table schedule(");
        schedule_sql.append("schedule_id integer primary key autoincrement");
        schedule_sql.append(",date_id integer");
        schedule_sql.append(",hour integer");
        schedule_sql.append(",minute integer");
        schedule_sql.append(",content text");
        schedule_sql.append(");");

        sqLiteDatabase.execSQL(schedule_sql.toString());

        /*StringBuffer budget_sql=new StringBuffer();

        budget_sql.append("create table budget(");
        budget_sql.append("budget_id integer primary key autoincrement");
        budget_sql.append(",date_id integer");
        budget_sql.append(",cardcompany varchar(200)");
        budget_sql.append(",regdate varchar(200)");
        budget_sql.append(",category varchar(200)");
        budget_sql.append(",budget varchar(100)");
        budget_sql.append(",content varchar(100)");
        budget_sql.append(")");

        sqLiteDatabase.execSQL(budget_sql.toString());

        StringBuffer school_sql=new StringBuffer();

        school_sql.append("create table school(");
        school_sql.append("school_id integer primary key autoincrement");
        school_sql.append(",addr varchar(300)");
        school_sql.append(",school_name varchar(50)");
        school_sql.append(",lat varchar(50)");
        school_sql.append(",lng varchar(50)");
        school_sql.append(",schoolbus varchar(4)");
        school_sql.append(",max_stu_num integer");
        school_sql.append(",teacher_num integer");
        school_sql.append(",call_num varchar(20)");
        school_sql.append(",cctv_num integer");
        school_sql.append(")");

        sqLiteDatabase.execSQL(school_sql.toString());*/

        Log.d(TAG,"데이터 베이스 구축");
    }

    //해당 파일이 이미 존재하나, 버전 숫자가 다른경우우
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists diary");
        sqLiteDatabase.execSQL("drop table if exists schedule");
        /*sqLiteDatabase.execSQL("drop table if exists budget");
        sqLiteDatabase.execSQL("drop table if exists school");*/
        onCreate(sqLiteDatabase);
        Log.d(TAG,"upgrade");
    }
}
