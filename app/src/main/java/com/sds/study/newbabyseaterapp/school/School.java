package com.sds.study.newbabyseaterapp.school;


public class School{

    private int school_id;
    private String school_name;
    private double lat;
    private double lon;
    private String address;
    private int max_stu_num;
    private int teacher_num;
    private String school_tel;
    private int cctv_num;
    private String has_schoolbus;

    public int getSchool_id(){

        return school_id;
    }

    public void setSchool_id(int school_id){

        this.school_id = school_id;
    }

    public String getSchool_name(){

        return school_name;
    }

    public void setSchool_name(String school_name){

        this.school_name = school_name;
    }

    public double getLat(){

        return lat;
    }

    public void setLat(double lat){

        this.lat = lat;
    }

    public double getLon(){

        return lon;
    }

    public void setLon(double lon){

        this.lon = lon;
    }

    public String getAddress(){

        return address;
    }

    public void setAddress(String address){

        this.address = address;
    }

    public int getMax_stu_num(){

        return max_stu_num;
    }

    public void setMax_stu_num(int max_stu_num){

        this.max_stu_num = max_stu_num;
    }

    public int getTeacher_num(){

        return teacher_num;
    }

    public void setTeacher_num(int teacher_num){

        this.teacher_num = teacher_num;
    }

    public String getSchool_tel(){

        return school_tel;
    }

    public void setSchool_tel(String school_tel){

        this.school_tel = school_tel;
    }

    public int getCctv_num(){

        return cctv_num;
    }

    public void setCctv_num(int cctv_num){

        this.cctv_num = cctv_num;
    }

    public String getHas_schoolbus(){

        return has_schoolbus;
    }

    public void setHas_schoolbus(String has_schoolbus){

        this.has_schoolbus = has_schoolbus;
    }
}
