package com.andreamapp.cqu.table;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andreamapp.cqu.R;
import com.andreamapp.cqu.about.AboutActivity;
import com.andreamapp.cqu.base.BaseModelActivity;
import com.andreamapp.cqu.exams.ExamsActivity;
import com.andreamapp.cqu.grade.GradeActivity;
import com.andreamapp.cqu.login.LoginActivity;
import com.andreamapp.cqu.utils.API;

import java.util.Calendar;
import java.util.List;
import java.util.Set;

/**
 * Created by Andream on 2018/3/24.
 * Email: andreamapp@qq.com
 * Website: http://andreamapp.com
 */

// TODO: Add week selector popup window
// TODO: Add semester selector
public class TableFragment extends BaseModelActivity<CourseIndexWrapper>
        implements TableView.OnCourseClickListener,
        Toolbar.OnMenuItemClickListener,
        ConfirmReloadTableDialogFragment.Listener,
        ProfileDialogFragment.Listener,
        ThemeSelectorDialogFragment.Listener {

    Toolbar mToolBar;                   // App top bar, show current week and menu
    ViewPager mViewPager;               // Table pager, every page show a week's courses
    WeekPagerAdapter mAdapter;          // Table pager adapter
    SwipeRefreshLayout mRefresh;        // refresher, only show it in programming, as it conflicts with pager

    TableViewModel mTableViewModel;     // Table model, provide the data source from network or cache
    int mThisWeekNum;                   // Record current week
    Calendar semesterStartDate;         // Record the startup date of semester, for calculating current week

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.table_fragment);

        mToolBar = findViewById(R.id.tool_bar);
        mRefresh = findViewById(R.id.refresh);
        mViewPager = findViewById(R.id.table_view_pager);

        // setup adapter
        mAdapter = new WeekPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);

        // setup model, and refresh
        mTableViewModel = ViewModelProviders.of(this).get(TableViewModel.class);
        mRefresh.setColorSchemeColors(getPrimiryColor(), getPrimiryColor(), getAccentColor());
        mRefresh.setEnabled(false);
        refresh(false);

        // setup tool bar and menu
        setSupportActionBar(mToolBar);
        mToolBar.setOnMenuItemClickListener(this);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                TextView tv = findViewById(R.id.table_week_btn);
