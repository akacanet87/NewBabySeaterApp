package com.sds.study.newbabyseaterapp.calendar;

import android.Manifest;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
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
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.sds.study.newbabyseaterapp.BabySeaterSqlHelper;
import com.sds.study.newbabyseaterapp.R;
import com.sds.study.newbabyseaterapp.calendar.budget.Budget;
import com.sds.study.newbabyseaterapp.calendar.budget.BudgetItem;
import com.sds.study.newbabyseaterapp.calendar.budget.BudgetListAdapter;
import com.sds.study.newbabyseaterapp.calendar.budget.BudgetTag;
import com.sds.study.newbabyseaterapp.calendar.budget.BudgetTotalItem;
import com.sds.study.newbabyseaterapp.calendar.budget.BudgetTotalListAdapter;
import com.sds.study.newbabyseaterapp.calendar.budget.SmsFormat;
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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Calendar;

public class CalendarActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemClickListener{

    InputMethodManager imm;
    Uri imgUri;
    LinearLayout layout_nav_header;
    DrawerLayout drawer_layout;
    NavigationView nav_view;
    PopupWindow set_baby_pop;
    ImageButton btn_babysetting;
    ImageView nav_img_babyprofile;
    TextView nav_txt_babyname, nav_txt_babygender, nav_txt_babybirth, nav_txt_babystellation, nav_txt_babystone, nav_txt_babysince;
    EditText popup_txt_name, popup_txt_gender;
    DatePicker popup_datepicker;
    Button btn_popup_save, btn_popup_cancel;

    PopupWindow set_budget_pop;
    TextView popup_txt_budget, txt_total_budget, txt_total_spent;
    Button btn_popup_budget_save, btn_popup_budget_cancel;

    TextView calendar_txt_yearmonth, calendar_txt_thisdate;

    EditText diary_txt_title, diary_txt_content;

    TextView schedule_txt_time, schedule_txt_hour, schedule_txt_minute, schedule_txt_ampm;
    ImageButton schedule_btn_alarm, btn_alarm_schedule_item;
    EditText schedule_txt_content;
    LinearLayout layout_schedule_timepicker;

    ArrayAdapter<CharSequence> budgetMethodAdapter;
    ArrayAdapter<CharSequence> budgetBankAdapter;
    TextView budget_txt_date, budget_txt_time;
    Spinner budget_spinner_method, budget_spinner_card;
    EditText budget_txt_place, budget_txt_money, budget_txt_content;

    ListView daily_schedule_list, daily_diary_list, daily_budget_list, total_budget_list;

    Calendar calendar = Calendar.getInstance();
    CoordinatorLayout layoutContainer;
    View inc_layout_calendar, inc_layout_diarylist, inc_layout_diary, inc_layout_schedulelist, inc_layout_schedule, inc_layout_budgetlist, inc_layout_budget, inc_layout_total_budgetlist;

    CalendarFragment calendarFragment;
    BabySeaterSqlHelper sqlHelper;    //데이터 베이스 구축
    public static SQLiteDatabase db;  //데이터 베이스 쿼리문 제어
    Baby baby;
    CalendarDAO calendarDAO;
    SchoolDAO schoolDAO;
    Diary diaryDTO;
    Schedule scheduleDTO;
    Budget budgetDTO;
    DiaryListAdapter diaryListAdapter;
    ScheduleListAdapter scheduleListAdapter;
    BudgetListAdapter budgetListAdapter;
    BudgetTotalListAdapter budgetTotalListAdapter;

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
    public static final int INSERT_BUDGET = 2200;
    public static final int DELETE_BUDGET = 2201;
    public static final int UPDATE_BUDGET = 2202;
    public static final int ALARM_BTN = 5000;
    public static final int NEW_ITEM = 3000;
    public static final int EDIT_ITEM = 3001;
    public static final int SMS_PERMISSION = 7000;
    public static final int COARSE_LOC_PERMISSION = 7001;
    public static final int FINE_LOC_PERMISSION = 7002;
    public static final int OVERAY_PERMISSION = 7003;
    public static final int READ_EXST_PERMISSION = 7004;
    public static final int WRITE_EXST_PERMISSION = 7005;
    public static final int FROM_CAMERA = 8000;
    public static final int FROM_ALBUM = 8001;
    public static final int CROP_IMG = 8002;

    String date_name;
    int deviceWidth, deviceHeight;
    int today_year, today_month, today_date;
    int this_hour, this_minute, my_hour, my_minute;
    int date_id;
    int ym_id;
    long day_mills = 86400000;

    String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        TAG = this.getClass().getName() + "/Canet";

        initDB();

        setContentView(R.layout.activity_calendar);

        initDeviceSize();

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
        initImg();
        initBaby();

        Intent intent = getIntent();
        String sms = intent.getStringExtra("sms");
        String bankName = intent.getStringExtra("bankName");

