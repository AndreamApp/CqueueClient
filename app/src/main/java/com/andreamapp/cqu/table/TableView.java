package com.andreamapp.cqu.table;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.andreamapp.cqu.R;

import java.util.Date;
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

    private List<Set<CourseIndex>> mCourseIndexes;
    Date semesterStartDate;

    private OnCourseClickListener listener;

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

    public OnCourseClickListener getOnCourseClickListener() {
        return listener;
    }

    public void setOnCourseClickListener(OnCourseClickListener listener) {
        this.listener = listener;
    }

    public List<Set<CourseIndex>> getIndexes() {
        return mCourseIndexes;
    }

    public void setIndexes(List<Set<CourseIndex>> indexes) {
        this.mCourseIndexes = indexes;
    }

    public Date getSemesterStartDate() {
        return semesterStartDate;
    }

    public void setSemesterStartDate(Date semesterStartDate) {
        this.semesterStartDate = semesterStartDate;
    }

    public void setCurrentWeek(int week) {
        if (week < 1 || week > CourseIndex.MAX_WEEK_NUM) {
            return;
        }

        int lastWeek = -1;
        LinearLayout layout = null;
        int lastSection = 0;
        for (final CourseIndex index : mCourseIndexes.get(week)) {
            if (index.weekday != lastWeek) {
                lastWeek = index.weekday;
                lastSection = 0;
                layout = (LinearLayout) mContentLayout.getChildAt(index.weekday);
                layout.removeAllViews();
            }

            // fill empty gap with empty view
            if (index.sectionStart - lastSection > 1) {
                for (int i = lastSection + 1; i < index.sectionStart; ) {
                    int sections = Math.min(index.sectionStart - i, 2);
                    // fill every two sections
                    if (sections > 0) {
                        final CourseView empty = new CourseView(getContext());
                        empty.emptySection(sections);
                        /* don't enable this function now
                        empty.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                TodoDialog.newInstance(empty)
                                        .show(((AppCompatActivity)getContext()).getSupportFragmentManager(), "todo");
                            }
                        });
                        */
                        MarginLayoutParams lp = new MarginLayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT);
                        lp.bottomMargin = getResources().getDimensionPixelSize(R.dimen.table_item_margin);
                        layout.addView(empty, lp);
                        i += sections;
                    }
                }
            }

            lastSection = index.sectionEnd;

            // lastSection < 0 means a separator index
            if(lastSection > 0){
                // create view, add to layout
                CourseView view = new CourseView(getContext());
                view.setCourse(index);
                MarginLayoutParams lp = new MarginLayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.bottomMargin = getResources().getDimensionPixelSize(R.dimen.table_item_margin);
                layout.addView(view, lp);

                // bind click handler
                view.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            listener.onCourseClicked(index);
                        }
                    }
                });
            }
        }
    }

    class CourseView extends android.support.v7.widget.AppCompatTextView {

        CourseIndex index;

        public CourseView(Context context) {
            super(context);
            setTextSize(14);
            setTextColor(Color.WHITE);
            setEllipsize(TextUtils.TruncateAt.END);
            setClickable(true);
            setFocusable(true);
        }

        public void emptySection(int sections) {
            setGravity(Gravity.TOP | Gravity.LEFT);
            int pd = getResources().getDimensionPixelSize(R.dimen.table_item_margin);
            setPadding(0, pd, 0, pd);
            setHeight(sections * getResources().getDimensionPixelSize(R.dimen.table_item_height)
                    + (sections - 1) * getResources().getDimensionPixelSize(R.dimen.table_item_margin));
            setBackgroundResource(R.drawable.table_item_empty_bg);
        }

        public void setCourse(CourseIndex index) {
            this.index = index;
            int sections = index.sectionEnd - index.sectionStart + 1;
            setGravity(Gravity.CENTER);
            setText(index.classroom + "\n\n" + index.course.course_name);
            setHeight(
                    sections * getResources().getDimensionPixelSize(R.dimen.table_item_height)
                            + (sections - 1) * getResources().getDimensionPixelSize(R.dimen.table_item_margin));
            setBackgroundResource(R.drawable.table_item_bg);
        }
    }

    public interface OnCourseClickListener {
        void onCourseClicked(CourseIndex index);
    }


    public static class TodoDialog extends AppCompatDialogFragment {

        public static TodoDialog newInstance(CourseView view) {
            TodoDialog td = new TodoDialog();
            td.view = view;
            return td;
        }

        CourseView view;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            View v = getActivity().getLayoutInflater()
                    .inflate(R.layout.about_feedback_dialog, null);
            EditText text = v.findViewById(R.id.feedback_edit_text);
            text.setText(view.getText().toString());
            text.setSelection(text.getText().length());
            AlertDialog dialog = new AlertDialog.Builder(getActivity(), getTheme())
                    .setTitle("TODO")
                    .setView(v)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @SuppressLint("StaticFieldLeak")
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String message = text.getText().toString();
                            view.setText(message);
                        }
                    })
                    .setNegativeButton("取消", null)
                    .create();
            if (dialog.getWindow() != null) {
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            }
            return dialog;
        }
    }
}
