package com.sds.study.newbabyseaterapp.calendar.budget;

/**
 * Created by CANET on 2017-01-06.
 */

public class TotalBudget{

    private int total_budget_id;
    private int month;
    private int year;
    private int budget;

    public int getTotal_budget_id(){

        return total_budget_id;
    }

    public void setTotal_budget_id(int total_budget_id){

        this.total_budget_id = total_budget_id;
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

    public int getBudget(){

        return budget;
    }

    public void setBudget(int budget){

        this.budget = budget;
    }
}
