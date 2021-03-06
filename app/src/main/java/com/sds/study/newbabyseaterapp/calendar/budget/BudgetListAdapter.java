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
    ArrayList<Budget> daily_budget_list=new ArrayList<>();
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

        daily_budget_list.removeAll(daily_budget_list);

        while (rs.moveToNext()) {

            int budget_id = rs.getInt(rs.getColumnIndex("budget_id"));
            String date = rs.getString(rs.getColumnIndex("date"));
            String hour = rs.getString(rs.getColumnIndex("hour"));
            String minute = rs.getString(rs.getColumnIndex("minute"));
            String place = rs.getString(rs.getColumnIndex("place"));
            int cost = rs.getInt(rs.getColumnIndex("cost"));
            String payment_method = rs.getString(rs.getColumnIndex("method"));
            String bank_name = rs.getString(rs.getColumnIndex("bank"));
            String content = rs.getString(rs.getColumnIndex("content"));

            Budget dto = new Budget();

            dto.setBudget_id(budget_id);
            dto.setDate_id(date_id);
            dto.setCost(cost);
            dto.setPayment_method(payment_method);
            dto.setBank_name(bank_name);
            dto.setPlace(place);
            dto.setContent(content);
            dto.setDate(date);
            dto.setHour(hour);
            dto.setMinute(minute);

            daily_budget_list.add(dto);

        }

        this.notifyDataSetChanged();
        this.notifyDataSetInvalidated();

        Log.d(TAG, "getDailyBudgetList notifychange 완료");

    }

    @Override
    public int getCount(){

        return daily_budget_list.size();
    }

    @Override
    public Object getItem(int i){

        return daily_budget_list.get(i);
    }

    @Override
    public long getItemId(int i){

        return daily_budget_list.get(i).getBudget_id();
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup){

        View view = null;     //누가 여기에 들어올지 모른다..
        Budget budget = daily_budget_list.get(i);

        //해당 index에 아이템이 이미 채워져 있다면..
        if (convertView != null) {
            view = convertView;
            BudgetItem item = (BudgetItem) view;
            item.setBudget(budget);
        } else {
            view = new BudgetItem(context, budget);
            //해당 index에 아무것도 없는 상태라면
        }

        return view;
    }
}
