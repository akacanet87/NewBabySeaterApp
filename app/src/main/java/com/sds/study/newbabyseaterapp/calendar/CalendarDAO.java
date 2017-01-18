package com.sds.study.newbabyseaterapp.calendar;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

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
        TAG = this.getClass().getName() + "/Canet";

    }

    public CalendarDAO(SQLiteDatabase db){

        this.db = db;
        TAG = this.getClass().getName() + "/Canet";

    }

    public ArrayList<Baby> selectAllBabies(){

        ArrayList<Baby> babies = new ArrayList<>();

        String sql = "select * from baby";

        Cursor rs = db.rawQuery(sql, null);

        if(rs != null){

            for(rs.moveToFirst(); !rs.isAfterLast(); rs.moveToNext()){

                Baby baby = new Baby();

                baby.setBaby_id(rs.getInt(rs.getColumnIndex("baby_id")));
                baby.setName(rs.getString(rs.getColumnIndex("name")));
                baby.setGender(rs.getString(rs.getColumnIndex("gender")));
                baby.setYear(rs.getInt(rs.getColumnIndex("year")));
                baby.setMonth(rs.getInt(rs.getColumnIndex("month")));
                baby.setDate(rs.getInt(rs.getColumnIndex("date")));

                babies.add(baby);

            }
        }

        return babies;

    }

    public String selectImg( int id ){

        String sql = "select img_path from picture where picture_id=" + id;

        String img_path = null;

        Cursor rs = db.rawQuery(sql, null);
        rs.moveToFirst();

        if(rs != null){

            img_path = rs.getString(rs.getColumnIndex("img_path"));

            rs.close();

        }

        Log.d(TAG, "이미지 db에서 불러오기");

        return img_path;

    }

    public Baby selectBaby(int id){

        String sql = "select * from baby where baby_id=" + id;

        //Log.d(TAG, "id : " + id);
        //Log.d(TAG, "sql : " + sql);

        Baby baby = new Baby();
        Cursor rs = db.rawQuery(sql, null);
        rs.moveToFirst();

        //Log.d(TAG, "rs : " + rs);

        if(rs != null){

            //Log.d(TAG, "if문 들어옴");

            baby.setName(rs.getString(rs.getColumnIndex("name")));
            baby.setGender(rs.getString(rs.getColumnIndex("gender")));
            baby.setYear(rs.getInt(rs.getColumnIndex("year")));
            baby.setMonth(rs.getInt(rs.getColumnIndex("month")));
            baby.setDate(rs.getInt(rs.getColumnIndex("date")));

            //Log.d(TAG, "baby세팅 완료");

            rs.close();

        }

        //Log.d(TAG, "이름 : " + baby.getName() + "\n성별 : " + baby.getGender() + "\n생년월일 : " + baby.getYear() + "." + baby.getMonth() + "." + baby.getDate() + "\nload baby 완료");

        return baby;

    }

    public void insertImage( String img_path ){

        String sql = "insert into picture(img_path)";
        sql += " values(?)";

        Log.d(TAG, "img_path : " + img_path);

        db.execSQL(sql, new String[]{

                img_path

        });

        Log.d(TAG, "이미지 저장 완료");

    }

    public void insertBaby(String name, String gender, int year, int month, int date){

        String sql = "insert into baby(name, gender, year, month, date)";
        sql += " values(?,?,?,?,?)";

        db.execSQL(sql, new String[]{

                name, gender, Integer.toString(year), Integer.toString(month), Integer.toString(date)

        });

        Log.d(TAG, "이름 : " + name + "\n성별 : " + gender + "\n생년월일 : " + year + "." + month + "." + date + "\n아기 등록 완료");

    }

    public void insertDiary(String title, String content, int date_id){

        String sql = "insert into diary(title, content, date_id)";
        sql += " values(?,?,?)";

        db.execSQL(sql, new String[]{

                title, content, Integer.toString(date_id)

        });

        Log.d(TAG, "다이어리 등록 완료");

    }

    public void insertSchedule(int date_id, int hour, int minute, String content){

        String sql = "insert into schedule(date_id, hour, minute, content)";
        sql += " values(?,?,?,?)";

        db.execSQL(sql, new String[]{

                Integer.toString(date_id), Integer.toString(hour), Integer.toString(minute), content

        });

        Log.d(TAG, "스케줄 등록 완료");

    }

    public void insertBudget(int date_id, int year, int month, String date, String hour, String minute, String place, int cost, String method, String bank, String content){

        String sql = "insert into budget(date_id,year,month,date,hour,minute,place,cost,method,bank,content)";
        sql += " values(?,?,?,?,?,?,?,?,?,?,?)";

        db.execSQL(sql, new String[]{

                Integer.toString(date_id), Integer.toString(year), Integer.toString(month), date, hour, minute, place, Integer.toString(cost), method, bank, content

        });

        Log.d(TAG, "가계부 등록 완료");

    }

    public void insertTotalBudget(int year, int month, int budget){

        String sql = "insert into total_budget(year, month, budget)";
        sql += " values(?,?,?)";

        db.execSQL(sql, new String[]{

                Integer.toString(year), Integer.toString(month), Integer.toString(budget)

        });

        Log.d(TAG, "총 예산 등록 완료");

    }

    public int countImg(){

        int imgCount = 0;

        String sql = "select picture_id from picture";

        Cursor rs = db.rawQuery(sql, null);

        if(rs != null){

            imgCount = rs.getCount();

            rs.close();

        }

        return imgCount;

    }

    public int countBaby(){

        int babyCount = 0;

        String sql = "select baby_id from baby";

        Cursor rs = db.rawQuery(sql, null);

        if(rs != null){

            babyCount = rs.getCount();

            rs.close();

        }

        return babyCount;

    }

    public int countDailyList(int date_id){

        int diaryCount = 0;

        String sql = "select date_id from diary where date_id=" + date_id;

        Cursor rs = db.rawQuery(sql, null);

        if(rs != null){

            diaryCount = rs.getCount();

            rs.close();

        }

        //Log.d(TAG, "다이어리 카운트 완료");

        return diaryCount;

    }

    public int countScheduleList(int date_id){

        int scheduleCount = 0;

        String sql = "select date_id from schedule where date_id=" + date_id;

        Cursor rs = db.rawQuery(sql, null);

        if(rs != null){

            scheduleCount = rs.getCount();

            rs.close();

        }

        //Log.d(TAG, "스케줄 카운트 완료");

        return scheduleCount;

    }

    public int countBudgetList(int date_id){

        int budgetCount = 0;

        String sql = "select date_id from budget where date_id=" + date_id;

        Cursor rs = db.rawQuery(sql, null);

        if(rs != null){

            budgetCount = rs.getCount();

            rs.close();

        }

        //Log.d(TAG, "스케줄 카운트 완료");

        return budgetCount;

    }

    public int countTotalBudgetList(){

        int budgetCount = 0;

        String sql = "select total_budget_id from total_budget";

        Cursor rs = db.rawQuery(sql, null);

        if(rs != null){

            budgetCount = rs.getCount();

            rs.close();

        }

        //Log.d(TAG, "스케줄 카운트 완료");

        return budgetCount;

    }

    public int getLastTotalBudget(int month){

        int budget = 0;

        String sql = "select budget from total_budget where total_budget_id = (select max(total_budget_id) from total_budget where month=" + month+ ")";

        Cursor rs = db.rawQuery(sql, null);

        rs.moveToFirst();

        if(rs != null){

            budget = rs.getInt(rs.getColumnIndex("budget"));

            rs.close();

        }

        //Log.d(TAG, "스케줄 카운트 완료");

        return budget;

    }

    public void updateDiary(String title, String content, int diary_id){

        String sql = "update diary set title=?, content=? where diary_id=?";

        db.execSQL(sql, new String[]{

                title, content, Integer.toString(diary_id)

        });

        Log.d(TAG, "다이어리 수정 완료");

    }

    public void updateSchedule(int hour, int minute, String content, int schedule_id){

        String sql = "update schedule set hour=?, minute=?, content=? where schedule_id=?";

        db.execSQL(sql, new String[]{

                Integer.toString(hour), Integer.toString(minute), content, Integer.toString(schedule_id)

        });

        Log.d(TAG, "스케줄 수정 완료");

    }

    public void updateBudget(int date_id, int year, int month, String date, String hour, String minute, String place, int cost, String method, String bank, String content, int budget_id){

        String sql = "update budget set date_id=?, year=?, month=?, date=?, hour=?, minute=?, place=?, cost=?, method=?, bank=?, content=? where schedule_id=?";

        db.execSQL(sql, new String[]{

                Integer.toString(date_id), Integer.toString(year), Integer.toString(month), date, hour, minute, place, Integer.toString(cost), method, bank, content, Integer.toString(budget_id)

        });

        Log.d(TAG, "가계부 수정 완료");

    }

    public void deleteDiary(int diary_id){

        String sql = "delete from diary where diary_id=?";

        db.execSQL(sql, new String[]{

                Integer.toString(diary_id)

        });

        Log.d(TAG, "다이어리 삭제 완료");

    }

    public void deleteSchedule(int schedule_id){

        String sql = "delete from schedule where schedule_id=?";

        db.execSQL(sql, new String[]{

                Integer.toString(schedule_id)

        });

        Log.d(TAG, "스케줄 삭제 완료");

    }

    public void deleteBudget(int budget_id){

        String sql = "delete from budget where budget_id=?";

        db.execSQL(sql, new String[]{

                Integer.toString(budget_id)

        });

        Log.d(TAG, "가계부 삭제 완료");

    }

    public int getTotalSpent(int year, int month){

        int total=0;

        String sql = "select cost from budget where year="+year+" and month="+month;

        //Log.d(TAG, "id : " + id);
        //Log.d(TAG, "sql : " + sql);

        Cursor rs = db.rawQuery(sql, null);

        rs.moveToFirst();

        if(rs != null){

            for(rs.moveToFirst(); !rs.isAfterLast(); rs.moveToNext()){

                int cost = rs.getInt(rs.getColumnIndex("cost"));
                total += cost;

            }
        }

        //Log.d(TAG, "이름 : " + baby.getName() + "\n성별 : " + baby.getGender() + "\n생년월일 : " + baby.getYear() + "." + baby.getMonth() + "." + baby.getDate() + "\nload baby 완료");

        return total;

    }

}
