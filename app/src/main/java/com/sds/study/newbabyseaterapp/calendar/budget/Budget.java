package com.sds.study.newbabyseaterapp.calendar.budget;

/**
 * Created by CANET on 2017-01-06.
 */

public class Budget{

    private int budgetcard_id;
    private int date_id;
    private int cost;
    private String payment;
    private String place;
    private String content;

    public int getBudgetcard_id(){

        return budgetcard_id;
    }

    public void setBudgetcard_id(int budgetcard_id){

        this.budgetcard_id = budgetcard_id;
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

    public String getPayment(){

        return payment;
    }

    public void setPayment(String payment){

        this.payment = payment;
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
}
