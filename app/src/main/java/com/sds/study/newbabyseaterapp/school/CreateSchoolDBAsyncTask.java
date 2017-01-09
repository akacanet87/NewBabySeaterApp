package com.sds.study.newbabyseaterapp.school;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by CANET on 2017-01-09.
 */

public class CreateSchoolDBAsyncTask extends AsyncTask<String, Integer, String>{

    Context context;
    JSONArray array;
    SQLiteDatabase db;
    HttpURLConnection con;
    URL url;
    ArrayList<School> schoolsInfo;
    BufferedReader buffR;
    ProgressDialog progress;
    
    String TAG;
    String json;
    int maxListSize = 50000;

    String key = "zNUxHwqZV0QfCgDkhHtNKqyPfsEEHmNfp0%2FO0zFHfmg1sujkxk%2FJVxf4qml60BaH219L795Fhlwx7vuiGAFahg%3D%3D";
    String api_address = "http://api.data.go.kr/openapi/0c9e6948-e327-404b-89bf-2506d4684c1c";

    String errorCode = "[\"ERROR\",\"인증되지";

    public CreateSchoolDBAsyncTask(Context context, SQLiteDatabase db){

        this.context = context;
        this.db = db;
        TAG = this.getClass().getName()+"/Canet";
        progress = new ProgressDialog(context);
    
    }


    @Override
    protected void onPreExecute(){

        super.onPreExecute();
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setMessage("어린이집 데이터베이스를 받는 중입니다..");
        progress.show();
        
    }

    @Override
    protected String doInBackground(String... strings){

        Log.d(TAG, "doInBackground 실행");

        json = getJson();
        //parseJson(json);

        return json;
        
    }

    @Override
    protected void onPostExecute(String s){

        Log.d(TAG, "onPostExecute 실행");
        Log.d(TAG, s);

        if(s.contains(errorCode)){

            showAlertMsg("안내","데이터베이스를 받아오는데 실패하였습니다.\n잠시 후 다시 시도해 주세요.");
            return;

        }else{

            parseJson(s);
            progress.dismiss();

            super.onPostExecute(s);

        }

    }

    public String getJson(){

        String string_url = api_address + "?serviceKey=" + key + "&s_page=1" + "&s_list=" + maxListSize + "&type=json" + "&numOfRows=1" + "&pageNo=1";

        StringBuffer sb = new StringBuffer();

        try{

            Log.d(TAG, "JSON 요청 주소 " + string_url);
            url = new URL(string_url);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            int responseCode = con.getResponseCode();
            Log.d(TAG, Integer.toString(responseCode));
            if(responseCode != HttpURLConnection.HTTP_OK) return null;

            buffR = new BufferedReader(new InputStreamReader(con.getInputStream(), "euc-kr"));
            String input = null;
            sb = new StringBuffer();

            while((input = buffR.readLine()) != null){
                sb.append(input);
            }

            Log.d(TAG, "파싱성공");

        }catch(MalformedURLException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }finally{

            if(con!=null){

                con.disconnect();

            }

            if(buffR!=null){

                try{
                    buffR.close();
                }catch(IOException e){
                    e.printStackTrace();
                }

            }

        }

        return sb.toString();
    }

    public void parseJson(String json){

        Log.d(TAG, "파싱시작");

        try{
            Log.d(TAG, "트라이문 진입");
            array = new JSONArray(json);

            JSONObject jsonObject = new JSONObject(json);



            Log.d(TAG, "포문 전");


            /*for(int i = 0; i < array.length(); i++){
                if(array.get(i) == null){
                    break;
                }
                JSONObject obj_kindergarten = array.getJSONObject(i);

                String sql="insert into school(addr,school_name,lat,lng,schoolbus,max_stu_num,teacher_num,call_num,cctv_num)";
                sql+=" values(?,?,?,?,?,?,?,?,?)";

                String addr=(obj_kindergarten.getString("소재지도로명주소"));
                String school_name=(obj_kindergarten.getString("어린이집명"));
                String lat=Double.toString(obj_kindergarten.getDouble("위도"));
                String lng=Double.toString(obj_kindergarten.getDouble("경도"));
                String schoolbus=(obj_kindergarten.getString("통학차량운영여부"));
                String max_stu_num=Integer.toString(obj_kindergarten.getInt("정원수"));
                String teacher_num=Integer.toString(obj_kindergarten.getInt("보육교직원수"));
                String call_num=(obj_kindergarten.getString("어린이집전화번호"));
                String cctv_num=Integer.toString(obj_kindergarten.getInt("CCTV설치수"));

                db.execSQL(sql,new String[]{addr,school_name,lat,lng,schoolbus,max_stu_num,teacher_num,call_num,cctv_num});

            }*/

            Log.d(TAG, "list_kindergarten 길이 :" + Integer.toString(schoolsInfo.size()));
            Log.d(TAG, "array의 길이 :" + Integer.toString(array.length()));

        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    public void showAlertMsg(String title, String msg){

        AlertDialog.Builder alert = new AlertDialog.Builder(context);

        alert.setTitle(title).setMessage(msg).show();

    }
    
}
