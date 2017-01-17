package com.sds.study.newbabyseaterapp.calendar.budget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sds.study.newbabyseaterapp.R;

import static com.sds.study.newbabyseaterapp.calendar.CalendarActivity.BUDGET_NUM;

/**
 * Created by CANET on 2017-01-03.
 */

public class BudgetTotalItem extends LinearLayout{

    Budget budget;
    TextView budget_total_txt_date, budget_total_txt_time, budget_total_txt_card, budget_total_txt_place, budget_total_txt_money;
    ImageButton btn_delete_budget_item;

    public BudgetTotalItem(Context context, Budget budget) {
        super(context);
        this.budget=budget;
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_total_budget,this);

        budget_total_txt_date=(TextView) this.findViewById(R.id.budget_total_txt_date);
        budget_total_txt_time=(TextView) this.findViewById(R.id.budget_total_txt_time);
        budget_total_txt_card=(TextView) this.findViewById(R.id.budget_total_txt_card);
        budget_total_txt_place=(TextView) this.findViewById(R.id.budget_total_txt_place);
        budget_total_txt_money=(TextView) this.findViewById(R.id.budget_total_txt_money);

        BudgetTag budgetTag = new BudgetTag();
        budgetTag.setView_num(BUDGET_NUM);
        budgetTag.setBudget(budget);

        setBudget(budget);

    }

    public BudgetTotalItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setBudget(Budget budget) {

        budget_total_txt_date.setText(String.valueOf(budget.getDate()));
        budget_total_txt_time.setText(String.valueOf(budget.getTime()));
        budget_total_txt_card.setText(String.valueOf(budget.getBank_name()));
        budget_total_txt_place.setText(String.valueOf(budget.getPlace()));
        budget_total_txt_money.setText(String.valueOf(budget.getCost()));

    }

    public Budget getBudget(){

        return budget;

    }

}
