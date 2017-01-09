package com.sds.study.newbabyseaterapp.school;


public class School{

    private int school_id;
    String school_name;
    String location;
    String address;
    String total_kids;
    String num_teacher;
    String tel_school;
    String num_CCTV;

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

    public String getLocation(){

        return location;
    }

    public void setLocation(String location){

        this.location = location;
    }

    public String getAddress(){

        return address;
    }

    public void setAddress(String address){

        this.address = address;
    }

    public String getTotal_kids(){

        return total_kids;
    }

    public void setTotal_kids(String total_kids){

        this.total_kids = total_kids;
    }

    public String getNum_teacher(){

        return num_teacher;
    }

    public void setNum_teacher(String num_teacher){

        this.num_teacher = num_teacher;
    }

    public String getTel_school(){

        return tel_school;
    }

    public void setTel_school(String tel_school){

        this.tel_school = tel_school;
    }

    public String getNum_CCTV(){

        return num_CCTV;
    }

    public void setNum_CCTV(String num_CCTV){

        this.num_CCTV = num_CCTV;
    }
}
