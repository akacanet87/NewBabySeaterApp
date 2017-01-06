package com.sds.study.newbabyseaterapp.calendar.diary;

/**
 * Created by CANET on 2017-01-05.
 */

public final class DiaryTag{

    private int view_num;
    private Diary diary;

    public int getView_num(){

        return view_num;
    }

    public void setView_num(int view_num){

        this.view_num = view_num;
    }

    public Diary getDiary(){

        return diary;
    }

    public void setDiary(Diary diary){

        this.diary = diary;
    }
}
