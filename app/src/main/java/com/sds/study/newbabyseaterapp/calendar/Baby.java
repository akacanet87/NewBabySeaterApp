package com.sds.study.newbabyseaterapp.calendar;

/**
 * Created by CANET on 2017-01-15.
 */

public class Baby{

    private int baby_id;
    private String name;
    private String gender;
    private int year;
    private int month;
    private int date;

    private String birth;
    private int d_day;
    private String stellation;
    private String stone;

    public int getBaby_id(){

        return baby_id;
    }

    public void setBaby_id(int baby_id){

        this.baby_id = baby_id;
    }

    public String getName(){

        return name;
    }

    public void setName(String name){

        this.name = name;
    }

    public String getGender(){

        return gender;
    }

    public void setGender(String gender){

        this.gender = gender;
    }

    public int getYear(){

        return year;
    }

    public void setYear(int year){

        this.year = year;
    }

    public int getMonth(){

        return month;
    }

    public void setMonth(int month){

        this.month = month;
    }

    public int getDate(){

        return date;
    }

    public void setDate(int date){

        this.date = date;
    }

    public String getBirth(){

        return birth;
    }

    public void setBirth(String birth){

        this.birth = birth;
    }

    public int getD_day(){

        return d_day;
    }

    public void setD_day(int d_day){

        this.d_day = d_day;
    }

    public String getStellation(){

        return stellation;
    }

    public void setStellation(String stellation){

        this.stellation = stellation;
    }

    public String getStone(){

        return stone;
    }

    public void setStone(String stone){

        this.stone = stone;
    }
}
