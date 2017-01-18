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
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by CANET on 2017-01-09.
 */

public class CreateSchoolDBAsyncTask extends AsyncTask<Integer, Integer, Integer>{

    Context context;
    SQLiteDatabase db;
    HttpURLConnection con;
    URL url;
    BufferedReader buffR;
    Gson gson;
    ProgressDialog progress;

    SchoolInfo[] schoolInfos;
    SchoolDAO schoolDAO;
    
    String TAG;
    String json;

    //  어린이집 개수는 54000개 정도지만 json의 양이 너무 많다. 20000개 30초, 25000개 1분
    int maxListSize = 35000;

    String key = "zNUxHwqZV0QfCgDkhHtNKqyPfsEEHmNfp0%2FO0zFHfmg1sujkxk%2FJVxf4qml60BaH219L795Fhlwx7vuiGAFahg%3D%3D";
    String api_address = "http://api.data.go.kr/openapi/0c9e6948-e327-404b-89bf-2506d4684c1c";

    String errorCode = "[\"ERROR\",\"인증되지";

    public static final int SAVE_SUCCESS = 0001;
    public static final int SAVE_FAILED = 0002;

    public CreateSchoolDBAsyncTask(Context context, SQLiteDatabase db){

        this.context = context;
        this.db = db;
        TAG = this.getClass().getName()+"/Canet";
    
    }


    @Override
    protected void onPreExecute(){

        super.onPreExecute();
        Log.d(TAG, "onPreExecute시작");

        schoolDAO = new SchoolDAO(context, db);
        gson = new Gson();
        progress = new ProgressDialog(context);
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setCanceledOnTouchOutside(false);
        progress.setMessage("어린이집 데이터베이스 받아오는 중...\n잠시만 기다리시면 시작됩니다.\n(1~5분 정도 소요됩니다.)");
        progress.setMax(maxListSize);
        progress.show();

        Log.d(TAG, "json 받기 완료");
        
    }

    @Override
    protected Integer doInBackground(Integer... values){

        json = getJson();

        Log.d(TAG, "doInBackground 실행");

        int result = 0;

        if(json.contains("{\"schools\":[{")){

            for(int i=0 ; i<schoolInfos.length ; i++){

                publishProgress(i);

                SchoolInfo schoolInfo = schoolInfos[i];

                schoolDAO.insertSchool(schoolInfo);

            }

            Log.d(TAG, "작업 완료!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            result = SAVE_SUCCESS;

        }else{

            result = SAVE_FAILED;

        }

        return result;

    }

    @Override
    protected void onProgressUpdate(Integer... values){

        super.onProgressUpdate(values);

        progress.setProgress(values[0]);

    }

    @Override
    protected void onPostExecute(Integer result){

        Log.d(TAG, "onPostExecute 실행");
        Log.d(TAG, "result : " + result);

        progress.dismiss();
        progress=null;

        if(result==SAVE_SUCCESS){

            showAlertMsg("안내","데이터베이스 저장 완료!");

        }else if(result==SAVE_FAILED){

            showAlertMsg("안내","데이터베이스를 받아오는데 실패하였습니다.\n잠시 후 다시 시도해 주세요.");

        }

        super.onPostExecute(result);

    }

    @Override
    protected void onCancelled(){

        Log.d(TAG, "onCancelled 호출");

        if(progress.isShowing()){

            progress.dismiss();

        }

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

        //StringBuffer sb = new StringBuffer();

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

            //  1줄밖에 안넘어와서 while문이 필요 없다.
            newData = "{\"schools\":"+buffR.readLine()+"}";

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
