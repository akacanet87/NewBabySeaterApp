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

package com.sds.study.newbabyseaterapp.calendar.calendar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sds.study.newbabyseaterapp.R;
import com.sds.study.newbabyseaterapp.calendar.CalendarDAO;

import java.util.ArrayList;
import java.util.Calendar;

import static com.sds.study.newbabyseaterapp.calendar.CalendarActivity.TODAY_DATE;
import static com.sds.study.newbabyseaterapp.calendar.CalendarActivity.TODAY_MONTH;
import static com.sds.study.newbabyseaterapp.calendar.CalendarActivity.TODAY_YEAR;
import static com.sds.study.newbabyseaterapp.calendar.CalendarActivity.db;

/**
 * View to display a month
 */
public class OneMonthLayout extends LinearLayout implements View.OnClickListener {

    public interface OnClickDayListener {
        void onClick(OneDayLayout oneDayLayout);
    }

    CalendarDAO calendarDAO;

    int this_year;
    int this_month;

    String TAG;

    ArrayList<LinearLayout> weeks = null;
    public ArrayList<OneDayLayout> oneDayLayouts = null;
    ArrayList<Integer> isInThisMonths = null;
    OnClickDayListener onClickDayListener;
    private final OnClickDayListener dummyClickDayListener = new OnClickDayListener() {
        @Override
        public void onClick(OneDayLayout oneDayLayout) {

        }
    };

    public void setOnClickDayListener(OnClickDayListener onClickDayListener) {
        if (onClickDayListener != null) {
            this.onClickDayListener = onClickDayListener;
        }
        else {
            this.onClickDayListener = dummyClickDayListener;
        }
    }


    public OneMonthLayout(Context context) {
        this(context, null);
    }

    public OneMonthLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OneMonthLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TAG = this.getClass().getName()+"/Canet";

        setOrientation(LinearLayout.VERTICAL);
        onClickDayListener = dummyClickDayListener;

