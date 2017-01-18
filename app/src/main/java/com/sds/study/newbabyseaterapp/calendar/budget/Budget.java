package com.sds.study.newbabyseaterapp.calendar.budget;

/**
 * Created by CANET on 2017-01-06.
 */

public class Budget{

    private int budget_id;
    private int date_id;
    private int cost;
    private int month;
    private int year;
    private int hour;
    private int minute;
    private String payment_method;
    private String bank_name;
    private String place;
    private String content;
    private String date;

    public int getBudget_id(){

        return budget_id;
    }

    public void setBudget_id(int budget_id){

        this.budget_id = budget_id;
    }

    public int getDate_id(){

        return date_id;
    }

    public void setDate_id(int date_id){

        this.date_id = date_id;
    }

    public int getCost(){

        return cost;
    }

    public void setCost(int cost){

        this.cost = cost;
    }

    public int getMonth(){

        return month;
    }

    public void setMonth(int month){

        this.month = month;
    }

    public int getYear(){

        return year;
    }

    public void setYear(int year){

        this.year = year;
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

    public String getPayment_method(){

        return payment_method;
    }

    public void setPayment_method(String payment_method){

        this.payment_method = payment_method;
    }

    public String getBank_name(){

        return bank_name;
    }

    public void setBank_name(String bank_name){

        this.bank_name = bank_name;
    }

    public String getPlace(){

        return place;
    }

    public void setPlace(String place){

        this.place = place;
    }

    public String getContent(){

        return content;
    }

    public void setContent(String content){

        this.content = content;
    }

    public String getDate(){

        return date;
    }

    public void setDate(String date){

        this.date = date;
    }
}
