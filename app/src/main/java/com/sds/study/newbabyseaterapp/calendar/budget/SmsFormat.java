package com.sds.study.newbabyseaterapp.calendar.budget;

import android.util.Log;

/**
 * Created by CANET on 2017-01-06.
 */

public class SmsFormat{

    String TAG;

    private String time;
    private String card;
    private String place;
    private int cost;

    public SmsFormat(){

        TAG = this.getClass().getName()+"/Canet";

    }

    public void getLines(String sms, int[] lines ){

        String[] msgarr = sms.split("\n");

        String dateTime = msgarr[lines[0]];
        String cardString = msgarr[lines[1]];
        String placeString = msgarr[lines[2]];
        String costString = msgarr[lines[3]];

        String[] splitDT = dateTime.split(" ");

        Log.d(TAG, "time : "+splitDT[1]);

        this.setTime(splitDT[1]);

        if(cardString.contains("[")){

            Log.d(TAG, "card : "+cardString.substring(cardString.indexOf("["),cardString.indexOf("]")));

            this.setCard(cardString.substring(cardString.indexOf("["),cardString.indexOf("]")));

        }else if(cardString.contains("(")){

            Log.d(TAG, "card : "+cardString.substring(0,cardString.indexOf("(")));

            this.setCard(cardString.substring(0,cardString.indexOf("(")));

        }else if(cardString.contains(" ")){

            String[] splitCard = cardString.split(" ");

            Log.d(TAG, "card : "+splitCard[0]);

            this.setCard(splitCard[0]);

        }

        Log.d(TAG, "place : "+placeString);

        this.setPlace(placeString);

        Log.d(TAG, "cost : "+costString.substring(0,costString.indexOf("원")).replaceAll(",",""));

        this.setCost(Integer.parseInt(costString.substring(0,costString.indexOf("원")).replaceAll(",","")));

    }

    public String getTime(){

        return time;
    }

    public void setTime(String time){

        this.time = time;
    }

    public String getCard(){

        return card;
    }

    public void setCard(String card){

        this.card = card;
    }

    public String getPlace(){

        return place;
    }

    public void setPlace(String place){

        this.place = place;
    }

    public int getCost(){

        return cost;
    }

    public void setCost(int cost){

        this.cost = cost;
    }
}
