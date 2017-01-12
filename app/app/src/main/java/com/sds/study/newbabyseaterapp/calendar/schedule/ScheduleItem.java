package com.sds.study.newbabyseaterapp.calendar.schedule;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sds.study.newbabyseaterapp.R;

import static com.sds.study.newbabyseaterapp.calendar.CalendarActivity.SCHEDULE_NUM;

/**
 * Created by CANET on 2017-01-03.
 */

public class ScheduleItem extends LinearLayout{

    Schedule schedule;
    TextView item_txt_hour,item_txt_minute, item_txt_content;
    ImageButton btn_delete_schedule_item, btn_alarm_schedule_item;

    public ScheduleItem(Context context, Schedule schedule) {
        super(context);
        this.schedule=schedule;
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_schedule,this);

        item_txt_hour=(TextView) this.findViewById(R.id.schedule_item_txt_hour);
        item_txt_minute=(TextView) this.findViewById(R.id.schedule_item_txt_minute);
        item_txt_content=(TextView) this.findViewById(R.id.schedule_item_txt_content);
        btn_alarm_schedule_item=(ImageButton) this.findViewById(R.id.btn_alarm_schedule_item);
        btn_delete_schedule_item=(ImageButton) this.findViewById(R.id.btn_delete_schedule_item);
        btn_alarm_schedule_item.setFocusable(false);
        btn_delete_schedule_item.setFocusable(false);

        ScheduleTag scheduleTag = new ScheduleTag();
        scheduleTag.setView_num(SCHEDULE_NUM);
        scheduleTag.setSchedule(schedule);

        btn_delete_schedule_item.setTag(scheduleTag);
        btn_alarm_schedule_item.setTag(scheduleTag);
        this.setTag(scheduleTag);

        setSchedule(schedule);

    }

    public ScheduleItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setSchedule(Schedule schedule) {

        item_txt_hour.setText(String.valueOf(schedule.getHour()));
        item_txt_minute.setText(String.valueOf(schedule.getMinute()));
        item_txt_content.setText(String.valueOf(schedule.getContent()));

    }

    public Schedule getSchedule(){

        return schedule;

    }

}