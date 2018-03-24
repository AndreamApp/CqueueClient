package com.andreamapp.cqu.table;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.andreamapp.cqu.R;
import com.andreamapp.cqu.bean.Table;

import java.util.List;
import java.util.Set;

/**
 * Created by Andream on 2018/3/24.
 * Email: andreamapp@qq.com
 * Website: http://andreamapp.com
 */

public class TableView extends RelativeLayout {

    private LinearLayout mWeekdayLayout;
    private LinearLayout mContentLayout;

    private Table mData;
    private List<Set<CourseIndex>> mCourseIndexes;

    public TableView(@NonNull Context context) {
        super(context);
    }

    public TableView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TableView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public TableView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        inflate(context, R.layout.table_view, this);
        mWeekdayLayout = findViewById(R.id.table_weekday_layout);
        mContentLayout = findViewById(R.id.table_content_layout);
    }

    private void loadTable(Table data) {
        mCourseIndexes = CourseIndex.generate(data);
        // TODO: set current week by calendar of school
        setCurrentWeek(1);
    }

    private void setCurrentWeek(int week) {
        if (week < 1 || week > 25) {
            return;
        }

        int lastWeek = -1;
        LinearLayout layout = null;
        int lastSection = 0;
        for (CourseIndex index : mCourseIndexes.get(week)) {
            if (index.weekday != lastWeek) {
                lastWeek = index.weekday;
                lastSection = 0;
                layout = (LinearLayout) mContentLayout.getChildAt(index.weekday);
                layout.removeAllViews();
            }

            // 有空课
            if (index.sectionStart - lastSection > 1) {
                for (int i = lastSection + 1; i < index.sectionStart; i++) {
                    CourseView empty = new CourseView(getContext());
                    empty.emptySection(1);
                    MarginLayoutParams lp = new MarginLayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
                    lp.bottomMargin = getResources().getDimensionPixelSize(R.dimen.table_item_margin);
                    layout.addView(empty, lp);
                }
            }

            lastSection = index.sectionEnd;

            CourseView view = new CourseView(getContext());
            view.setCourse(index);
            MarginLayoutParams lp = new MarginLayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.bottomMargin = getResources().getDimensionPixelSize(R.dimen.table_item_margin);
            layout.addView(view, lp);
        }
    }

    public Table getData() {
        return mData;
    }

    public void setData(Table data) {
        this.mData = data;
        loadTable(data);
    }

    class CourseView extends android.support.v7.widget.AppCompatTextView {

        CourseIndex index;

        public CourseView(Context context) {
            super(context);
            setTextSize(14);
            setTextColor(Color.WHITE);
            setGravity(Gravity.CENTER);
            setClickable(true);
            setFocusable(true);
        }

        public void emptySection(int sections) {
            setHeight(sections * getResources().getDimensionPixelSize(R.dimen.table_item_height));
        }

        public void setCourse(CourseIndex index) {
            this.index = index;
            int sections = index.sectionEnd - index.sectionStart + 1;
            setText(index.course.course_name + "\n" + index.classroom);
            setHeight(
                    sections * getResources().getDimensionPixelSize(R.dimen.table_item_height)
                            + (sections - 1) * getResources().getDimensionPixelSize(R.dimen.table_item_margin));
            setBackgroundResource(R.drawable.table_item_bg);
        }
    }
}