//                tv.setText("第"+(position+1)+"周");
                mToolBar.setTitle("第" + (position + 1) + "周");
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void refresh(boolean fromNetwork) {
        if (mAdapter.getCount() == 0) {
            showState(R.string.state_loading);
        }
        mRefresh.setRefreshing(true);
        mTableViewModel.fetchIndexes(fromNetwork).observe(this, this);
    }

    @Override
    public void onChanged(@Nullable CourseIndexWrapper wrapper) {
        super.onChanged(wrapper);

        mRefresh.setRefreshing(false);
        if (wrapper != null && wrapper.status) {
            mAdapter.wrapper = wrapper;
            mAdapter.notifyDataSetChanged();
            Calendar c = Calendar.getInstance();
            // Todo: Fetch it from network
            c.set(2018, Calendar.MARCH, 5, 0, 0, 0);
            setSemesterStartDate(c);

            // show empty
            if (mAdapter.getCount() == 0) {
                showState(R.string.state_no_table);
            }
            // show content
            else {
                hideState();
            }
        } else {
            // show error
            if (mAdapter.getCount() == 0) {
                showState(R.string.state_no_table, v -> refresh(true));
            }
            // keep old data
            else {
                hideState();
            }
        }
    }

    @Override
    protected boolean showState(boolean show, int resId, View.OnClickListener clickHandler) {
        // content visibility as opposed to state's
        mViewPager.setVisibility(show ? View.GONE : View.VISIBLE);
        return super.showState(show, resId, clickHandler);
    }

    @Override
    public void onBackPressed() {
        if (mViewPager.getAdapter() != null && mViewPager.getAdapter().getCount() > 0
                && mThisWeekNum != mViewPager.getCurrentItem()) {
            mViewPager.setCurrentItem(mThisWeekNum);
        } else {
            // back to home, avoid reloading from network
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            superStartActivity(startMain);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    public Calendar getSemesterStartDate() {
        return semesterStartDate;
    }

    public void setSemesterStartDate(Calendar semesterStartDate) {
        this.semesterStartDate = semesterStartDate;
        long days = (Calendar.getInstance().getTimeInMillis() - semesterStartDate.getTimeInMillis()) / (24 * 60 * 60 * 1000);
        long weekNum = days / 7;
        mThisWeekNum = (int) weekNum;
        mViewPager.setCurrentItem(mThisWeekNum);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_exams:
                startActivity(new Intent(this, ExamsActivity.class));
                break;
            case R.id.action_grade:
                startActivity(new Intent(this, GradeActivity.class));
                break;
            case R.id.action_theme:
                ThemeSelectorDialogFragment.newInstance()
                        .show(getSupportFragmentManager(), "ThemeSelector");
                break;
            case R.id.action_refresh_table:
                ConfirmReloadTableDialogFragment.newInstance()
                        .show(getSupportFragmentManager(), "ConfirmReload");
                break;
            case R.id.action_about:
                startActivity(new Intent(this, AboutActivity.class));
                break;
            case R.id.action_logout:
                ProfileDialogFragment.newInstance(API.currentUser(this))
                        .show(getSupportFragmentManager(), "Profile");
                break;
        }
        return true;
    }

    @Override
    public void onCourseClicked(CourseIndex index) {
        CourseDetailDialogFragment.newInstance(index).show(getSupportFragmentManager(), "CourseDetail");
    }

    @Override
    public void onReloadTable() {
        refresh(true);
    }

    @Override
    public void onUserLogout() {
        mRefresh.setRefreshing(true);
        new LogoutTask(this).execute();
    }

    @Override
    public void onLogout(boolean res) {
        mRefresh.setRefreshing(false);
        if (res) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else {
            Snackbar.make(mToolBar, "登出失败", Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onThemeChanged(String theme) {
        // restart to perform theme change
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    class WeekPagerAdapter extends FragmentPagerAdapter {

        CourseIndexWrapper wrapper;

        WeekPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return TableWeekFragment.newInstance(wrapper.indexes, position + 1);
        }

        @Override
        public int getCount() {
            // wrapper.indexes.get(0) has no value
            return wrapper == null || wrapper.indexes == null ? 0 : wrapper.indexes.size() - 1;
        }
    }

    /**
     * The fragment of a single week page
     */
    public static class TableWeekFragment extends Fragment {
        List<Set<CourseIndex>> indexes;
        int week;

        TableView tableView;

        static TableWeekFragment newInstance(List<Set<CourseIndex>> indexes, int week) {
            TableWeekFragment f = new TableWeekFragment();

            f.indexes = indexes;
            f.week = week;
            return f;
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            tableView = new TableView(inflater.getContext(), null);
            tableView.setIndexes(indexes);
            tableView.setCurrentWeek(week);
            // show course detail
            tableView.setOnCourseClickListener((TableFragment) getActivity());
            return tableView;
        }
    }

    /**
     * show the detail of course information, as a bottom sheet dialog
     * including course's name, teacher's name, classroom, credit, hours in teaching
     * eg.
     * 毛泽东思想和中国特色社会主义理论体系概论
     * 张运青（任课教师）
     * D1331（上课地点）
     * 3.00（学分）
     * 48.0（总学时）
     */
    public static class CourseDetailDialogFragment extends BottomSheetDialogFragment {

        CourseIndex courseIndex;

        TextView courseName, teacherName, classroom, credit, hours;

        public static CourseDetailDialogFragment newInstance(CourseIndex index) {
            CourseDetailDialogFragment fragment = new CourseDetailDialogFragment();
            fragment.courseIndex = index;

            return fragment;
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.table_course_detail_sheet, container, false);
            courseName = v.findViewById(R.id.course_name);
            teacherName = v.findViewById(R.id.course_teacher_name);
            classroom = v.findViewById(R.id.course_classroom);
            credit = v.findViewById(R.id.course_credit);
            hours = v.findViewById(R.id.course_hours);

            setCourseIndex(courseIndex);

            return v;
        }

        public CourseIndex getCourseIndex() {
            return courseIndex;
        }

        public void setCourseIndex(CourseIndex courseIndex) {
            this.courseIndex = courseIndex;
            if (courseIndex != null && courseIndex.course != null) {
                courseName.setText(courseIndex.course.course_name);
                teacherName.setText(getString(R.string.course_detail_item_teacher, courseIndex.course.teacher));
                classroom.setText(getString(R.string.course_detail_item_classroom, courseIndex.classroom));
                credit.setText(getString(R.string.course_detail_item_credit, courseIndex.course.credit));
                hours.setText(getString(R.string.course_detail_item_hours_all, courseIndex.course.hours_all));
            }
        }
    }

}