        if(sms != null){

            int[] lines = getSmsObject(bankName);
            SmsFormat smsFormat = new SmsFormat();
            smsFormat.getLines(sms, lines);
            setLayoutVisible(inc_layout_calendar, inc_layout_budget);

            budget_txt_date.setText(date_name);
            budget_txt_time.setText(smsFormat.getTime());
            budget_txt_place.setText(smsFormat.getPlace());
            budget_txt_money.setText(Integer.toString(smsFormat.getCost()));
            budget_spinner_card.setSelection(setSpinnerSelection(smsFormat.getCard()));
            budget_spinner_method.setSelection(0);

            Log.d(TAG, "smsFormat.getCard() : " + smsFormat.getCard());
            Log.d(TAG, "smsFormat.getPlace() : " + smsFormat.getPlace());
            Log.d(TAG, "smsFormat.getTime() : " + smsFormat.getTime());
            Log.d(TAG, "smsFormat.getCost() : " + smsFormat.getCost());

        }

    }

    public void initCalendar(){

        TODAY_YEAR = calendar.get(Calendar.YEAR);
        TODAY_MONTH = calendar.get(Calendar.MONTH) + 1;
        TODAY_DATE = calendar.get(Calendar.DAY_OF_MONTH);
        date_name = TODAY_YEAR + "." + TODAY_MONTH + "." + TODAY_DATE;

        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        layoutContainer = (CoordinatorLayout) findViewById(R.id.layoutContainer);

        //  nav_header
        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        nav_view = (NavigationView) drawer_layout.findViewById(R.id.nav_view);
        layout_nav_header = (LinearLayout) nav_view.getHeaderView(0);
        btn_babysetting = (ImageButton) nav_view.getHeaderView(0).findViewById(R.id.btn_babysetting);
        nav_img_babyprofile = (ImageView) nav_view.getHeaderView(0).findViewById(R.id.nav_img_babyprofile);
        nav_txt_babyname = (TextView) nav_view.getHeaderView(0).findViewById(R.id.nav_txt_babyname);
        nav_txt_babygender = (TextView) nav_view.getHeaderView(0).findViewById(R.id.nav_txt_babygender);
        nav_txt_babybirth = (TextView) nav_view.getHeaderView(0).findViewById(R.id.nav_txt_babybirth);
        nav_txt_babystellation = (TextView) nav_view.getHeaderView(0).findViewById(R.id.nav_txt_babystellation);
        nav_txt_babystone = (TextView) nav_view.getHeaderView(0).findViewById(R.id.nav_txt_babystone);
        nav_txt_babysince = (TextView) nav_view.getHeaderView(0).findViewById(R.id.nav_txt_babysince);
        //Log.d(TAG, "btn_babysetting : "+btn_babysetting );
        //Log.d(TAG, "nav_txt_babygender : "+nav_txt_babygender );
        //layout_nav_header = (LinearLayout) nav_view.findViewById(R.id.layout_nav_header);

        //Log.d(TAG, "layout_nav_header : " + layout_nav_header);

        //  including된 layout들
        inc_layout_calendar = layoutContainer.findViewById(R.id.inc_layout_calendar);
        inc_layout_diarylist = layoutContainer.findViewById(R.id.inc_layout_diarylist);
        inc_layout_diary = layoutContainer.findViewById(R.id.inc_layout_diary);
        inc_layout_schedulelist = layoutContainer.findViewById(R.id.inc_layout_schedulelist);
        inc_layout_schedule = layoutContainer.findViewById(R.id.inc_layout_schedule);
        inc_layout_budgetlist = layoutContainer.findViewById(R.id.inc_layout_budgetlist);
        inc_layout_budget = layoutContainer.findViewById(R.id.inc_layout_budget);
        inc_layout_total_budgetlist = layoutContainer.findViewById(R.id.inc_layout_total_budgetlist);

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

        //  inc_layout_budget의 view들
        budget_txt_date = (TextView) inc_layout_budget.findViewById(R.id.budget_txt_date);
        budget_txt_time = (TextView) inc_layout_budget.findViewById(R.id.budget_txt_time);
        budget_txt_place = (EditText) inc_layout_budget.findViewById(R.id.budget_txt_place);
        budget_txt_money = (EditText) inc_layout_budget.findViewById(R.id.budget_txt_money);
        budget_txt_content = (EditText) inc_layout_budget.findViewById(R.id.budget_txt_content);
        budget_spinner_method = (Spinner) inc_layout_budget.findViewById(R.id.budget_spinner_method);
        budget_spinner_card = (Spinner) inc_layout_budget.findViewById(R.id.budget_spinner_card);

        budgetMethodAdapter = ArrayAdapter.createFromResource(this, R.array.paymentMethod, R.layout.support_simple_spinner_dropdown_item);
        budgetBankAdapter = ArrayAdapter.createFromResource(this, R.array.bankName, R.layout.support_simple_spinner_dropdown_item);
        budgetMethodAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        budgetBankAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        budget_spinner_method.setAdapter(budgetMethodAdapter);
        budget_spinner_card.setAdapter(budgetBankAdapter);

        //  inc_layout_budgetlist의 view들
        daily_budget_list = (ListView) inc_layout_budgetlist.findViewById(R.id.daily_budget_list);
        daily_budget_list.setOnItemClickListener(this);

        //  inc_layout_total_budgetlist의 view들
        total_budget_list = (ListView) inc_layout_total_budgetlist.findViewById(R.id.total_budget_list);
        txt_total_budget = (TextView) inc_layout_total_budgetlist.findViewById(R.id.txt_total_budget);
        txt_total_spent = (TextView) inc_layout_total_budgetlist.findViewById(R.id.txt_total_spent);
        total_budget_list.setOnItemClickListener(this);

        //Log.d(TAG, "OneMonthFragment 생성 전");

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
        budgetTotalListAdapter = new BudgetTotalListAdapter(this, db);

        /*if(schoolDAO.selectOne()==0){

            readExcelFile();

        }*/


        //Log.d(TAG, "initDB 완료");

    }

    public void initDeviceSize(){

        WindowManager w = getWindowManager();
        Display d = w.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        d.getMetrics(metrics);
        // since SDK_INT = 1;
        deviceWidth = metrics.widthPixels;
        deviceHeight = metrics.heightPixels;


        try{
            Point realSize = new Point();
            Display.class.getMethod("getRealSize", Point.class).invoke(d, realSize);
            deviceWidth = realSize.x;
            deviceHeight = realSize.y;
        }catch(IllegalAccessException e){
            e.printStackTrace();
        }catch(InvocationTargetException e){
            e.printStackTrace();
        }catch(NoSuchMethodException e){
            e.printStackTrace();
        }

    }

    public void initBaby(){

        if(calendarDAO.countBaby() != 0){

            /*ArrayList<Baby> babies = calendarDAO.selectAllBabies();

            for(int i=0 ; i<babies.size() ; i++){

                Baby baby = babies.get(i);

                int baby_id = baby.getBaby_id();
                String name = baby.getName();
                String gender = baby.getGender();

                Log.d(TAG, "baby_id : "+baby_id+", name : "+name+", gender : "+gender);

            }*/

            int babyCount = calendarDAO.countBaby();

            //Log.d(TAG, "babyCount : " + babyCount);

            Baby myBaby = calendarDAO.selectBaby(babyCount);

            String name = myBaby.getName();
            String gender = myBaby.getGender();
            int year = myBaby.getYear();
            int month = myBaby.getMonth();
            int date = myBaby.getDate();

            String birth = year + "." + month + "." + date;
            int d_day = calDDay(year, month, date);
            String stellation = getStellation(month, date);
            String stone = getBabyStone(month);

            myBaby.setBirth(birth);
            myBaby.setD_day(d_day);
            myBaby.setStellation(stellation);
            myBaby.setStone(stone);

            //Log.d(TAG, "이름 : "+name+"\n성별 : "+gender+"\n생년월일 : "+birth+"\n태어난지 "+d_day+"일 째\n탄생석 : "+stone+"\n별자리 : "+stellation);

            setNavView(myBaby);

        }

    }

    public void initImg(){

        if(calendarDAO.countImg() != 0){

            String img_path = calendarDAO.selectImg(calendarDAO.countImg());

            File file = new File(img_path);
            BufferedInputStream buffI = null;

            try{
                buffI = new BufferedInputStream(new FileInputStream(file));
                Bitmap bitmap = BitmapFactory.decodeStream(buffI);
                nav_img_babyprofile.setImageBitmap(bitmap);

            }catch(FileNotFoundException e){
                e.printStackTrace();
            }finally{
                if(buffI != null){
                    try{
                        buffI.close();
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                }
            }

        }

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

            case READ_EXST_PERMISSION:

                if(permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED){
                    showAlertMsg("내장 디렉토리 읽기 권한 안내", "권한을 부여하지 않으면 일부 기능을 사용 할 수 없습니다.");

                    return;
                }

            case WRITE_EXST_PERMISSION:

                if(permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED){
                    showAlertMsg("내장 디렉토리 쓰기 권한 안내", "권한을 부여하지 않으면 일부 기능을 사용 할 수 없습니다.");

                    return;
                }

                break;

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
        //Log.d(TAG, "date_id는 " + date_id);

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
                refreshScheduleContents();

            }else if(inc_layout_budgetlist.getVisibility() == View.VISIBLE){

                setLayoutVisible(inc_layout_budgetlist, inc_layout_calendar);
                refreshCalendar();

            }else if(inc_layout_budget.getVisibility() == View.VISIBLE){

                setLayoutVisible(inc_layout_budget, inc_layout_budgetlist);
                refreshBudgetContents();

            }else if(inc_layout_total_budgetlist.getVisibility() == View.VISIBLE){

                setLayoutVisible(inc_layout_total_budgetlist, inc_layout_calendar);
                refreshCalendar();

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
                setMonthlyBudget();
                break;
            case R.id.nav_btn_billslist:
                Log.d(TAG, "date_id는 " + date_id);
                setLayoutVisible(inc_layout_calendar, inc_layout_total_budgetlist);

                if(calendarDAO.countTotalBudgetList() == 0){

                    txt_total_budget.setText("예산을 설정해주세요.");

                }else{

                    txt_total_budget.setText(Integer.toString(calendarDAO.getLastTotalBudget(TODAY_MONTH)));

                }

                txt_total_spent.setText(Integer.toString(calendarDAO.getTotalSpent(TODAY_YEAR, TODAY_MONTH)));

                if((calendarDAO.getLastTotalBudget(TODAY_MONTH) - calendarDAO.getTotalSpent(TODAY_YEAR, TODAY_MONTH)) <= 0){

                    txt_total_spent.setBackground(getResources().getDrawable(R.drawable.outline_red));

                }else if((calendarDAO.getLastTotalBudget(TODAY_MONTH) - calendarDAO.getTotalSpent(TODAY_YEAR, TODAY_MONTH)) < 50000){

                    txt_total_spent.setBackground(getResources().getDrawable(R.drawable.outline_pink));

                }

                getAllBudgetList();

                break;

        }

        drawer_layout.closeDrawer(GravityCompat.START);
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

        }else if(adapterView == daily_budget_list){

            Log.d(TAG, l + "은 뭘까");
            Log.d(TAG, i + " 번째 스케줄 아이템 클릭");
            BudgetItem budgetItem = (BudgetItem) view;
            Budget budget = budgetItem.getBudget();
            setLayoutVisible(inc_layout_budgetlist, inc_layout_budget);
            inc_layout_budget.findViewById(R.id.budget_btn_save).setVisibility(View.GONE);
            inc_layout_budget.findViewById(R.id.budget_btn_edit).setVisibility(View.VISIBLE);

            int budget_id = budget.getBudget_id();
            int date_id = budget.getDate_id();
            String date = budget.getDate();
            String time = budget.getTime();
            String payment_method = budget.getPayment_method();
            String bank_name = budget.getBank_name();
            String place = budget.getPlace();
            int cost = budget.getCost();
            String content = budget.getContent();

            Log.d(TAG, "budget_id : " + budget_id);
            Log.d(TAG, "date : " + date);
            Log.d(TAG, "time : " + time);
            Log.d(TAG, "payment_method : " + payment_method);
            Log.d(TAG, "bank_name : " + bank_name);
            Log.d(TAG, "place : " + place);
            Log.d(TAG, "cost : " + cost);

            budget_txt_date.setText(date);
            budget_txt_time.setText(time);
            budget_txt_place.setText(place);
            budget_txt_money.setText(Integer.toString(cost));
            budget_txt_content.setText(content);
            budget_spinner_card.setSelection(setSpinnerSelection(bank_name));
            budget_spinner_method.setSelection(0);

            budgetDTO = new Budget();

            budgetDTO.setBudget_id(budget_id);
            budgetDTO.setDate_id(date_id);
            budgetDTO.setDate(date);
            budgetDTO.setTime(time);
            budgetDTO.setCost(cost);
            budgetDTO.setPlace(place);
            budgetDTO.setPayment_method(payment_method);
            budgetDTO.setBank_name(bank_name);
            budgetDTO.setContent(content);

        }else if(adapterView == total_budget_list){

            Log.d(TAG, l + "은 뭘까");
            Log.d(TAG, i + " 번째 스케줄 아이템 클릭");
            BudgetTotalItem budgetTotalItem = (BudgetTotalItem) view;
            Budget budget = budgetTotalItem.getBudget();
            setLayoutVisible(inc_layout_total_budgetlist, inc_layout_budget);
            inc_layout_budget.findViewById(R.id.budget_btn_save).setVisibility(View.GONE);
            inc_layout_budget.findViewById(R.id.budget_btn_edit).setVisibility(View.VISIBLE);

            int budget_id = budget.getBudget_id();
            int date_id = budget.getDate_id();
            String date = budget.getDate();
            String time = budget.getTime();
            String payment_method = budget.getPayment_method();
            String bank_name = budget.getBank_name();
            String place = budget.getPlace();
            int cost = budget.getCost();
            String content = budget.getContent();

            Log.d(TAG, "budget_id : " + budget_id);
            Log.d(TAG, "date : " + date);
            Log.d(TAG, "time : " + time);
            Log.d(TAG, "payment_method : " + payment_method);
            Log.d(TAG, "bank_name : " + bank_name);
            Log.d(TAG, "place : " + place);
            Log.d(TAG, "cost : " + cost);

            budget_txt_date.setText(date);
            budget_txt_time.setText(time);
            budget_txt_place.setText(place);
            budget_txt_money.setText(Integer.toString(cost));
            budget_txt_content.setText(content);
            budget_spinner_card.setSelection(setSpinnerSelection(bank_name));
            budget_spinner_method.setSelection(0);

            budgetDTO = new Budget();

            budgetDTO.setBudget_id(budget_id);
            budgetDTO.setDate_id(date_id);
            budgetDTO.setDate(date);
            budgetDTO.setTime(time);
            budgetDTO.setCost(cost);
            budgetDTO.setPlace(place);
            budgetDTO.setPayment_method(payment_method);
            budgetDTO.setBank_name(bank_name);
            budgetDTO.setContent(content);

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        if(resultCode != RESULT_OK){

            return;

        }

        switch(requestCode){

            case FROM_ALBUM:

                imgUri = data.getData();
                Log.d(TAG, "imgUri : " + imgUri);

            case FROM_CAMERA:

                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(imgUri, "image/*");

                intent.putExtra("outputX", 180);
                intent.putExtra("outputY", 240);
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("scale", true);
                intent.putExtra("return-data", true);
                startActivityForResult(intent, CROP_IMG);

                break;

            case CROP_IMG:

                Bundle bundle = data.getExtras();
                String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/BabySeater/" + System.currentTimeMillis() + ".jpg";

                if(bundle != null){

                    Bitmap bitmap = bundle.getParcelable("data");
                    nav_img_babyprofile.setImageBitmap(bitmap);

                    storeCropImage(bitmap, filePath);

                }

                break;

        }

    }

    public void btnCalendarClick(View view){

        switch(view.getId()){

            case R.id.btn_babysetting:

                Log.d(TAG, "btn_babysetting 눌림");
                setBabyInfo();

                break;

            case R.id.nav_img_babyprofile:

                Log.d(TAG, "nav_img_babyprofile 눌림");
                checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, READ_EXST_PERMISSION);
                checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, WRITE_EXST_PERMISSION);
                setBabyImgDialog();

                break;

            case R.id.btn_popup_save:

                Log.d(TAG, "btn_popup_save 눌림");
                saveBabyDB();
                set_baby_pop.dismiss();
                setNavView(baby);

                break;

            case R.id.btn_popup_cancel:

                Log.d(TAG, "btn_popup_cancel 눌림");
                set_baby_pop.dismiss();

                break;

            case R.id.btn_popup_budget_save:

                saveBudget();
                set_budget_pop.dismiss();

                break;

            case R.id.btn_popup_budget_cancel:

                set_budget_pop.dismiss();

                break;

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
                getDiaryList(date_id);

                break;

            case R.id.diary_btn_edit:

                updateDiaryContents();
                refreshDiaryContents();
                imm.hideSoftInputFromWindow(inc_layout_diary.findViewById(R.id.diary_txt_title).getWindowToken(), 0);
                inc_layout_diary.findViewById(R.id.diary_btn_save).setVisibility(View.VISIBLE);
                inc_layout_diary.findViewById(R.id.diary_btn_edit).setVisibility(View.GONE);
                setLayoutVisible(inc_layout_diary, inc_layout_diarylist);
                getDiaryList(date_id);

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
                int schedule_date_id = item_schedule.getDate_id();
                String content = item_schedule.getContent();
                boolean isAlarmOff = item_schedule.isAlarmOff();
                Log.d(TAG, "view_num은 " + scheduleTag.getView_num());
                Log.d(TAG, "schedule_id는 " + schedule_id);
                Log.d(TAG, "hour는 " + hour);
                Log.d(TAG, "minute는 " + minute);
                Log.d(TAG, "date_id는 " + schedule_date_id);
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

            case R.id.btn_budget_list:

                Log.d(TAG, "date_id는 " + date_id);
                setLayoutVisible(inc_layout_calendar, inc_layout_budgetlist);
                getBudgetList(date_id);

                break;

            case R.id.btn_add_budget_item:

                setLayoutVisible(inc_layout_budgetlist, inc_layout_budget);
                imm.showSoftInput(inc_layout_budget.findViewById(R.id.budget_txt_content), 0);

                break;

            case R.id.budget_btn_save:

                insertBudgetContents();
                refreshBudgetContents();
                imm.hideSoftInputFromWindow(inc_layout_budget.findViewById(R.id.budget_txt_content).getWindowToken(), 0);
                setLayoutVisible(inc_layout_budget, inc_layout_budgetlist);
                getBudgetList(date_id);

                break;

            case R.id.budget_btn_edit:

                updateBudgetContents();
                refreshBudgetContents();
                imm.hideSoftInputFromWindow(inc_layout_budget.findViewById(R.id.budget_txt_content).getWindowToken(), 0);
                inc_layout_budget.findViewById(R.id.budget_btn_save).setVisibility(View.VISIBLE);
                inc_layout_budget.findViewById(R.id.budget_btn_edit).setVisibility(View.GONE);
                setLayoutVisible(inc_layout_budget, inc_layout_budgetlist);
                getBudgetList(date_id);

                break;

            case R.id.budget_btn_cancel:

                refreshBudgetContents();
                imm.hideSoftInputFromWindow(inc_layout_budget.findViewById(R.id.budget_txt_content).getWindowToken(), 0);
                if(inc_layout_budget.findViewById(R.id.budget_btn_save).getVisibility() == View.GONE){

                    inc_layout_budget.findViewById(R.id.budget_btn_save).setVisibility(View.VISIBLE);
                    inc_layout_budget.findViewById(R.id.budget_btn_edit).setVisibility(View.GONE);

                }
                setLayoutVisible(inc_layout_budget, inc_layout_budgetlist);
                getBudgetList(date_id);

                break;

            case R.id.btn_delete_budget_item:

                Log.d(TAG, "가계부 삭제 버튼 클릭");
                BudgetTag budgetTag = (BudgetTag) view.getTag();
                Budget item_budget = budgetTag.getBudget();

                Log.d(TAG, "key_num은 " + item_budget.getBudget_id());
                Log.d(TAG, "Date_id는 " + item_budget.getDate_id());
                Log.d(TAG, "Bank_name은 " + item_budget.getBank_name());
                Log.d(TAG, "Content는 " + item_budget.getContent());

                showConfirmMsg("가계부 삭제", "정말 삭제하시겠습니까?", DELETE_BUDGET, item_budget);

                break;

        }

    }

    public void setBabyImgDialog(){

        DialogInterface.OnClickListener camera = new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialogInterface, int i){

                takePhoto();

            }

        };

        DialogInterface.OnClickListener album = new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialogInterface, int i){

                takeAlbum();

            }

        };

        DialogInterface.OnClickListener cancel = new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialogInterface, int i){

                dialogInterface.dismiss();

            }

        };

        new AlertDialog.Builder(this).setTitle("업로드할 이미지 선택 방법").setPositiveButton("사진촬영", camera).setNeutralButton("취소", cancel).setNegativeButton("앨범선택", album).show();

    }

    public void takePhoto(){

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        String url = "bs_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
        imgUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), url));

        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
        startActivityForResult(cameraIntent, FROM_CAMERA);

    }

    public void takeAlbum(){

        Intent albumIntent = new Intent(Intent.ACTION_PICK);
        albumIntent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(albumIntent, FROM_ALBUM);

    }

    public void storeCropImage(Bitmap bitmap, String path){

        //String dirPath = Environment.getDownloadCacheDirectory().getAbsolutePath()+"/BabySeater";
        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/BabySeater";
        File dirBabySeater = new File(dirPath);
        if(!dirBabySeater.exists()){

            dirBabySeater.mkdir();

        }

        calendarDAO.insertImage(path);

        Log.d(TAG, "path : " + path);

        File copyFile = new File(path);
        BufferedOutputStream buffO = null;

        try{
            copyFile.createNewFile();
            buffO = new BufferedOutputStream(new FileOutputStream(copyFile));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, buffO);

            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(copyFile)));
            buffO.flush();
            buffO.close();
        }catch(IOException e){
            e.printStackTrace();
        }

    }

    public void setBabyInfo(){

        try{
            //  LayoutInflater 객체와 시킴
            LayoutInflater inflater = (LayoutInflater) this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View layout = inflater.inflate(R.layout.layout_babysetting_popup,
                    (ViewGroup) findViewById(R.id.popup_babysetting));

            set_budget_pop = new PopupWindow(layout, deviceWidth, deviceHeight, true);
            set_budget_pop.showAtLocation(layout, Gravity.CENTER, 0, 0);
            set_budget_pop.setOutsideTouchable(false);

            popup_txt_name = (EditText) layout.findViewById(R.id.popup_txt_name);
            popup_txt_gender = (EditText) layout.findViewById(R.id.popup_txt_gender);
            popup_datepicker = (DatePicker) layout.findViewById(R.id.popup_datepicker);
            btn_popup_save = (Button) layout.findViewById(R.id.btn_popup_save);
            btn_popup_cancel = (Button) layout.findViewById(R.id.btn_popup_cancel);

        }catch(Exception e){
            e.printStackTrace();
        }

    }

    public void saveBabyDB(){

        String name = popup_txt_name.getText().toString();
        String gender = popup_txt_gender.getText().toString();
        int year = popup_datepicker.getYear();
        int month = popup_datepicker.getMonth() + 1;
        int date = popup_datepicker.getDayOfMonth();

        String birth = year + "." + month + "." + date;
        int d_day = calDDay(year, month, date);
        String stellation = getStellation(month, date);
        String stone = getBabyStone(month);

        //Log.d(TAG, "이름 : "+name+"\n성별 : "+gender+"\n생년월일 : "+year+"년 "+month+"월 "+date+"일"+"\n태어난지 "+d_day+"일 째");

        baby = new Baby();
        baby.setName(name);
        baby.setGender(gender);
        baby.setYear(year);
        baby.setMonth(month);
        baby.setDate(date);
        baby.setBirth(birth);
        baby.setD_day(d_day);
        baby.setStellation(stellation);
        baby.setStone(stone);

        calendarDAO.insertBaby(name, gender, year, month, date);

        Log.d(TAG, "아이 저장 완료!");

    }

    public void setNavView(Baby baby){

        nav_txt_babyname.setText(baby.getName());
        nav_txt_babygender.setText(baby.getGender());
        nav_txt_babybirth.setText(baby.getBirth());
        nav_txt_babysince.setText(Integer.toString(baby.getD_day()));
        nav_txt_babystellation.setText(baby.getStellation());
        nav_txt_babystone.setText(baby.getStone());

    }

    public void setMonthlyBudget(){

        try{
            //  LayoutInflater 객체와 시킴
            LayoutInflater inflater = (LayoutInflater) this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View layout = inflater.inflate(R.layout.layout_budgetsetting_popup,
                    (ViewGroup) findViewById(R.id.popup_budgetsetting));

            set_budget_pop = new PopupWindow(layout, deviceWidth, deviceHeight, true);
            set_budget_pop.showAtLocation(layout, Gravity.CENTER, 0, 0);
            set_budget_pop.setOutsideTouchable(false);

            popup_txt_budget = (EditText) layout.findViewById(R.id.popup_txt_budget);
            btn_popup_budget_save = (Button) layout.findViewById(R.id.btn_popup_budget_save);
            btn_popup_budget_cancel = (Button) layout.findViewById(R.id.btn_popup_budget_cancel);

            if(calendarDAO.countTotalBudgetList() == 0){

                popup_txt_budget.setText(null);

            }else{

                popup_txt_budget.setText(Integer.toString(calendarDAO.getLastTotalBudget(TODAY_MONTH)));

            }

        }catch(Exception e){
            e.printStackTrace();
        }

    }

    public void saveBudget(){

        int budget = Integer.parseInt(popup_txt_budget.getText().toString());

        calendarDAO.insertTotalBudget(TODAY_YEAR, TODAY_MONTH, budget);

        popup_txt_budget.setText(null);

        Log.d(TAG, "예산 저장 완료!");

    }

    public int calDDay(int year, int month, int date){

        Calendar d_cal = Calendar.getInstance();
        Calendar today_cal = Calendar.getInstance();
        today_cal.set(TODAY_YEAR, TODAY_MONTH, TODAY_DATE);
        d_cal.set(year, month, date);

        long d_day = d_cal.getTimeInMillis() / day_mills;
        long today = today_cal.getTimeInMillis() / day_mills;

        long count = today - d_day;
        int final_day = (int) count + 1;

        return final_day;

    }

    public String getStellation(int month, int date){

        String stellation = null;

        switch(month){

            case 1:
                if(date >= 20){
                    stellation = "물병";
                }else{
                    stellation = "염소";
                }
                break;
            case 2:
                if(date >= 19){
                    stellation = "물고기";
                }else{
                    stellation = "물병";
                }
                break;
            case 3:
                if(date >= 21){
                    stellation = "양";
                }else{
                    stellation = "물고기";
                }
                break;
            case 4:
                if(date >= 20){
                    stellation = "황소";
                }else{
                    stellation = "양";
                }
                break;
            case 5:
                if(date >= 21){
                    stellation = "쌍둥이";
                }else{
                    stellation = "황소";
                }
                break;
            case 6:
                if(date >= 22){
                    stellation = "게";
                }else{
                    stellation = "쌍둥이";
                }
                break;
            case 7:
                if(date >= 23){
                    stellation = "사자";
                }else{
                    stellation = "게";
                }
                break;
            case 8:
                if(date >= 23){
                    stellation = "처녀";
                }else{
                    stellation = "사자";
                }
                break;
            case 9:
                if(date >= 24){
                    stellation = "천칭";
                }else{
                    stellation = "처녀";
                }
                break;
            case 10:
                if(date >= 24){
                    stellation = "전갈";
                }else{
                    stellation = "천칭";
                }
                break;
            case 11:
                if(date >= 23){
                    stellation = "사수";
                }else{
                    stellation = "전갈";
                }
                break;
            case 12:
                if(date >= 25){
                    stellation = "염소";
                }else{
                    stellation = "사수";
                }
                break;

        }

        return stellation + "자리";

    }

    public String getBabyStone(int month){

        String stone = null;

        switch(month){

            case 1:
                stone = "가넷(진실,우정)";
                break;
            case 2:
                stone = "자수정(평화,성실)";
                break;
            case 3:
                stone = "아쿠아마린(총명)";
                break;
            case 4:
                stone = "다이아몬드(고귀)";
                break;
            case 5:
                stone = "에메랄드(행복)";
                break;
            case 6:
                stone = "진주(건강,부귀)";
                break;
            case 7:
                stone = "루비(용기,정의)";
                break;
            case 8:
                stone = "페리도트(화합)";
                break;
            case 9:
                stone = "사파이어(진리)";
                break;
            case 10:
                stone = "오팔(희망,순결)";
                break;
            case 11:
                stone = "토파즈(우정)";
                break;
            case 12:
                stone = "터키석(성공,승리)";
                break;

        }

        return stone;

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

    public void getBudgetList(int date_id){

        daily_budget_list.setAdapter(budgetListAdapter);
        budgetListAdapter.getDailyBudgetList(date_id);
        budgetListAdapter.notifyDataSetChanged();
        budgetListAdapter.notifyDataSetInvalidated();

    }

    public void getAllBudgetList(){

        total_budget_list.setAdapter(budgetTotalListAdapter);
        budgetTotalListAdapter.getAllBudgetList();
        budgetTotalListAdapter.notifyDataSetChanged();
        budgetTotalListAdapter.notifyDataSetInvalidated();

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

    public void insertBudgetContents(){

        budgetDTO = new Budget();

        budgetDTO.setDate_id(date_id);
        budgetDTO.setYear(TODAY_YEAR);
        budgetDTO.setMonth(TODAY_MONTH);
        budgetDTO.setPayment_method(budget_spinner_method.getSelectedItem().toString());
        budgetDTO.setBank_name(budget_spinner_card.getSelectedItem().toString());
        budgetDTO.setCost(Integer.parseInt(budget_txt_money.getText().toString()));
        budgetDTO.setPlace(budget_txt_place.getText().toString());
        budgetDTO.setContent(budget_txt_content.getText().toString());
        budgetDTO.setDate(date_name);
        budgetDTO.setTime(budget_txt_time.getText().toString());

        showConfirmMsg("가계부 등록", "새로운 가계부를 저장하시겠습니까?", INSERT_BUDGET, budgetDTO);

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

    public void updateBudgetContents(){

        budgetDTO.setPayment_method(budget_spinner_method.getSelectedItem().toString());
        budgetDTO.setBank_name(budget_spinner_card.getSelectedItem().toString());
        budgetDTO.setCost(Integer.parseInt(budget_txt_money.getText().toString()));
        budgetDTO.setPlace(budget_txt_place.getText().toString());
        budgetDTO.setContent(budget_txt_content.getText().toString());

        Log.d(TAG, "수정된 결제방법 : " + budget_spinner_method.getSelectedItem().toString());
        Log.d(TAG, "수정된 카드 : " + budget_spinner_card.getSelectedItem().toString());
        Log.d(TAG, "수정된 결제금액 : " + Integer.parseInt(budget_txt_money.getText().toString()));
        Log.d(TAG, "수정된 결제장소 : " + budget_txt_place.getText().toString());
        Log.d(TAG, "수정된 메모내용 : " + budget_txt_content.getText().toString());

        showConfirmMsg("가계부 수정", "수정하시겠습니까?", UPDATE_BUDGET, budgetDTO);

    }

    public void refreshDiaryContents(){

        diary_txt_title.setText("");
        diary_txt_content.setText("");

    }

    public void refreshScheduleContents(){

        schedule_txt_content.setText("");
        //setTimePickersTime(this_hour, this_minute);

    }

    public void refreshBudgetContents(){

        budget_txt_content.setText("");
        budget_txt_date.setText("");
        budget_txt_time.setText("");
        budget_txt_place.setText("");
        budget_txt_money.setText("");

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

        calendarFragment.horizontalPagerAdapter.notifyDataSetChanged();

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

    public void showConfirmMsg(String title, String msg, final int site_id, Budget budget){

        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        final int item_id = budget.getBudget_id();
        final int item_year = budget.getYear();
        final int item_month = budget.getMonth();
        final String item_date = budget.getDate();
        final String item_time = budget.getTime();
        final String item_place = budget.getPlace();
        final int item_cost = budget.getCost();
        final String item_method = budget.getPayment_method();
        final String item_bank = budget.getBank_name();
        final String item_content = budget.getContent();

        alert.setTitle(title).setMessage(msg).setCancelable(false).setPositiveButton("확인",
                new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        // 'YES'

                        switch(site_id){

                            case INSERT_BUDGET:

                                calendarDAO.insertBudget(date_id, TODAY_YEAR, TODAY_MONTH, item_date, item_time, item_place, item_cost, item_method, item_bank, item_content);
                                showAlertMsg("안내", "저장하였습니다");
                                getBudgetList(date_id);

                                break;

                            case DELETE_BUDGET:

                                calendarDAO.deleteBudget(item_id);
                                showAlertMsg("안내", "삭제하였습니다");
                                getBudgetList(date_id);
                                break;

                            case UPDATE_BUDGET:

                                calendarDAO.updateBudget(date_id, item_year, item_month, item_date, item_time, item_place, item_cost, item_method, item_bank, item_content, item_id);
                                showAlertMsg("안내", "수정하였습니다");
                                getBudgetList(date_id);
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

    public int[] getSmsObject(String card){

        int[] lines = new int[4];

        //  lines[0] = int dateTimeLine, lines[1] = int cardLine, lines[2] = int placeLine, lines[3] = int costLine

        switch(card){

            case "국민":

                lines[0] = 3;
                lines[1] = 1;
                lines[2] = 5;
                lines[3] = 4;

                break;

            case "기업":

                lines[0] = 4;
                lines[1] = 3;
                lines[2] = 6;
                lines[3] = 2;

                break;

            case "농협":

                break;

            case "우리":

                break;

            case "신한":

                break;

            case "외환":

                break;

            case "하나":

                break;

            case "시티":

                break;

        }

        return lines;

    }

    public int setSpinnerSelection(String card){

        int cardIndex = 0;

        if(card.contains("국민")){
            cardIndex = 0;
        }else if(card.contains("기업")){
            cardIndex = 1;
        }else if(card.contains("농협")){
            cardIndex = 2;
        }else if(card.contains("우리")){
            cardIndex = 3;
        }else if(card.contains("신한")){
            cardIndex = 4;
        }else if(card.contains("외환")){
            cardIndex = 5;
        }else if(card.contains("하나")){
            cardIndex = 6;
        }else if(card.contains("시티")){
            cardIndex = 7;
        }

        return cardIndex;

    }

}
