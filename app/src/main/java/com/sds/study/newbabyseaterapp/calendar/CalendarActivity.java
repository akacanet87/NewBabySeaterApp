package com.sds.study.newbabyseaterapp.calendar;

import android.Manifest;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.sds.study.newbabyseaterapp.BabySeaterSqlHelper;
import com.sds.study.newbabyseaterapp.R;
import com.sds.study.newbabyseaterapp.calendar.budget.BudgetListAdapter;
import com.sds.study.newbabyseaterapp.calendar.cal.CalendarFragment;
import com.sds.study.newbabyseaterapp.calendar.cal.DailyLayout;
import com.sds.study.newbabyseaterapp.calendar.cal.OnMonthChangeListener;
import com.sds.study.newbabyseaterapp.calendar.diary.Diary;
import com.sds.study.newbabyseaterapp.calendar.diary.DiaryItem;
import com.sds.study.newbabyseaterapp.calendar.diary.DiaryListAdapter;
import com.sds.study.newbabyseaterapp.calendar.diary.DiaryTag;
import com.sds.study.newbabyseaterapp.calendar.schedule.Schedule;
import com.sds.study.newbabyseaterapp.calendar.schedule.ScheduleItem;
import com.sds.study.newbabyseaterapp.calendar.schedule.ScheduleListAdapter;
import com.sds.study.newbabyseaterapp.calendar.schedule.ScheduleTag;
import com.sds.study.newbabyseaterapp.school.SchoolActivity;
import com.sds.study.newbabyseaterapp.school.SchoolDAO;

import java.util.Calendar;

public class CalendarActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemClickListener{

    InputMethodManager imm;

    TextView calendar_txt_yearmonth, calendar_txt_thisdate;

    EditText diary_txt_title, diary_txt_content;

    TextView schedule_txt_time, schedule_txt_hour, schedule_txt_minute, schedule_txt_ampm;
    ImageButton schedule_btn_alarm, btn_alarm_schedule_item;
    EditText schedule_txt_content;
    LinearLayout layout_schedule_timepicker;

    ListView daily_schedule_list, daily_diary_list, daily_budget_list;

    Calendar calendar = Calendar.getInstance();
    CoordinatorLayout layoutContainer;
    View inc_layout_calendar, inc_layout_diarylist, inc_layout_diary, inc_layout_schedulelist, inc_layout_schedule;

    CalendarFragment calendarFragment;
    BabySeaterSqlHelper sqlHelper;    //데이터 베이스 구축
    public static SQLiteDatabase db;  //데이터 베이스 쿼리문 제어
    CalendarDAO calendarDAO;
    SchoolDAO schoolDAO;
    Diary diaryDTO;
    Schedule scheduleDTO;
    DiaryListAdapter diaryListAdapter;
    ScheduleListAdapter scheduleListAdapter;
    BudgetListAdapter budgetListAdapter;

    public static int TODAY_YEAR;
    public static int TODAY_MONTH;
    public static int TODAY_DATE;

    private boolean isBackKeyPressed = false;           // flag
    private long currentTimeByMillis = 0;               // calculate time interval

    private static final int MSG_TIMER_EXPIRED = 1;     // switch - key
    private static final int BACKKEY_TIMEOUT = 2;       // define interval
    private static final int MILLIS_IN_SEC = 1000;

    public static final int DIARY_NUM = 1000;
    public static final int SCHEDULE_NUM = 1001;
    public static final int BUDGET_NUM = 1002;
    public static final int INSERT_DIARY = 2000;
    public static final int DELETE_DIARY = 2001;
    public static final int UPDATE_DIARY = 2002;
    public static final int INSERT_SCHEDULE = 2100;
    public static final int DELETE_SCHEDULE = 2101;
    public static final int UPDATE_SCHEDULE = 2102;
    public static final int ALARM_BTN = 9000;
    public static final int NEW_ITEM = 3000;
    public static final int EDIT_ITEM = 3001;
    public static final int SMS_PERMISSION = 7000;
    public static final int COARSE_LOC_PERMISSION = 7001;
    public static final int FINE_LOC_PERMISSION = 7002;
    public static final int OVERAY_PERMISSION = 7003;
    public static final int READ_EXST_PERMISSION = 7004;

