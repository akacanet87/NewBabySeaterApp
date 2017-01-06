/*
* Copyright (C) 2015 Hansoo Lab.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.sds.study.newbabyseaterapp.calendar.calendar;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sds.study.newbabyseaterapp.R;

import java.util.Calendar;

import fr.castorflex.android.verticalviewpager.VerticalViewPager;

//  한 달을 보여주는 Fragment
public class OneMonthFragment extends Fragment implements ViewPager.OnPageChangeListener{
    
    public static final String ARG_YEAR = "year";
    public static final String ARG_MONTH = "month";
   
    VerticalViewPager verticalViewPager;      //  Vertical View Pager, ref: https://github.com/LittlePanpc/VerticalViewPager-1
    OneMonthPagerAdapter verticalPagerAdapter;
    public ViewPager horizontalViewPager;
    public OneMonthPagerAdapter horizontalPagerAdapter;
    int now_year = -1;
    int now_month = -1;

    /** previous position */
    int previousPosition;

    OnMonthChangeListener dummyListener = new OnMonthChangeListener() {
        @Override
        public void onChange(int year, int month) {}

        @Override
        public void onDayClick(DailyLayout dayView) {}

        @Override
        public void onDayClick(OneDayLayout dayView) {}

    };

    OnMonthChangeListener listener = dummyListener;

    OneMonthLayout.OnClickDayListener onClickDayListener = new OneMonthLayout.OnClickDayListener() {
        @Override
        public void onClick(OneDayLayout oneDayLayout) {
            listener.onDayClick(oneDayLayout);
        }
    };

    public static OneMonthFragment newInstance(int year, int month) {

        OneMonthFragment fragment = new OneMonthFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_YEAR, year);
        bundle.putInt(ARG_MONTH, month);
        fragment.setArguments(bundle);
        return fragment;
    }
    public OneMonthFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            now_year = getArguments().getInt(ARG_YEAR);
            now_month = getArguments().getInt(ARG_MONTH);
        }
        else {
            Calendar calendar = Calendar.getInstance();
            now_year = calendar.get(Calendar.YEAR);
            now_month = calendar.get(Calendar.MONTH);
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        verticalPagerAdapter = new OneMonthPagerAdapter(this, now_year, now_month);
        horizontalPagerAdapter = new OneMonthPagerAdapter(this, now_year, now_month);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_onemonth, container, false);

        horizontalViewPager = (ViewPager) view.findViewById(R.id.horizontal_viewpager);
        horizontalViewPager.addOnPageChangeListener(this);
        horizontalViewPager.setAdapter(horizontalPagerAdapter);
        horizontalViewPager.setCurrentItem(horizontalPagerAdapter.getPosition(now_year, now_month));
        horizontalViewPager.setOffscreenPageLimit(1);

        /*verticalViewPager = (VerticalViewPager) view.findViewById(R.id.vertical_viewpager);
        verticalViewPager.setAdapter(verticalPagerAdapter);
        verticalViewPager.setOnPageChangeListener(this);
        verticalViewPager.setCurrentItem(verticalPagerAdapter.getPosition(now_year, now_month));
        verticalViewPager.setOffscreenPageLimit(1);*/
        
        return view;
    }
    
    @Override
    public void onDetach() {
        setOnMonthChangeListener(null);
        super.onDetach();
    }

    public void setOnMonthChangeListener(OnMonthChangeListener listener) {
        if(listener == null) this.listener = dummyListener;
        else this.listener = listener;
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        switch(state) {
            case ViewPager.SCROLL_STATE_IDLE:
                //HLog.d(TAG, CLASS, "SCROLL_STATE_IDLE");
                break;
            case ViewPager.SCROLL_STATE_DRAGGING:
                //HLog.d(TAG, CLASS, "SCROLL_STATE_DRAGGING");
                previousPosition = horizontalViewPager.getCurrentItem();
                //previousPosition = verticalViewPager.getCurrentItem();
                break;
            case ViewPager.SCROLL_STATE_SETTLING:
                //HLog.d(TAG, CLASS, "SCROLL_STATE_SETTLING");
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        //HLog.d(TAG, CLASS, position + "-  " + positionOffset);
        if(previousPosition != position) {
            previousPosition = position;

            /*YearMonth yearMonth = verticalPagerAdapter.getYearMonth(position);
            listener.onChange(yearMonth.year, yearMonth.month);*/
            YearMonth yearMonth = horizontalPagerAdapter.getYearMonth(position);
            listener.onChange(yearMonth.year, yearMonth.month);

        }
    }

    @Override
    public void onPageSelected(int position) {
    }
}
