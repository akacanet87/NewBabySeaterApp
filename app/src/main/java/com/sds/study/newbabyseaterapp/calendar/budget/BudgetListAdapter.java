package com.sds.study.newbabyseaterapp.calendar.budget;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.sds.study.newbabyseaterapp.calendar.diary.Diary;
import com.sds.study.newbabyseaterapp.calendar.diary.DiaryItem;

import java.util.ArrayList;

/**
 * Created by CANET on 2017-01-03.
 */

public class BudgetListAdapter extends BaseAdapter{

    Context context;
    ArrayList<Diary> daily_diary_list=new ArrayList<>();
    SQLiteDatabase db;

    String TAG;

    public BudgetListAdapter(Context context, SQLiteDatabase db ) {

        this.context = context;
        this.db = db;
        TAG = this.getClass().getName()+"/Canet";

    }

    public void getDailyBudgetList( int date_id ){

        String sql = "select * from budget where date_id="+date_id;

        Cursor rs = db.rawQuery(sql, null);

        daily_diary_list.removeAll(daily_diary_list);

        while (rs.moveToNext()) {

            int diary_id = rs.getInt(rs.getColumnIndex("diary_id"));
            String title = rs.getString(rs.getColumnIndex("title"));
            String content = rs.getString(rs.getColumnIndex("content"));

            Diary dto = new Diary();

            dto.setDiary_id(diary_id);
            dto.setDate_id(date_id);
            dto.setTitle(title);
            dto.setContent(content);

            daily_diary_list.add(dto);

        }

        this.notifyDataSetChanged();
        this.notifyDataSetInvalidated();

        Log.d(TAG, "getDailyDiaryList notifychange 완료");

    }

    @Override
    public int getCount(){

        return daily_diary_list.size();
    }

    @Override
    public Object getItem(int i){

        return daily_diary_list.get(i);
    }

    @Override
    public long getItemId(int i){

        return daily_diary_list.get(i).getDiary_id();
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup){

        View view = null;     //누가 여기에 들어올지 모른다..
        Diary diary = daily_diary_list.get(i);

        //해당 index에 아이템이 이미 채워져 있다면..
        if (convertView != null) {
            view = convertView;
            DiaryItem item = (DiaryItem) view;
            item.setDiary(diary);
        } else {
            view = new DiaryItem(context, diary);
            //해당 index에 아무것도 없는 상태라면
        }

        return view;
    }
}
