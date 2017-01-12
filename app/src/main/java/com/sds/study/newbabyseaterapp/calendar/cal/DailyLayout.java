/*
* Copyright (C) 2015 Hansoo Lab.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.sds.study.newbabyseaterapp.calendar.cal;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sds.study.newbabyseaterapp.R;
import com.sds.study.newbabyseaterapp.calendar.CalendarDAO;

import java.util.Calendar;

import static com.sds.study.newbabyseaterapp.calendar.CalendarActivity.TODAY_DATE;
import static com.sds.study.newbabyseaterapp.calendar.CalendarActivity.TODAY_MONTH;
import static com.sds.study.newbabyseaterapp.calendar.CalendarActivity.TODAY_YEAR;
import static com.sds.study.newbabyseaterapp.calendar.CalendarActivity.db;

/**
 * View to display a day
 * @author Brownsoo
 *
 */
public class DailyLayout extends LinearLayout{
    
    /** number text field */
    RelativeLayout main_layout;
    TextView txt_day, txt_diary, txt_schedule, txt_budget;
    ImageView img_icon_diary, img_icon_schedule, img_icon_budget;

    //TextView txt_day;

    /** Value object for a day info */
    DailyData dailyData;
    String TAG;

    int today_date, today_month, today_year, today_date_id;

    public DailyLayout(Context context ){

        super(context);
        init(context);
        TAG = this.getClass().getName()+"/Canet";

    }

    public DailyLayout(Context context, AttributeSet attrs){

        super(context, attrs);
        //TAG = this.getClass().getName()+"/Canet";
        init(context);

    }

    public void init(Context context){

        View view = View.inflate(context, R.layout.item_day, this);

        main_layout = (RelativeLayout) view.findViewById(R.id.daily_calendar_layout);
        txt_day = (TextView) view.findViewById(R.id.txt_day);
        txt_diary = (TextView) view.findViewById(R.id.txt_diary);
        txt_schedule = (TextView) view.findViewById(R.id.txt_schedule);
        txt_budget = (TextView) view.findViewById(R.id.txt_budget);
        img_icon_diary = (ImageView) view.findViewById(R.id.img_icon_diary);
        img_icon_schedule = (ImageView) view.findViewById(R.id.img_icon_schedule);
        img_icon_budget = (ImageView) view.findViewById(R.id.img_icon_budget);

        //dailyData = new DailyData();

    }

    public void setDay(DailyData one) {

        this.dailyData = one;

        today_date = dailyData.calendar.get(Calendar.DAY_OF_MONTH);
        today_month = dailyData.calendar.get(Calendar.MONTH) + 1;
        today_year = dailyData.calendar.get(Calendar.YEAR);

        today_date_id = today_year * 10000 + today_month * 100 + today_date;

        //Log.d(TAG, "생성 시 today_date_id는 "+today_date_id);

    }

    public int get(int field) throws IllegalArgumentException, ArrayIndexOutOfBoundsException {

        return dailyData.get(field);

    }

    public void refresh(int get_month) {

        CalendarDAO calendarDAO = new CalendarDAO(db);

        int diaryCount = calendarDAO.countDailyList(today_date_id);
        int scheduleCount = calendarDAO.countScheduleList(today_date_id);
        //int budgetCount = calendarDAO.countBudgetList(today_date_id);


        //Log.d(TAG, "refresh 후의 "+today_date_id+"의 diaryCount는 "+diaryCount);

        setDailyStyle(get_month);
        txt_day.setText(String.valueOf(dailyData.get(Calendar.DAY_OF_MONTH)));

        setDateColor(dailyData, txt_day);

        setImg(diaryCount, txt_diary, img_icon_diary, R.drawable.icon_diary);
        setImg(scheduleCount, txt_schedule, img_icon_schedule, R.drawable.icon_schedule);
        //setImg(budgetCount, txt_budget, img_icon_budget, R.drawable.icon_budget);

    }

    public void setDateColor(DailyData data, TextView tv ){

        if(data.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {

            tv.setTextColor(Color.RED);

        }else if(data.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {

            tv.setTextColor(Color.BLUE);

        }else {

            tv.setTextColor(Color.BLACK);

        }

    }

    public void setDailyStyle( int get_month ){
        
      /*  Log.d(TAG, "today_month는 "+today_month);
        Log.d(TAG, "get_month는 "+get_month);*/

        if(today_date == TODAY_DATE && today_month == TODAY_MONTH && today_year == TODAY_YEAR){

            main_layout.setBackgroundResource(R.drawable.day_cell_bg_today);
            txt_day.setTextAppearance(R.style.text_calendar_today);

        }else if( get_month+1==today_month ){

            main_layout.setBackgroundResource(R.drawable.day_cell_bg);
            txt_day.setTextAppearance(R.style.text_calendar_thismonth);

        }else{

            main_layout.setBackgroundResource(R.drawable.day_cell_bg_om);
            txt_day.setTextAppearance(R.style.text_calendar_othermonth);

        }

    }

    public void setImg( int count, TextView tv, ImageView imgv, int img_resource ){

        if(count != 0){

            tv.setText(Integer.toString(count));
            imgv.setImageResource(img_resource);

        }else{

            tv.setText("");
            imgv.setImageResource(0);

        }

    }
    
}