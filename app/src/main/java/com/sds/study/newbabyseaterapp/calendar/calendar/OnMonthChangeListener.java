package com.sds.study.newbabyseaterapp.calendar.calendar;

/**
 * 현재 달이 바뀌었을 경우 호출
 */

public interface OnMonthChangeListener{

    /**
     * Notify current month is changed
     * @param year 4 digits number of year
     * @param month number of month (0~11)
     */
    void onChange(int year, int month);

    /**
     * Callback for clicking on a day cell
     * @param dayView OneDayView instance that dispatching this callback
     */
    void onDayClick(OneDayLayout dayView);

    void onDayClick(DailyLayout dayView);

}