    int today_year, today_month, today_date;
    int this_hour, this_minute, my_hour, my_minute;
    int date_id;
    int ym_id;

    String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        TAG = this.getClass().getName() + "/Canet";

        initDB();

        setContentView(R.layout.activity_calendar);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        checkPermission(Manifest.permission.RECEIVE_SMS, SMS_PERMISSION);

        initCalendar();

    }

    public void initCalendar(){

        TODAY_YEAR = calendar.get(Calendar.YEAR);
        TODAY_MONTH = calendar.get(Calendar.MONTH) + 1;
        TODAY_DATE = calendar.get(Calendar.DAY_OF_MONTH);

        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        layoutContainer = (CoordinatorLayout) findViewById(R.id.layoutContainer);

        //  including된 layout들
        inc_layout_calendar = layoutContainer.findViewById(R.id.inc_layout_calendar);
        inc_layout_diarylist = layoutContainer.findViewById(R.id.inc_layout_diarylist);
        inc_layout_diary = layoutContainer.findViewById(R.id.inc_layout_diary);
        inc_layout_schedulelist = layoutContainer.findViewById(R.id.inc_layout_schedulelist);
        inc_layout_schedule = layoutContainer.findViewById(R.id.inc_layout_schedule);

        //  layout_calendar의 view들
        calendar_txt_yearmonth = (TextView) findViewById(R.id.calendar_txt_yearmonth);
        calendar_txt_thisdate = (TextView) findViewById(R.id.calendar_txt_thisdate);
        calendar_txt_thisdate.setText(Integer.toString(TODAY_DATE) + " 일");
        date_id = TODAY_YEAR * 10000 + TODAY_MONTH * 100 + TODAY_DATE;
        ym_id = TODAY_YEAR * 100 + TODAY_MONTH;

        //  inc_layout_diary의 view들
        diary_txt_title = (EditText) inc_layout_diary.findViewById(R.id.diary_txt_title);
        diary_txt_content = (EditText) inc_layout_diary.findViewById(R.id.diary_txt_content);
        //ImageView img_content = (ImageView) inc_layout_diary.findViewById(R.id.diary_img_content);

        //  inc_layout_diarylist의 view들
        daily_diary_list = (ListView) inc_layout_diarylist.findViewById(R.id.daily_diary_list);
        daily_diary_list.setOnItemClickListener(this);

        //  inc_layout_schedule의 view들
        layout_schedule_timepicker = (LinearLayout) inc_layout_schedule.findViewById(R.id.layout_schedule_timepicker);
        schedule_txt_hour = (TextView) inc_layout_schedule.findViewById(R.id.schedule_txt_hour);
        schedule_txt_minute = (TextView) inc_layout_schedule.findViewById(R.id.schedule_txt_minute);
        schedule_btn_alarm = (ImageButton) inc_layout_schedule.findViewById(R.id.schedule_btn_alarm);
        schedule_txt_content = (EditText) inc_layout_schedule.findViewById(R.id.schedule_txt_content);

        //  inc_layout_schedulelist의 view들
        daily_schedule_list = (ListView) inc_layout_schedulelist.findViewById(R.id.daily_schedule_list);
        daily_schedule_list.setOnItemClickListener(this);

        Log.d(TAG, "OneMonthFragment 생성 전");

        calendarFragment = (CalendarFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_calendar);
        calendarFragment.setOnMonthChangeListener(new OnMonthChangeListener(){

            @Override
            public void onChange(int year, int month){

                today_year = year;
                today_month = month + 1;
                calendar_txt_yearmonth.setText(Integer.toString(today_year) + " 년  " + Integer.toString(today_month) + " 월");

            }

            @Override
            public void onDayClick(DailyLayout dayView){

                today_date = dayView.get(Calendar.DAY_OF_MONTH);
                calendar_txt_thisdate.setText(Integer.toString(today_date) + " 일");

                date_id = today_year * 10000 + today_month * 100 + today_date;

            }

        });

        //Log.d(TAG, "initCalendar 완료");

    }

    public void initDB(){

        sqlHelper = new BabySeaterSqlHelper(this, "babyseater.sqlite", null, 1);
        db = sqlHelper.getWritableDatabase();
        calendarDAO = new CalendarDAO(this, db);
        schoolDAO = new SchoolDAO(this, db);
        diaryListAdapter = new DiaryListAdapter(this, db);
        scheduleListAdapter = new ScheduleListAdapter(this, db);
        budgetListAdapter = new BudgetListAdapter(this, db);

        /*if(schoolDAO.selectOne()==0){

            readExcelFile();

        }*/


        //Log.d(TAG, "initDB 완료");

    }

    public boolean checkPermission(String manifestPermission, int permissionNum){

        Log.d(TAG, "checkPermission 진입");

        boolean isPassed = false;

        int permission = ContextCompat.checkSelfPermission(this, manifestPermission);
        if(permission == PackageManager.PERMISSION_DENIED){

            ActivityCompat.requestPermissions(this, new String[]{
                    manifestPermission
            }, permissionNum);

        }else{

            isPassed = !isPassed;

        }

        return isPassed;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){

        switch(requestCode){

            case SMS_PERMISSION:

                if(permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED){
                    showAlertMsg("문자 관련 권한 안내", "권한을 부여하지 않으면 일부 기능을 사용 할 수 없습니다.");
                }

                break;

            /*case READ_EXST_PERMISSION :

                if(permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED){
                    showAlertMsg("내장 디렉토리 읽기 권한 안내", "권한을 부여하지 않으면 일부 기능을 사용 할 수 없습니다.");

                    return;
                }

                break;*/

            case FINE_LOC_PERMISSION:

                if(permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED){
                    showAlertMsg("위치 관련 권한 안내1", "권한을 부여하지 않으면 일부 기능을 사용 할 수 없습니다.");
                    Log.d(TAG, "FINE_LOC_PERMISSION 체크 완료");

                    return;

                }

                //showAlertMsg("안내", "상단의 나침반 아이콘을 클릭하여 위치를 재설정해 주세요.");

            case COARSE_LOC_PERMISSION:

                if(permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED){
                    showAlertMsg("위치 관련 권한 안내2", "권한을 부여하지 않으면 일부 기능을 사용 할 수 없습니다.");
                    Log.d(TAG, "COARSE_LOC_PERMISSION 체크 완료");

                    return;

                }

                //showAlertMsg("안내", "상단의 나침반 아이콘을 클릭하여 위치를 재설정해 주세요.");

                break;

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_item, menu);
        return true;
    }

    @Override
    public void onBackPressed(){

        Log.d(TAG, "back버튼 눌림");
        Log.d(TAG, "date_id는 " + date_id);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{

            if(inc_layout_calendar.getVisibility() == View.VISIBLE){

                if(!isBackKeyPressed){
                    // first click
                    isBackKeyPressed = !isBackKeyPressed;

                    currentTimeByMillis = Calendar.getInstance().getTimeInMillis();
                    Toast.makeText(this, "\'뒤로\'버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();

                    startTimer();

                }else if(isBackKeyPressed){
                    // second click : 2초 이내면 종료! 아니면 아무것도 안한다.
                    isBackKeyPressed = !isBackKeyPressed;
                    if(Calendar.getInstance().getTimeInMillis() <= (currentTimeByMillis + (BACKKEY_TIMEOUT * MILLIS_IN_SEC))){
                        finish();
                    }

                }

            }else if(inc_layout_diarylist.getVisibility() == View.VISIBLE){

                setLayoutVisible(inc_layout_diarylist, inc_layout_calendar);
                refreshCalendar();

            }else if(inc_layout_diary.getVisibility() == View.VISIBLE){

                setLayoutVisible(inc_layout_diary, inc_layout_diarylist);
                refreshDiaryContents();

            }else if(inc_layout_schedulelist.getVisibility() == View.VISIBLE){

                setLayoutVisible(inc_layout_schedulelist, inc_layout_calendar);
                refreshCalendar();

            }else if(inc_layout_schedule.getVisibility() == View.VISIBLE){

                setLayoutVisible(inc_layout_schedule, inc_layout_schedulelist);
                refreshDiaryContents();

            }

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch(item.getItemId()){

            case R.id.btn_school:

                Log.d(TAG, "스쿨버튼 클릭");

                boolean isCoarsePassed = checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION, COARSE_LOC_PERMISSION);
                boolean isFinePassed = checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, FINE_LOC_PERMISSION);
                /*boolean isReadExPassed = checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, READ_EXST_PERMISSION);*/

                if(isCoarsePassed || isFinePassed){

                    Intent intent = new Intent(this, SchoolActivity.class);
                    startActivity(intent);
                    Log.d(TAG, "인텐트 넘김");

                    finish();

                }

                /*else if(isCoarsePassed){

                    Intent intent = new Intent(this, SchoolActivity.class);
                    startActivity(intent);
                    Log.d(TAG, "인텐트 넘김");

                    finish();

                }else if(isFinePassed){

                    Intent intent = new Intent(this, SchoolActivity.class);
                    startActivity(intent);
                    Log.d(TAG, "인텐트 넘김");

                    finish();

                }*/

                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item){
        // Handle navigation view item clicks here.

        switch(item.getItemId()){

            case R.id.nav_btn_setbudget:
                break;
            case R.id.nav_btn_billslist:
                break;

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l){

        if(adapterView == daily_diary_list){

            Log.d(TAG, l + "은 뭘까");
            Log.d(TAG, i + " 번째 다이어리 아이템 클릭");
            DiaryItem diaryItem = (DiaryItem) view;
            Diary diary = diaryItem.getDiary();
            setLayoutVisible(inc_layout_diarylist, inc_layout_diary);
            inc_layout_diary.findViewById(R.id.diary_btn_save).setVisibility(View.GONE);
            inc_layout_diary.findViewById(R.id.diary_btn_edit).setVisibility(View.VISIBLE);
            imm.showSoftInput(inc_layout_diary.findViewById(R.id.diary_txt_title), 0);

            diary_txt_title.setText(diary.getTitle());
            diary_txt_content.setText(diary.getContent());


            String title = diary.getTitle();
            String content = diary.getContent();
            int date_id = diary.getDate_id();
            int diary_id = diary.getDiary_id();

            diaryDTO = new Diary();

            diaryDTO.setDiary_id(diary_id);
            diaryDTO.setDate_id(date_id);
            diaryDTO.setTitle(title);
            diaryDTO.setContent(content);

            Log.d(TAG, "title : " + title);
            Log.d(TAG, "content : " + content);
            Log.d(TAG, "date_id : " + date_id);
            Log.d(TAG, "diary_id : " + diary_id);

        }else if(adapterView == daily_schedule_list){

            Log.d(TAG, l + "은 뭘까");
            Log.d(TAG, i + " 번째 스케줄 아이템 클릭");
            ScheduleItem scheduleItem = (ScheduleItem) view;
            Schedule schedule = scheduleItem.getSchedule();
            setLayoutVisible(inc_layout_schedulelist, inc_layout_schedule);
            inc_layout_schedule.findViewById(R.id.schedule_btn_save).setVisibility(View.GONE);
            inc_layout_schedule.findViewById(R.id.schedule_btn_edit).setVisibility(View.VISIBLE);

            int hour = schedule.getHour();
            int minute = schedule.getMinute();
            String content = schedule.getContent();
            int date_id = schedule.getDate_id();
            int schedule_id = schedule.getSchedule_id();
            boolean isAlarmOff = schedule.isAlarmOff();

            Log.d(TAG, "schedule_id : " + schedule_id);
            Log.d(TAG, "date_id : " + date_id);
            Log.d(TAG, "hour : " + hour);
            Log.d(TAG, "minute : " + minute);
            Log.d(TAG, "content : " + content);
            Log.d(TAG, "isAlarmOff : " + isAlarmOff);

            if(isAlarmOff){
                schedule_btn_alarm.setImageResource(R.drawable.alarm_off);
            }else{
                schedule_btn_alarm.setImageResource(R.drawable.alarm);
            }
            schedule_txt_hour.setText(Integer.toString(hour));
            schedule_txt_minute.setText(Integer.toString(minute));
            schedule_txt_content.setText(content);

            setTimePickersTime(hour, minute);

            scheduleDTO = new Schedule();

            scheduleDTO.setSchedule_id(schedule_id);
            scheduleDTO.setDate_id(date_id);
            scheduleDTO.setHour(hour);
            scheduleDTO.setMinute(minute);
            scheduleDTO.setContent(content);
            scheduleDTO.setAlarmOff(isAlarmOff);

        }

    }

    public void btnCalendarClick(View view){

        switch(view.getId()){

            case R.id.btn_diary_list:

                Log.d(TAG, "date_id는 " + date_id);
                setLayoutVisible(inc_layout_calendar, inc_layout_diarylist);
                getDiaryList(date_id);

                break;

            case R.id.btn_add_diary_item:

                setLayoutVisible(inc_layout_diarylist, inc_layout_diary);
                imm.showSoftInput(inc_layout_diary.findViewById(R.id.diary_txt_title), 0);

                break;

            case R.id.diary_btn_save:

                insertDiaryContents();
                refreshDiaryContents();
                imm.hideSoftInputFromWindow(inc_layout_diary.findViewById(R.id.diary_txt_title).getWindowToken(), 0);
                setLayoutVisible(inc_layout_diary, inc_layout_diarylist);

                break;

            case R.id.diary_btn_edit:

                updateDiaryContents();
                refreshDiaryContents();
                imm.hideSoftInputFromWindow(inc_layout_diary.findViewById(R.id.diary_txt_title).getWindowToken(), 0);
                inc_layout_diary.findViewById(R.id.diary_btn_save).setVisibility(View.VISIBLE);
                inc_layout_diary.findViewById(R.id.diary_btn_edit).setVisibility(View.GONE);
                setLayoutVisible(inc_layout_diary, inc_layout_diarylist);

                break;

            case R.id.diary_btn_cancel:

                refreshDiaryContents();
                imm.hideSoftInputFromWindow(inc_layout_diary.findViewById(R.id.diary_txt_title).getWindowToken(), 0);
                if(inc_layout_diary.findViewById(R.id.diary_btn_save).getVisibility() == View.GONE){

                    inc_layout_diary.findViewById(R.id.diary_btn_save).setVisibility(View.VISIBLE);
                    inc_layout_diary.findViewById(R.id.diary_btn_edit).setVisibility(View.GONE);

                }
                setLayoutVisible(inc_layout_diary, inc_layout_diarylist);
                getDiaryList(date_id);

                break;

            case R.id.btn_delete_diary_item:

                Log.d(TAG, "다이어리 삭제 버튼 클릭");
                DiaryTag diaryTag = (DiaryTag) view.getTag();
                Diary item_diary = diaryTag.getDiary();

                Log.d(TAG, "key_num은 " + item_diary.getDiary_id());
                Log.d(TAG, "Date_id는 " + item_diary.getDate_id());
                Log.d(TAG, "Title은 " + item_diary.getTitle());
                Log.d(TAG, "Content는 " + item_diary.getContent());

                showConfirmMsg("다이어리 삭제", "정말 삭제하시겠습니까?", DELETE_DIARY, item_diary);

                break;

            case R.id.btn_schedule_list:

                Log.d(TAG, "date_id는 " + date_id);
                setLayoutVisible(inc_layout_calendar, inc_layout_schedulelist);
                getScheduleList(date_id);

                break;

            case R.id.btn_add_schedule_item:

                setLayoutVisible(inc_layout_schedulelist, inc_layout_schedule);
                this_hour = calendar.get(Calendar.HOUR_OF_DAY);
                this_minute = calendar.get(Calendar.MINUTE);
                schedule_txt_hour.setText(Integer.toString(this_hour));
                schedule_txt_minute.setText(Integer.toString(this_minute));
                setTimePickersTime(this_hour, this_minute);

                break;

            case R.id.schedule_btn_save:

                insertScheduleContents();
                refreshScheduleContents();
                imm.hideSoftInputFromWindow(inc_layout_schedule.findViewById(R.id.schedule_txt_content).getWindowToken(), 0);
                setLayoutVisible(inc_layout_schedule, inc_layout_schedulelist);
                getScheduleList(date_id);

                break;

            case R.id.schedule_btn_edit:

                updateScheduleContents();
                refreshScheduleContents();
                imm.hideSoftInputFromWindow(inc_layout_schedule.findViewById(R.id.schedule_txt_content).getWindowToken(), 0);
                inc_layout_schedule.findViewById(R.id.schedule_btn_save).setVisibility(View.VISIBLE);
                inc_layout_schedule.findViewById(R.id.schedule_btn_edit).setVisibility(View.GONE);
                setLayoutVisible(inc_layout_schedule, inc_layout_schedulelist);

                break;

            case R.id.schedule_btn_cancel:

                refreshScheduleContents();
                imm.hideSoftInputFromWindow(inc_layout_schedule.findViewById(R.id.schedule_txt_content).getWindowToken(), 0);
                if(inc_layout_schedule.findViewById(R.id.schedule_btn_save).getVisibility() == View.GONE){

                    inc_layout_schedule.findViewById(R.id.schedule_btn_save).setVisibility(View.VISIBLE);
                    inc_layout_schedule.findViewById(R.id.schedule_btn_edit).setVisibility(View.GONE);

                }
                setLayoutVisible(inc_layout_schedule, inc_layout_schedulelist);
                getScheduleList(date_id);

                break;

            case R.id.btn_delete_schedule_item:

                Log.d(TAG, "스케줄 삭제 버튼 클릭");
                ScheduleTag scheduleTag = (ScheduleTag) view.getTag();
                Schedule item_schedule = scheduleTag.getSchedule();
                int schedule_id = item_schedule.getSchedule_id();
                int hour = item_schedule.getHour();
                int minute = item_schedule.getMinute();
                int date_id = item_schedule.getDate_id();
                String content = item_schedule.getContent();
                boolean isAlarmOff = item_schedule.isAlarmOff();
                Log.d(TAG, "view_num은 " + scheduleTag.getView_num());
                Log.d(TAG, "schedule_id는 " + schedule_id);
                Log.d(TAG, "hour는 " + hour);
                Log.d(TAG, "minute는 " + minute);
                Log.d(TAG, "date_id는 " + date_id);
                Log.d(TAG, "content는 " + content);
                Log.d(TAG, "isAlarmOff는 " + isAlarmOff);
                showConfirmMsg("스케줄 삭제", "정말 삭제하시겠습니까?", DELETE_SCHEDULE, item_schedule);

                break;

            case R.id.layout_schedule_timepicker:

                showTimePicker();

                break;

            case R.id.schedule_btn_alarm:

                Log.d(TAG, "schedule_btn_alarm 눌림");
                Log.d(TAG, "view = " + view);
                setAlarmImg(view);

                break;

            case R.id.btn_alarm_schedule_item:

                Log.d(TAG, "btn_alarm_schedule_item 눌림");
                Log.d(TAG, "view = " + view);

                setAlarmImg(view);

                break;

        }

    }

    public void getDiaryList(int date_id){

        //Log.d(TAG, "getDiaryList 진입");
        daily_diary_list.setAdapter(diaryListAdapter);
        //Log.d(TAG, "다이어리 어댑터연결");
        diaryListAdapter.getDailyDiaryList(date_id);
        diaryListAdapter.notifyDataSetChanged();
        diaryListAdapter.notifyDataSetInvalidated();
        //Log.d(TAG, "다이어리 리스트 연결 완료");

    }

    public void getScheduleList(int date_id){

        daily_schedule_list.setAdapter(scheduleListAdapter);
        scheduleListAdapter.getDailyScheduleList(date_id);
        scheduleListAdapter.notifyDataSetChanged();
        scheduleListAdapter.notifyDataSetInvalidated();

    }

    public void insertDiaryContents(){

        diaryDTO = new Diary();

        diaryDTO.setDate_id(date_id);
        diaryDTO.setTitle(diary_txt_title.getText().toString());
        diaryDTO.setContent(diary_txt_content.getText().toString());

        String title = diaryDTO.getTitle();
        String content = diaryDTO.getContent();
        int date_id = diaryDTO.getDate_id();

        Log.d(TAG, "title : " + title);
        Log.d(TAG, "content : " + content);
        Log.d(TAG, "date_id : " + date_id);

        showConfirmMsg("다이어리 등록", "새로운 다이어리를 저장하시겠습니까?", INSERT_DIARY, diaryDTO);

    }

    public void insertScheduleContents(){

        scheduleDTO = new Schedule();

        String content = schedule_txt_content.getText().toString();

        scheduleDTO.setDate_id(date_id);
        if(my_hour == 0){
            my_hour = this_hour;
        }
        if(my_minute == 0){
            my_minute = this_minute;
        }
        scheduleDTO.setHour(my_hour);
        scheduleDTO.setMinute(my_minute);
        scheduleDTO.setContent(content);
        scheduleDTO.setAlarmOff(false);

        Log.d(TAG, "date_id : " + date_id);
        Log.d(TAG, "this_hour : " + my_hour);
        Log.d(TAG, "this_minute : " + my_minute);
        Log.d(TAG, "content : " + content);
        Log.d(TAG, "isAlarmOff : " + scheduleDTO.isAlarmOff());

        showConfirmMsg("스케줄 등록", "새로운 스케줄을 저장하시겠습니까?", INSERT_SCHEDULE, scheduleDTO);

    }

    public void updateDiaryContents(){

        diaryDTO.setTitle(diary_txt_title.getText().toString());
        diaryDTO.setContent(diary_txt_content.getText().toString());

        String title = diaryDTO.getTitle();
        String content = diaryDTO.getContent();
        int date_id = diaryDTO.getDate_id();
        int diary_id = diaryDTO.getDiary_id();

        Log.d(TAG, "수정된 title : " + title);
        Log.d(TAG, "수정된 content : " + content);
        Log.d(TAG, "수정된 date_id : " + date_id);
        Log.d(TAG, "수정된 diary_id : " + diary_id);

        showConfirmMsg("다이어리 수정", "수정하시겠습니까?", UPDATE_DIARY, diaryDTO);

    }

    public void updateScheduleContents(){

        scheduleDTO.setHour(Integer.parseInt(schedule_txt_hour.getText().toString()));
        scheduleDTO.setMinute(Integer.parseInt(schedule_txt_minute.getText().toString()));
        scheduleDTO.setContent(schedule_txt_content.getText().toString());

        int hour = scheduleDTO.getHour();
        int minute = scheduleDTO.getMinute();
        String content = scheduleDTO.getContent();
        int date_id = scheduleDTO.getDate_id();
        int schedule_id = scheduleDTO.getSchedule_id();

        Log.d(TAG, "수정된 hour : " + hour);
        Log.d(TAG, "수정된 minute : " + minute);
        Log.d(TAG, "수정된 content : " + content);
        Log.d(TAG, "수정된 date_id : " + date_id);
        Log.d(TAG, "수정된 schedule_id : " + schedule_id);

        showConfirmMsg("스케줄 수정", "수정하시겠습니까?", UPDATE_SCHEDULE, scheduleDTO);

    }

    public void refreshDiaryContents(){

        diary_txt_title.setText("");
        diary_txt_content.setText("");

    }

    public void refreshScheduleContents(){

        schedule_txt_content.setText("");
        //setTimePickersTime(this_hour, this_minute);

    }

    public View setAlarmImg(View view){

        ImageButton imgBtn = (ImageButton) view;

        ScheduleTag tag = (ScheduleTag) imgBtn.getTag();

        Log.d(TAG, "tag는 " + tag);

        if(tag == null){

            showAlertMsg("알람켜기", "저장하시면 알람이 자동으로 켜집니다.");

        }else{

            Schedule schedule = tag.getSchedule();

            int schedule_id = schedule.getSchedule_id();
            int hour = schedule.getHour();
            int minute = schedule.getMinute();
            int date_id = schedule.getDate_id();
            String content = schedule.getContent();
            boolean isAlarmOff = schedule.isAlarmOff();

            if(isAlarmOff){

                imgBtn.setImageResource(R.drawable.alarm);

            }else{

                imgBtn.setImageResource(R.drawable.alarm_off);

            }

            isAlarmOff = !isAlarmOff;
            schedule.setAlarmOff(isAlarmOff);
            tag.setSchedule(schedule);

            Log.d(TAG, "schedule_id는 " + schedule_id);
            Log.d(TAG, "hour는 " + hour);
            Log.d(TAG, "minute는 " + minute);
            Log.d(TAG, "date_id는 " + date_id);
            Log.d(TAG, "content는 " + content);
            Log.d(TAG, "isAlarmOff는 " + isAlarmOff);

            view.setTag(tag);

        }

        return view;

    }

    public void showTimePicker(){

        Dialog dlgTime = new TimePickerDialog(this, myTimeSetListener,
                this_hour, this_minute, false);
        dlgTime.show();

    }

    public void setTimePickersTime(int hourOfDay, int minute){

        schedule_txt_hour.setText(Integer.toString(hourOfDay));
        schedule_txt_minute.setText(Integer.toString(minute));

    }

    public void refreshCalendar(){


        final int position = calendarFragment.horizontalPagerAdapter.thisPosition;
        /*final int year = getYear;
        final int month = getMonth-position;*/

        Log.d(TAG, "position" + position);
        //for( int i=0 ; i<oneMonthFragment.horizontalPagerAdapter.monthViews[position].oneDayLayouts.size() ; i++ ){

        /*this.runOnUiThread(new Runnable(){

            public void run(){

                calendarFragment.horizontalPagerAdapter.getItemPosition(calendarFragment.horizontalPagerAdapter.monthViews[position]);

            }
        });*/

    }

    public void setLayoutVisible(View thisView, View nextView){

        thisView.setVisibility(View.GONE);
        nextView.setVisibility(View.VISIBLE);

    }

    public void showConfirmMsg(String title, String msg, final int site_id, Diary diary){

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final int item_id = diary.getDiary_id();
        final String item_title = diary.getTitle();
        final String item_content = diary.getContent();

        alert.setTitle(title).setMessage(msg).setCancelable(false).setPositiveButton("확인",
                new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        // 'YES'

                        switch(site_id){

                            case INSERT_DIARY:

                                calendarDAO.insertDiary(item_title, item_content, date_id);
                                showAlertMsg("안내", "저장하였습니다");
                                getDiaryList(date_id);
                                break;

                            case DELETE_DIARY:

                                calendarDAO.deleteDiary(item_id);
                                showAlertMsg("안내", "삭제하였습니다");
                                getDiaryList(date_id);
                                break;

                            case UPDATE_DIARY:

                                calendarDAO.updateDiary(item_title, item_content, item_id);
                                showAlertMsg("안내", "수정하였습니다");
                                getDiaryList(date_id);
                                break;

                        }

                    }
                }).setNegativeButton("취소",
                new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        // 'No'
                        return;
                    }
                }).show();

    }

    public void showConfirmMsg(String title, String msg, final int site_id, Schedule schedule){

        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        final int item_id = schedule.getSchedule_id();
        final int item_hour = schedule.getHour();
        final int item_minute = schedule.getMinute();
        final String item_content = schedule.getContent();

        alert.setTitle(title).setMessage(msg).setCancelable(false).setPositiveButton("확인",
                new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        // 'YES'

                        switch(site_id){

                            case INSERT_SCHEDULE:

                                calendarDAO.insertSchedule(date_id, item_hour, item_minute, item_content);
                                showAlertMsg("안내", "저장하였습니다");
                                getScheduleList(date_id);

                                break;

                            case DELETE_SCHEDULE:

                                calendarDAO.deleteSchedule(item_id);
                                showAlertMsg("안내", "삭제하였습니다");
                                getScheduleList(date_id);
                                break;

                            case UPDATE_SCHEDULE:

                                calendarDAO.updateSchedule(item_hour, item_minute, item_content, item_id);
                                showAlertMsg("안내", "수정하였습니다");
                                getScheduleList(date_id);
                                break;

                        }

                    }
                }).setNegativeButton("취소",
                new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        // 'No'
                        return;
                    }
                }).show();

    }

    public void showAlertMsg(String title, String msg){

        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle(title).setMessage(msg).show();

    }

    public void startTimer(){

        backTimerHandler.sendEmptyMessageDelayed(MSG_TIMER_EXPIRED, BACKKEY_TIMEOUT * MILLIS_IN_SEC);
    }

    private TimePickerDialog.OnTimeSetListener myTimeSetListener
            = new TimePickerDialog.OnTimeSetListener(){

        public void onTimeSet(TimePicker view, int hourOfDay, int minute){

            my_hour = hourOfDay;
            my_minute = minute;

            setTimePickersTime(hourOfDay, minute);

        }

    };

    public Handler backTimerHandler = new Handler(){

        public void handleMessage(Message msg){

            switch(msg.what){
                case MSG_TIMER_EXPIRED:{
                    isBackKeyPressed = false;
                }
                break;
            }

        }
    };

}
