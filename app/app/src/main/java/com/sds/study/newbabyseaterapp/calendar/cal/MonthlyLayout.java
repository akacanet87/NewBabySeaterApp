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
import android.view.View;
import android.widget.LinearLayout;

import com.sds.study.newbabyseaterapp.calendar.CalendarDAO;

import java.util.ArrayList;
import java.util.Calendar;

import static com.sds.study.newbabyseaterapp.calendar.CalendarActivity.db;

/**
 * View to display a month
 */
public class MonthlyLayout extends LinearLayout implements View.OnClickListener{

    public interface OnClickDayListener{

        void onClick(DailyLayout dailyLayout);
    }

    CalendarDAO calendarDAO;

    int this_year;
    int this_month;

    String TAG;

    ArrayList<LinearLayout> weeks = null;
    public ArrayList<DailyLayout> dailyLayouts = null;
    //ArrayList<Integer> isInThisMonths = null;
    OnClickDayListener onClickDayListener;
    private final OnClickDayListener dummyClickDayListener = new OnClickDayListener(){

        @Override
        public void onClick(DailyLayout dailyLayout){

        }
    };

    public void setOnClickDayListener(OnClickDayListener onClickDayListener){

        if(onClickDayListener != null){
            this.onClickDayListener = onClickDayListener;
        }else{
            this.onClickDayListener = dummyClickDayListener;
        }
    }


    public MonthlyLayout(Context context){
        //this(context, null);
        super(context);

        TAG = this.getClass().getName() + "/Canet";

        setOrientation(LinearLayout.VERTICAL);
        onClickDayListener = dummyClickDayListener;

        //Prepare many day-views enough to prevent recreation.
        if(weeks == null){

            weeks = new ArrayList<>(6); //Max 6 weeks in a month
            dailyLayouts = new ArrayList<>(42); // 7 days * 6 weeks = 42 days

            LinearLayout layout_oneday = null;

            for(int i = 0; i < 42; i++){

                if(i % 7 == 0){
                    //Create new week layout
                    layout_oneday = new LinearLayout(context);
                    LayoutParams params
                            = new LayoutParams(LayoutParams.MATCH_PARENT, 0);
                    params.weight = 1;
                    layout_oneday.setOrientation(LinearLayout.HORIZONTAL);
                    layout_oneday.setLayoutParams(params);
                    layout_oneday.setWeightSum(7);

                    weeks.add(layout_oneday);
                }

                LayoutParams params
                        = new LayoutParams(0, LayoutParams.MATCH_PARENT);
                params.weight = 1;

                DailyLayout dailyLayout = new DailyLayout(context);
                dailyLayout.setLayoutParams(params);
                dailyLayout.setOnClickListener(this);

                layout_oneday.addView(dailyLayout);
                dailyLayouts.add(dailyLayout);
            }
        }

        //for Preview of Graphic editor
        if(isInEditMode()){
            Calendar calendar = Calendar.getInstance();
            make(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH));
        }

    }

    public int getYear(){

        return this_year;
    }

    public int getMonth(){

        return this_month;
    }

    @Override
    public boolean shouldDelayChildPressedState(){

        return false;
    }

    public void make(int year, int month){

        calendarDAO = new CalendarDAO(db);

        if(this_year == year && this_month == month){
            return;
        }

        this.this_year = year;
        this.this_month = month;

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, 1);
        calendar.setFirstDayOfWeek(Calendar.SUNDAY);//Sunday is first day of week in this sample

        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);//Get day of the week in first day of this month
        int maxOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);//Get max day number of this month
        ArrayList<DailyData> dailyDataList = new ArrayList<>();
        ArrayList<Integer> isInThisMonths = new ArrayList<>();

        calendar.add(Calendar.DAY_OF_MONTH, Calendar.SUNDAY - dayOfWeek);//Move to first day of first week
        //HLog.d(TAG, CLASS, "first day : " + cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.KOREA) + " / " + cal.get(Calendar.DAY_OF_MONTH));

        /* add previous month */
        int seekDay;
        for(; ; ){
            seekDay = calendar.get(Calendar.DAY_OF_WEEK);
            if(dayOfWeek == seekDay) break;

            DailyData dailyData = new DailyData();
            dailyData.setDay(calendar);
            dailyDataList.add(dailyData);
            isInThisMonths.add(0);
            //하루 증가
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        //HLog.d(TAG, CLASS, "this month : " + cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.KOREA) + " / " + cal.get(Calendar.DAY_OF_MONTH));
        /* add this month */
        for(int i = 0; i < maxOfMonth; i++){
            DailyData dailyData = new DailyData();
            dailyData.setDay(calendar);
            dailyDataList.add(dailyData);
            isInThisMonths.add(1);
            //add one day
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        
        /* add next month */
        for(; ; ){
            if(calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY){
                DailyData dailyData = new DailyData();
                dailyData.setDay(calendar);
                dailyDataList.add(dailyData);
                isInThisMonths.add(0);
            }else{
                break;
            }
            //add one day
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        if(dailyDataList.size() == 0) return;

        //Remove all day-views
        this.removeAllViews();

        int count = 0;
        for(DailyData dailyData : dailyDataList){

            int today_date = dailyData.calendar.get(Calendar.DAY_OF_MONTH);
            int today_month = dailyData.calendar.get(Calendar.MONTH) + 1;
            int today_year = dailyData.calendar.get(Calendar.YEAR);

            int today_date_id = today_year * 10000 + today_month * 100 + today_date;

            if(count % 7 == 0){
                addView(weeks.get(count / 7));
            }
            DailyLayout dailyLayout = dailyLayouts.get(count);

            if(isInThisMonths.get(count)==0){

                dailyLayout.setClickable(false);
                dailyLayout.setActivated(false);
                dailyLayout.setFocusable(false);
                dailyLayout.clearFocus();
                dailyLayout.setEnabled(false);
                dailyLayout.setFocusableInTouchMode(false);
                dailyLayout.setLongClickable(false);

            }

            dailyLayout.setDay(dailyData);
            dailyLayout.refresh(this_month);
            count++;
        }

        //Set the weight-sum of LinearLayout to week counts
        this.setWeightSum(getChildCount());

    }


    @Override
    public void onClick(View view){

        DailyLayout dailyLayout = (DailyLayout) view;

        this.onClickDayListener.onClick(dailyLayout);

    }

}