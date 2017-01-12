package com.sds.study.newbabyseaterapp.calendar.budget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sds.study.newbabyseaterapp.R;
import com.sds.study.newbabyseaterapp.calendar.diary.Diary;
import com.sds.study.newbabyseaterapp.calendar.diary.DiaryTag;

import static com.sds.study.newbabyseaterapp.calendar.CalendarActivity.BUDGET_NUM;
import static com.sds.study.newbabyseaterapp.calendar.CalendarActivity.DIARY_NUM;

/**
 * Created by CANET on 2017-01-03.
 */

public class BudgetItem extends LinearLayout{

    Budget budget;
    TextView diary_item_txt_title, diary_item_txt_content;
    ImageButton btn_delete_diary_item;

    public BudgetItem(Context context, Budget budget) {
        super(context);
        this.budget=budget;
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_budget,this);

        /*diary_item_txt_title=(TextView) this.findViewById(R.id.diary_item_txt_title);
        diary_item_txt_content=(TextView) this.findViewById(R.id.diary_item_txt_content);
        btn_delete_diary_item=(ImageButton) this.findViewById(R.id.btn_delete_diary_item);
        btn_delete_diary_item.setFocusable(false);

        DiaryTag diaryTag = new DiaryTag();
        diaryTag.setView_num(BUDGET_NUM);
        diaryTag.setDiary(diary);

        btn_delete_diary_item.setTag(diaryTag);

        setDiary(diary);*/

    }

    public BudgetItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setBudget(Budget budget) {

        /*diary_item_txt_title.setText(String.valueOf(diary.getTitle()));
        diary_item_txt_content.setText(String.valueOf(diary.getContent()));*/

    }

    public Budget getBudget(){

        return budget;

    }

}
