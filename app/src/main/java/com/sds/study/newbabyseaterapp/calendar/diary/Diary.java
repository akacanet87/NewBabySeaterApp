package com.sds.study.newbabyseaterapp.calendar.diary;

/**
 * Created by CANET on 2017-01-03.
 */

public class Diary{

    private int diary_id;
    private int date_id;
    private String title;
    private String content;
    //private String img_path;

    public int getDiary_id(){

        return diary_id;
    }

    public void setDiary_id(int diary_id){

        this.diary_id = diary_id;
    }

    public int getDate_id(){

        return date_id;
    }

    public void setDate_id(int date_id){

        this.date_id = date_id;
    }

    public String getTitle(){

        return title;
    }

    public void setTitle(String title){

        this.title = title;
    }

    public String getContent(){

        return content;
    }

    public void setContent(String content){

        this.content = content;
    }

    /*public String getImg_path(){

        return img_path;
    }

    public void setImg_path(String img_path){

        this.img_path = img_path;
    }*/
}
