package com.sds.study.newbabyseaterapp.calendar.cal;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;

/**
 * PagerAdapter to view calendar monthly
 *
 * @author Brownsoo
 */
public class CalendarPagerAdapter extends PagerAdapter{


    @SuppressWarnings("unused")
    private CalendarFragment calendarFragment;

    public MonthlyLayout[] monthViews;
    /**
     * Default year to calculate the page position
     */
    final static int BASE_YEAR = 2017;
    /**
     * Default month to calculate the page position
     */
    final static int BASE_MONTH = Calendar.JANUARY;
    /**
     * Calendar instance based on default year and month
     */
    public final Calendar BASE_CAL;
    /**
     * Page numbers to reuse
     */
    public final static int PAGES = 5;
    /**
     * Loops, I think 1000 may be infinite scroll.
     */
    final static int LOOPS = 1000;
    /**
     * position basis
     */
    public final static int BASE_POSITION = PAGES * LOOPS / 2;

    public int thisPosition;
    Calendar thisCalendar;

    public CalendarPagerAdapter(CalendarFragment calendarFragment){

        this.calendarFragment = calendarFragment;
        Calendar base = Calendar.getInstance();
        base.set(BASE_YEAR, BASE_MONTH, 1);
        BASE_CAL = base;

        monthViews = new MonthlyLayout[PAGES];
        for(int i = 0; i < PAGES; i++){
            monthViews[i] = new MonthlyLayout(calendarFragment.getContext());
        }

    }

    public int getPosition(int year, int month){

        Calendar cal = Calendar.getInstance();
        cal.set(year, month, 1);
        return BASE_POSITION + howFarFromBase(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position){

        int howFarFromBase = position - BASE_POSITION;
        Calendar cal = (Calendar) BASE_CAL.clone();
        cal.add(Calendar.MONTH, howFarFromBase);

        position = position % PAGES;

        thisPosition = position;
        thisCalendar = cal;

        container.addView(monthViews[position]);

        monthViews[position].make(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH));
        monthViews[position].setOnClickDayListener(calendarFragment.onClickDayListener);

        return monthViews[position];
    }

    public int howFarFromBase(int year, int month){

        int disY = (year - BASE_YEAR) * 12;
        int disM = month - BASE_MONTH;

        return disY + disM;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object){

        ((MonthlyLayout) object).setOnClickDayListener(null);
        container.removeView((View) object);
    }

    @Override
    public int getCount(){

        return PAGES * LOOPS;
    }

    @Override
    public boolean isViewFromObject(View view, Object obj){

        return view == obj;
    }

    @Override
    public int getItemPosition(Object object){

        return POSITION_NONE;

    }

    public YearMonth getYearMonth(int position){

        Calendar cal = (Calendar) BASE_CAL.clone();
        cal.add(Calendar.MONTH, position - BASE_POSITION);

        YearMonth yearMonth = new YearMonth(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH));

        return yearMonth;
    }

}
