package com.sds.study.newbabyseaterapp.calendar.schedule;

/**
 * Created by CANET on 2017-01-03.
 */

public class Schedule{

    private int schedule_id;
    private int date_id;
    private int hour;
    private int minute;
    private String content;
    private boolean alarmOff;


    public int getSchedule_id(){

        return schedule_id;
    }

    public void setSchedule_id(int schedule_id){

        this.schedule_id = schedule_id;
    }

    public int getDate_id(){

        return date_id;
    }

    public void setDate_id(int date_id){

        this.date_id = date_id;
    }

    public int getHour(){

        return hour;
    }

    public void setHour(int hour){

        this.hour = hour;
    }

    public int getMinute(){

        return minute;
    }

    public void setMinute(int minute){

        this.minute = minute;
    }

    public String getContent(){

        return content;
    }

    public void setContent(String content){

        this.content = content;
    }

    public boolean isAlarmOff(){

        return alarmOff;
    }

    public void setAlarmOff(boolean alarmOff){

        this.alarmOff = alarmOff;
    }

}
