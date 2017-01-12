package com.sds.study.newbabyseaterapp.calendar;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by CANET on 2017-01-03.
 */

public class CalendarDAO{

    SQLiteDatabase db;
    Context context;

    String TAG;

    public CalendarDAO(Context context, SQLiteDatabase db){

        this.db = db;
        this.context = context;
        TAG = this.getClass().getName()+"/Canet";

    }

    public CalendarDAO(SQLiteDatabase db){

        this.db = db;
        TAG = this.getClass().getName()+"/Canet";

    }

    public void insertDiary( String title, String content, int date_id ){

        String sql="insert into diary(title, content, date_id)";
        sql+=" values(?,?,?)";

        db.execSQL(sql, new String[]{

                title, content, Integer.toString(date_id)

        });

        Log.d(TAG, "다이어리 등록 완료");

    }

    public void insertSchedule( int date_id, int hour, int minute, String content ){

        String sql = "insert into schedule(date_id, hour, minute, content)";
        sql += " values(?,?,?,?)";

        db.execSQL(sql, new String[]{

                Integer.toString(date_id), Integer.toString(hour), Integer.toString(minute), content

        });

        Log.d(TAG, "스케줄 등록 완료");

    }

    public int countDailyList(int date_id){

        int diaryCount=0;

        String sql="select date_id from diary where date_id="+date_id;

        Cursor rs = db.rawQuery(sql, null);

        if (rs != null) {
            try {
                diaryCount = rs.getCount();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                rs.close();
            }
        }

        //Log.d(TAG, "다이어리 카운트 완료");

        return diaryCount;

    }

    public int countScheduleList( int date_id ){

        int scheduleCount = 0;

        String sql = "select date_id from schedule where date_id="+date_id;

        Cursor rs = db.rawQuery(sql, null);

        if (rs != null) {
            try {
                scheduleCount = rs.getCount();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                rs.close();
            }
        }

        //Log.d(TAG, "스케줄 카운트 완료");

        return scheduleCount;

    }

    public void updateDiary( String title, String content, int diary_id ){

        String sql = "update diary set title=?, content=? where diary_id=?";

        db.execSQL(sql, new String[]{

                title, content, Integer.toString(diary_id)

        });

        Log.d(TAG, "다이어리 수정 완료");

    }

    public void updateSchedule( int hour, int minute, String content, int schedule_id){

        String sql = "update schedule set hour=?, minute=?, content=? where schedule_id=?";

        db.execSQL(sql, new String[]{

                Integer.toString(hour), Integer.toString(minute), content, Integer.toString(schedule_id)

        });

        Log.d(TAG, "스케줄 수정 완료");

    }

    public void deleteDiary( int diary_id ){

        String sql = "delete from diary where diary_id=?";

        db.execSQL(sql, new String[]{

                Integer.toString(diary_id)

        });

        Log.d(TAG, "다이어리 삭제 완료");

    }

    public void deleteSchedule( int schedule_id ){

        String sql = "delete from schedule where schedule_id=?";

        db.execSQL(sql, new String[]{

                Integer.toString(schedule_id)

        });

        Log.d(TAG, "스케줄 삭제 완료");

    }

}
