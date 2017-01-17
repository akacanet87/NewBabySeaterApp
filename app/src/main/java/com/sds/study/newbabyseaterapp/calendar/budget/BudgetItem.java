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
    TextView budget_item_txt_time, budget_item_txt_card, budget_item_txt_place, budget_item_txt_money;
    ImageButton btn_delete_budget_item;

    public BudgetItem(Context context, Budget budget) {
        super(context);
        this.budget=budget;
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_budget,this);

        budget_item_txt_time=(TextView) this.findViewById(R.id.budget_item_txt_time);
        budget_item_txt_card=(TextView) this.findViewById(R.id.budget_item_txt_card);
        budget_item_txt_place=(TextView) this.findViewById(R.id.budget_item_txt_place);
        budget_item_txt_money=(TextView) this.findViewById(R.id.budget_item_txt_money);
        btn_delete_budget_item=(ImageButton) this.findViewById(R.id.btn_delete_budget_item);
        btn_delete_budget_item.setFocusable(false);

        BudgetTag budgetTag = new BudgetTag();
        budgetTag.setView_num(BUDGET_NUM);
        budgetTag.setBudget(budget);

        btn_delete_budget_item.setTag(budgetTag);

        setBudget(budget);

    }

    public BudgetItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setBudget(Budget budget) {

        budget_item_txt_time.setText(String.valueOf(budget.getTime()));
        budget_item_txt_card.setText(String.valueOf(budget.getBank_name()));
        budget_item_txt_place.setText(String.valueOf(budget.getPlace()));
        budget_item_txt_money.setText(String.valueOf(budget.getCost()));

    }

    public Budget getBudget(){

        return budget;

    }

}
