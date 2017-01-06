package com.sds.study.newbabyseaterapp.calendar.calendar;

import java.util.Calendar;

public class OneDayData {
    
    Calendar calendar;
    private String msg;

    public OneDayData() {

        this.calendar = Calendar.getInstance();

    }

    public void setDay(int year, int month, int day) {

        calendar = Calendar.getInstance();
        calendar.set(year, month, day);

    }

    public void setDay(Calendar cal) {

        this.calendar = (Calendar) cal.clone();

    }

    public Calendar getDay() {

        return calendar;

    }

    public int get(int field) throws IllegalArgumentException, ArrayIndexOutOfBoundsException {

        return calendar.get(field);

    }

    public String getMessage() {

        return msg;

    }

    public void setMessage(String msg) {

        this.msg = msg;

    }
}
