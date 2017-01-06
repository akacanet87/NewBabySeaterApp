package com.sds.study.newbabyseaterapp.calendar.calendar;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;

/**
 * PagerAdapter to view calendar monthly
 *
 * @author Brownsoo
 */
public class OneMonthPagerAdapter extends PagerAdapter{


    @SuppressWarnings("unused")
    private OneMonthFragment oneMonthFragment;

    public OneMonthLayout[] monthViews;
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
    final Calendar BASE_CAL;
    /**
     * Page numbers to reuse
     */
    final static int PAGES = 5;
    /**
     * Loops, I think 1000 may be infinite scroll.
     */
    final static int LOOPS = 1000;
    /**
     * position basis
     */
    final static int BASE_POSITION = PAGES * LOOPS / 2;

    public int thisPosition;
    public Calendar thisCalendar;

    public OneMonthPagerAdapter(OneMonthFragment oneMonthFragment, int startYear, int startMonth){

        this.oneMonthFragment = oneMonthFragment;
        Calendar base = Calendar.getInstance();
        base.set(BASE_YEAR, BASE_MONTH, 1);
        BASE_CAL = base;

        monthViews = new OneMonthLayout[PAGES];
        for(int i = 0; i < PAGES; i++){
            monthViews[i] = new OneMonthLayout(oneMonthFragment.getContext());
        }

    }

    /**
     * Get the page position by given date
     *
     * @param year  4 digits number of year
     * @param month month number
     *
     * @return page position
     */
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
        monthViews[position].setOnClickDayListener(oneMonthFragment.onClickDayListener);

        return monthViews[position];
    }

    /**
     * How many months exist from the base month to the given values?
     *
     * @param year  the year to compare with the base year
     * @param month the month to compare with the base month
     *
     * @return counts of month
     */
    public int howFarFromBase(int year, int month){

        int disY = (year - BASE_YEAR) * 12;
        int disM = month - BASE_MONTH;

        return disY + disM;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object){

        ((OneMonthLayout) object).setOnClickDayListener(null);
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

    /**
     * Object to preserve year and month
     * @author Brownsoo
     *
     */

    /**
     * Get the particular date by page position
     *
     * @param position page position
     *
     * @return YearMonth
     */
    public YearMonth getYearMonth(int position){

        Calendar cal = (Calendar) BASE_CAL.clone();
        cal.add(Calendar.MONTH, position - BASE_POSITION);

        YearMonth yearMonth = new YearMonth(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH));

        return yearMonth;
    }

}
