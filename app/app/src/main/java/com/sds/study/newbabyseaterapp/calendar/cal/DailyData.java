package com.sds.study.newbabyseaterapp.calendar.cal;

import java.util.Calendar;

public class DailyData{

    Calendar calendar;

    public DailyData() {

        this.calendar = Calendar.getInstance();

    }

    public void setDay(Calendar cal) {

        this.calendar = (Calendar) cal.clone();

    }

    public int get(int field) throws IllegalArgumentException, ArrayIndexOutOfBoundsException {

        return calendar.get(field);

    }

}
