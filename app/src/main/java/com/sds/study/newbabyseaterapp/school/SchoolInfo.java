package com.sds.study.newbabyseaterapp.school;

import com.google.gson.annotations.SerializedName;

/**
 * Created by CANET on 2017-01-10.
 */

public class SchoolInfo{

    @SerializedName("어린이집명")
    public String school_name;

    @SerializedName("소재지도로명주소")
    public String address;

    @SerializedName("시도명")
    public String sido;

    @SerializedName("시군구명")
    public String sigungu;

    @SerializedName("위도")
    public String lat;

    @SerializedName("경도")
    public String lon;

    @SerializedName("어린이집전화번호")
    public String school_tel;

    @SerializedName("정원수")
    public String max_stu_num;

    @SerializedName("보육교직원수")
    public String teacher_num;

    @SerializedName("CCTV설치수")
    public String cctv_num;

    @SerializedName("통학차량운영여부")
    public String has_schoolbus;

}
