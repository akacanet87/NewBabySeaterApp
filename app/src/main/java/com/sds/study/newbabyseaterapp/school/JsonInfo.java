package com.sds.study.newbabyseaterapp.school;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by CANET on 2017-01-10.
 */

public class JsonInfo{

    private ArrayList<SchoolInfo> schoolInfos;

    public ArrayList<SchoolInfo> getSchoolInfos(){

        return schoolInfos;
    }

    public void setSchoolInfos(ArrayList<SchoolInfo> schoolInfos){

        this.schoolInfos = schoolInfos;
    }
}