        //Prepare many day-views enough to prevent recreation.
        if(weeks == null) {

            weeks = new ArrayList<>(6); //Max 6 weeks in a month
            oneDayLayouts = new ArrayList<>(42); // 7 days * 6 weeks = 42 days

            LinearLayout layout_oneday = null;

            for(int i=0; i<42; i++) {

                if(i % 7 == 0) {
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

                OneDayLayout oneDayLayout = new OneDayLayout(context);
                oneDayLayout.setLayoutParams(params);
                oneDayLayout.setOnClickListener(this);

                layout_oneday.addView(oneDayLayout);
                oneDayLayouts.add(oneDayLayout);
            }
        }

        //for Preview of Graphic editor
        if(isInEditMode()) {
            Calendar calendar = Calendar.getInstance();
            make(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH));
        }

    }

    /**
     * Get current year
     * @return 4 digits number of year
     */
    public int getYear() {
        return this_year;
    }

    /**
     * Get current month
     * @return 0~11 (Calendar.JANUARY ~ Calendar.DECEMBER)
     */
    public int getMonth() {
        return this_month;
    }


    /**
     * Any layout manager that doesn't scroll will want this.
     */
    @Override
    public boolean shouldDelayChildPressedState() {
        return false;
    }


    /**
     * Make a Month view
     * @param year year of this month view (4 digits number)
     * @param month month of this month view (0~11)
     */
    public void make(int year, int month)
    {
        calendarDAO = new CalendarDAO(db);

        if(this_year == year && this_month == month) {
            return;
        }

        long makeTime = System.currentTimeMillis();

        this.this_year = year;
        this.this_month = month;

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, 1);
        calendar.setFirstDayOfWeek(Calendar.SUNDAY);//Sunday is first day of week in this sample

        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);//Get day of the week in first day of this month
        int maxOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);//Get max day number of this month
        ArrayList<OneDayData> oneDayDataList = new ArrayList<>();
        isInThisMonths = new ArrayList<>();

        calendar.add(Calendar.DAY_OF_MONTH, Calendar.SUNDAY - dayOfWeek);//Move to first day of first week
        //HLog.d(TAG, CLASS, "first day : " + cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.KOREA) + " / " + cal.get(Calendar.DAY_OF_MONTH));

        /* add previous month */
        int seekDay;
        for(;;) {
            seekDay = calendar.get(Calendar.DAY_OF_WEEK);
            if(dayOfWeek == seekDay) break;

            OneDayData oneDayData = new OneDayData();
            oneDayData.setDay(calendar);
            oneDayDataList.add(oneDayData);
            isInThisMonths.add(0);
            //하루 증가
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        //HLog.d(TAG, CLASS, "this month : " + cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.KOREA) + " / " + cal.get(Calendar.DAY_OF_MONTH));
        /* add this month */
        for(int i=0; i < maxOfMonth; i++) {
            OneDayData oneDayData = new OneDayData();
            oneDayData.setDay(calendar);
            oneDayDataList.add(oneDayData);
            isInThisMonths.add(1);
            //add one day
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        /* add next month */
        for(;;) {
            if(calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                OneDayData oneDayData = new OneDayData();
                oneDayData.setDay(calendar);
                oneDayDataList.add(oneDayData);
                isInThisMonths.add(0);
            }
            else {
                break;
            }
            //add one day
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        if(oneDayDataList.size() == 0) return;

        //Remove all day-views
        this.removeAllViews();

        int count = 0;
        for(OneDayData oneDayData : oneDayDataList) {

            int today_date = oneDayData.calendar.get(Calendar.DAY_OF_MONTH);
            int today_month = oneDayData.calendar.get(Calendar.MONTH)+1;
            int today_year = oneDayData.calendar.get(Calendar.YEAR);

            int today_date_id = today_year*10000+today_month*100+today_date;

            int diaryCount = calendarDAO.countDailyList(today_date_id);
            int scheduleCount = calendarDAO.countScheduleList(today_date_id);

            if(count % 7 == 0) {
                addView(weeks.get(count / 7));
            }
            OneDayLayout oneDayLayout = oneDayLayouts.get(count);
            oneDayLayout.setTag(today_date_id);

            TextView txt_day = (TextView) oneDayLayout.findViewById(R.id.txt_day);
            TextView txt_diary = (TextView) oneDayLayout.findViewById(R.id.txt_diary);
            TextView txt_schedule = (TextView) oneDayLayout.findViewById(R.id.txt_schedule);
            TextView txt_budget = (TextView) oneDayLayout.findViewById(R.id.txt_budget);
            ImageView img_icon_diary = (ImageView) oneDayLayout.findViewById(R.id.img_icon_diary);
            ImageView img_icon_schedule = (ImageView) oneDayLayout.findViewById(R.id.img_icon_schedule);
            ImageView img_icon_budget = (ImageView) oneDayLayout.findViewById(R.id.img_icon_budget);

            if(isInThisMonths.get(count)==0){

                txt_diary.setText("");
                txt_schedule.setText("");
                txt_budget.setText("");
                img_icon_diary.setImageResource(0);
                img_icon_schedule.setImageResource(0);
                img_icon_budget.setImageResource(0);
                txt_day.setAlpha(0.3f);
                oneDayLayout.findViewById(R.id.daily_calendar_layout).setBackgroundResource(R.drawable.day_cell_bg_om);
                oneDayLayout.setClickable(false);

            }else if( today_date==TODAY_DATE && today_month==TODAY_MONTH && today_year==TODAY_YEAR ){

                oneDayLayout.findViewById(R.id.daily_calendar_layout).setBackgroundResource(R.drawable.day_cell_bg_today);

                if(diaryCount!=0){

                    txt_diary.setText(Integer.toString(diaryCount));

                }else{

                    txt_diary.setText("");

                }

                if(scheduleCount!=0){

                    txt_schedule.setText(Integer.toString(scheduleCount));

                }else{

                    txt_schedule.setText("");

                }

            }else{

                oneDayLayout.findViewById(R.id.daily_calendar_layout).setBackgroundResource(R.drawable.day_cell_bg);

                if(diaryCount!=0){

                    txt_diary.setText(Integer.toString(diaryCount));

                }else{

                    txt_diary.setText("");

                }

                if(scheduleCount!=0){

                    txt_schedule.setText(Integer.toString(scheduleCount));

                }else{

                    txt_schedule.setText("");

                }

            }

            if( txt_diary.getText().toString().equals("") ){

                img_icon_diary.setImageResource(0);

            }else{

                img_icon_diary.setImageResource(R.drawable.icon_diary);

            }

            if( txt_schedule.getText().toString().equals("") ){

                img_icon_schedule.setImageResource(0);

            }else{

                img_icon_schedule.setImageResource(R.drawable.icon_schedule);

            }

            if( txt_budget.getText().toString().equals("") ){

                img_icon_budget.setImageResource(0);

            }else{

                img_icon_budget.setImageResource(R.drawable.icon_budget);

            }

            oneDayLayout.setDay(oneDayData);
            oneDayLayout.refresh();
            count++;
        }

        //Set the weight-sum of LinearLayout to week counts
        this.setWeightSum(getChildCount());

    }


    protected String doubleString(int value) {

        String temp;

        if(value < 10){
            temp = "0"+ String.valueOf(value);

        }else {
            temp = String.valueOf(value);
        }
        return temp;
    }

    @Override
    public void onClick(View view) {

        OneDayLayout oneDayLayout = (OneDayLayout) view;

        this.onClickDayListener.onClick(oneDayLayout);

    }

}