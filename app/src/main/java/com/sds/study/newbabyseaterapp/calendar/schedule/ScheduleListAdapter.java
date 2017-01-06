package com.sds.study.newbabyseaterapp.calendar.schedule;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * Created by CANET on 2017-01-03.
 */

public class ScheduleListAdapter extends BaseAdapter{

    Context context;
    ArrayList<Schedule> scheule_daily_list = new ArrayList<>();
    SQLiteDatabase db;

    String TAG;

    public ScheduleListAdapter(Context context, SQLiteDatabase db ) {

        this.context = context;
        this.db = db;
        TAG = this.getClass().getName()+"/Canet";

    }

    public void getDailyScheduleList( int date_id ){

        String sql = "select * from schedule where date_id="+date_id;

        Cursor rs = db.rawQuery(sql, null);

        scheule_daily_list.removeAll(scheule_daily_list);

        while (rs.moveToNext()) {

            int schedule_id = rs.getInt(rs.getColumnIndex("schedule_id"));
            int hour = rs.getInt(rs.getColumnIndex("hour"));
            int minute = rs.getInt(rs.getColumnIndex("minute"));
            String content = rs.getString(rs.getColumnIndex("content"));

            Schedule dto = new Schedule();
            dto.setSchedule_id(schedule_id);
            dto.setDate_id(date_id);
            dto.setHour(hour);
            dto.setMinute(minute);
            dto.setContent(content);

            scheule_daily_list.add(dto);

        }

        this.notifyDataSetChanged();

    }


    //총 아이템 갯수!! arr.length 만큼 확보!!
    public int getCount() {
        return scheule_daily_list.size();
    }

    public Object getItem(int i) {
        return scheule_daily_list.get(i);
    }

    //
    public long getItemId(int i) {
        return scheule_daily_list.get(i).getSchedule_id();
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        View view = null;//누가 여기에 들어올지 모른다..
        Schedule schedule = scheule_daily_list.get(i);

        //해당 index에 아이템이 이미 채워져 있다면..
        if (convertView != null) {
            view = convertView;
            ScheduleItem item = (ScheduleItem) view;
            item.setSchedule(schedule);
        } else {
            view = new ScheduleItem(context, schedule);
            //해당 index에 아무것도 없는 상태라면
        }

        return view;
    }
}
