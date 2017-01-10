package com.sds.study.newbabyseaterapp.school;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
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
    BufferedReader buffR;
    Gson gson;
    ProgressDialog progress;

    JsonInfo jsonInfo;
    SchoolInfo[] schoolInfos;
    
    String TAG;
    String json;

    //  어린이집 개수는 54000개 정도지만 json의 양이 너무 많고 시간이 오래 걸리므로 또 10000개만 해도 MalformedJsonException이 나옴
    int maxListSize = 5000;

    String key = "zNUxHwqZV0QfCgDkhHtNKqyPfsEEHmNfp0%2FO0zFHfmg1sujkxk%2FJVxf4qml60BaH219L795Fhlwx7vuiGAFahg%3D%3D";
    String api_address = "http://api.data.go.kr/openapi/0c9e6948-e327-404b-89bf-2506d4684c1c";

    String errorCode = "[\"ERROR\",\"인증되지";

    public CreateSchoolDBAsyncTask(Context context, SQLiteDatabase db){

        this.context = context;
        this.db = db;
        TAG = this.getClass().getName()+"/Canet";
    
    }


    @Override
    protected void onPreExecute(){

        super.onPreExecute();
        Log.d(TAG, "onPreExecute시작");

        gson = new Gson();
        progress = new ProgressDialog(context);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setMessage("어린이집 데이터베이스 생성중...");
        progress.show();
        
    }

    @Override
    protected String doInBackground(String... strings){

        Log.d(TAG, "doInBackground 실행");

        json = getJson();

        return json;

    }

    @Override
    protected void onPostExecute(String s){

        Log.d(TAG, "onPostExecute 실행");
        Log.d(TAG, s);

        /*if(json.contains(errorCode)||json==null){

            showAlertMsg("안내","데이터베이스를 받아오는데 실패하였습니다.\n잠시 후 다시 시도해 주세요.");
            progress.dismiss();

        }else{

            progress.dismiss();
            for(int i=0 ; i<schoolInfos.length ; i++){

                SchoolInfo schoolInfo = schoolInfos[i];

                String name = schoolInfo.school_name;

                Log.d(TAG, "name : " + name);

            }

        }*/


        if(json.contains("{\"schools\":[{")){

            progress.dismiss();
            for(int i=0 ; i<schoolInfos.length ; i++){

                SchoolInfo schoolInfo = schoolInfos[i];

                String name = schoolInfo.school_name;

                Log.d(TAG, "name : " + name);

            }

        }else{

            showAlertMsg("안내","데이터베이스를 받아오는데 실패하였습니다.\n잠시 후 다시 시도해 주세요.");
            progress.dismiss();

        }

        super.onPostExecute(s);

    }

    @Override
    protected void onCancelled(){

        Log.d(TAG, "onCancelled 호출");

        if(buffR!=null){

            try{
                buffR.close();
            }catch(IOException e){
                e.printStackTrace();
            }

        }

    }

    public String getJson(){

        String string_url = api_address + "?serviceKey=" + key + "&s_page=1" + "&s_list=" + maxListSize + "&type=json" + "&numOfRows=" + 1 + "&pageNo=1";

        StringBuffer sb = new StringBuffer();

        String newData = null;

        try{

            Log.d(TAG, "JSON 요청 주소 " + string_url);
            url = new URL(string_url);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            int responseCode = con.getResponseCode();
            Log.d(TAG, Integer.toString(responseCode));
            if(responseCode != HttpURLConnection.HTTP_OK) return null;

            buffR = new BufferedReader(new InputStreamReader(con.getInputStream(), "euc-kr"));

            Log.d(TAG, "buffR에 담음");

            /*String data = null;

            Log.d(TAG, "data에 담음");

            while((data=buffR.readLine()) != null){

                sb.append(data);

            }

            newData = "{\"schools\":"+sb.toString()+"}";*/

            newData = "{\"schools\":"+buffR.readLine()+"}";

            /*int commaIndex = 0;

            while(true){

                commaIndex = data.indexOf("},{")+2;
                if(commaIndex<=2){

                    break;

                }
                sb.append(data.substring(0, commaIndex));

                data = data.substring(commaIndex, data.length());
                //Log.d(TAG, "data : " + data);

            }*/

            //Log.d(TAG, "sb.toString() : " + sb.toString());

            Log.d(TAG, "sb에 담음, buffR.readLine() 끝남");

            if(!newData.contains(errorCode)&&newData.contains("{\"schools\":[{")){

                Log.d(TAG, "파싱성공");

                JsonReader reader = new JsonReader(new StringReader(newData));
                reader.setLenient(true);
                JsonParser parser = new JsonParser();
                JsonElement rootObejct = parser.parse(reader).getAsJsonObject().get("schools");

                schoolInfos = gson.fromJson(rootObejct, SchoolInfo[].class);

                Log.d(TAG, "schoolInfos.length : " + schoolInfos.length);
                Log.d(TAG, "gson파싱");

            }

            Log.d(TAG, "newData : " + newData);

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

        return newData;
    }

    public void parseJson(String json){

        /*Log.d(TAG, "파싱시작");

        Log.d(TAG, "트라이문 진입");

        try{
            array = new JSONArray(json);

            for(int i=0; i<array.length() ; i++){

                jsonObjects.add(array.getJSONObject(i));

                String name = jsonObjects.get(i).getString("어린이집명");

                Log.d(TAG, "name : " + name);

            }

        }catch(JSONException e){
            e.printStackTrace();
        }*/

        /*try{

            Log.d(TAG, "포문 전");


            *//*for(int i = 0; i < array.length(); i++){
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

            }*//*

            Log.d(TAG, "list_kindergarten 길이 :" + Integer.toString(schoolsInfo.size()));
            Log.d(TAG, "array의 길이 :" + Integer.toString(array.length()));

        }catch(JSONException e){
            e.printStackTrace();
        }*/
    }

    public void showAlertMsg(String title, String msg){

        AlertDialog.Builder alert = new AlertDialog.Builder(context);

        alert.setTitle(title).setMessage(msg).show();

    }
    
}