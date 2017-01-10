package com.sds.study.newbabyseaterapp.school;

import com.google.gson.annotations.SerializedName;

/**
 * Created by CANET on 2017-01-10.
 */

public class SchoolInfo{

    @SerializedName("어린이집명")
    public String school_name;

    @SerializedName("위도")
    public String lat;

    @SerializedName("경도")
    public String lon;

    @SerializedName("소재지도로명주소")
    public String address;

    @SerializedName("정원수")
    public String total_kids;

    @SerializedName("보육교직원수")
    public String num_teacher;

    @SerializedName("어린이집전화번호")
    public String tel_school;

    @SerializedName("CCTV설치수")
    public String num_CCTV="";

}
