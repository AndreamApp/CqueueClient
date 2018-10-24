package com.andreamapp.cqu.table;

import android.annotation.SuppressLint;
import android.app.ListFragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andreamapp.cqu.R;
import com.andreamapp.cqu.about.AboutActivity;
import com.andreamapp.cqu.about.CheckUpdateTask;
import com.andreamapp.cqu.base.BaseModelActivity;
import com.andreamapp.cqu.bean.Courses;
import com.andreamapp.cqu.exams.ExamsActivity;
import com.andreamapp.cqu.grade.GradeActivity;
import com.andreamapp.cqu.login.LoginActivity;
import com.andreamapp.cqu.utils.Cache;

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

        // initially show table page
        setVisiblePage(0);

        // setup adapter
        mAdapter = new WeekPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);

        // setup model, and refresh
        mTableViewModel = ViewModelProviders.of(this).get(TableViewModel.class);
        mRefresh.setColorSchemeColors(getPrimiryColor(), getPrimiryColor(), getAccentColor());
        mRefresh.setEnabled(false);

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

        // Check update as start
        new CheckUpdateTask(TableFragment.this, false).execute();
    }

    @Override
    protected void onStart() {
        super.onStart();
        refresh(false);
    }

    public void setVisiblePage(int page) {
        if(0 == page) {
            mViewPager.setVisibility(View.VISIBLE);
            findViewById(R.id.fragment).setVisibility(View.GONE);
        }
        else if(1 == page) {
            mViewPager.setVisibility(View.GONE);
            findViewById(R.id.fragment).setVisibility(View.VISIBLE);
        }
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
            Calendar startDate = wrapper.getSemesterStartDate();
            if(startDate != null) {
                setSemesterStartDate(startDate);
            }

            boolean isEmpty = mAdapter.getCount() == 0;
            mAdapter.wrapper = wrapper;
            mAdapter.notifyDataSetChanged();
            if (isEmpty) {
                switchToCurrWeek();
            }

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

    private void switchToCurrWeek() {
        mViewPager.setCurrentItem(mThisWeekNum);
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

        MenuItem myActionMenuItem = menu.findItem(R.id.action_search_courses);

        SearchView searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setQueryHint(getString(R.string.search_hint));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // TODO
//                Snackbar.make(searchView, "SearchOnQueryTextSubmit: " + query, Snackbar.LENGTH_SHORT).show();
                mRefresh.setRefreshing(true);
                TableRepository.searchCourse(query).observe(TableFragment.this, courses -> {
                    mRefresh.setRefreshing(false);
                    setVisiblePage(1);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment, CourseListFragment.newInstance(courses))
                            .commit();
                });
//                if (!searchView.isIconified()) {
//                    searchView.setIconified(true);
//                }
//                myActionMenuItem.collapseActionView();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                // UserFeedback.show( "SearchOnQueryTextChanged: " + s);
                return false;
            }
        });

        return true;
    }

    public Calendar getSemesterStartDate() {
        return semesterStartDate;
    }

    /**
     * 根据学期开始日期，计算当前周。当前周需要及时更新，避免用户被过时的当前周误导。
     * 目前每次启动Activity都会计算一次
     * TODO: 从服务器获取当前学期开始日期
     *
     * @param semesterStartDate 学期开始日期
     */
    public void setSemesterStartDate(Calendar semesterStartDate) {
        this.semesterStartDate = semesterStartDate;
        long days = (Calendar.getInstance().getTimeInMillis() - semesterStartDate.getTimeInMillis()) / (24 * 60 * 60 * 1000);
        long weekNum = days / 7;
        mThisWeekNum = (int) weekNum;
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
                ProfileDialogFragment.newInstance(Cache.currentUser())
                        .show(getSupportFragmentManager(), "Profile");
                break;
        }
        return true;
    }

    static final long COURSE_CLICK_THRESH = 100;
    long course_last_click;
    @Override
    public void onCourseClicked(CourseIndex index) {
        long now = SystemClock.uptimeMillis();
        if (now - course_last_click > COURSE_CLICK_THRESH) {
            course_last_click = now;
            CourseDetailDialogFragment.newInstance(index, mThisWeekNum).show(getSupportFragmentManager(), "CourseDetail");
        }
    }

    /**
     * 重新导入课表对话框，回调函数
     */
    @Override
    public void onReloadTable() {
        refresh(true);
    }

    /**
     * 登出对话框，回调函数
     */
    @Override
    public void onUserLogout() {
        mRefresh.setRefreshing(true);
        new LogoutTask(this).execute();
    }

    /**
     * 登出任务，回调函数
     * @param res result of logout, true in most case
     */
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

        private static final String[] WEEKDAY = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
        CourseIndex courseIndex;
        int weekNum;

        TextView courseName, teacherName, courseTime, classroom, credit, hours;

        static volatile boolean isShowing;

        public static CourseDetailDialogFragment newInstance(CourseIndex index, int weekNum) {
            CourseDetailDialogFragment fragment = new CourseDetailDialogFragment();
            fragment.courseIndex = index;
            fragment.weekNum = weekNum;

            return fragment;
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.table_course_detail_sheet, container, false);
            courseName = v.findViewById(R.id.course_name);
            teacherName = v.findViewById(R.id.course_teacher_name);
            courseTime = v.findViewById(R.id.course_time);
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
                courseTime.setText(getString(R.string.course_detail_item_time, weekNum, WEEKDAY[courseIndex.weekday % 7],
                        courseIndex.sectionStart, courseIndex.sectionEnd));
                classroom.setText(getString(R.string.course_detail_item_classroom, courseIndex.classroom));
                credit.setText(getString(R.string.course_detail_item_credit, courseIndex.course.credit));
                hours.setText(getString(R.string.course_detail_item_hours_all, courseIndex.course.hours_all));
            }
        }

        @Override
        public void show(FragmentManager manager, String tag) {
            if (!isShowing) {
                isShowing = true;
                super.show(manager, tag);
            }
        }

        @Override
        public void onCancel(DialogInterface dialog) {
            isShowing = false;
            super.onCancel(dialog);
        }
    }

    public static class CourseListFragment extends Fragment {
        Courses courses;

        RecyclerView courseListRecyclerView;    // Show exams
        CourseListAdapter courseListAdapter;              // Exams adapter
        LinearLayoutManager courseListLayoutManager; // LayoutManager

        public static CourseListFragment newInstance(Courses courses) {
            CourseListFragment fragment = new CourseListFragment();
            fragment.courses = courses;
            return fragment;
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            courseListRecyclerView = new RecyclerView(inflater.getContext());
            courseListAdapter = new CourseListAdapter();
            courseListAdapter.setCourses(courses);
            courseListLayoutManager = new LinearLayoutManager(inflater.getContext());

            courseListRecyclerView.setAdapter(courseListAdapter);
            courseListRecyclerView.setLayoutManager(courseListLayoutManager);
            return courseListRecyclerView;
        }

        static class CourseListAdapter extends RecyclerView.Adapter<CourseListAdapter.ViewHolder>{
            // Adapter data source
            Courses courses;

            public Courses getCourses() {
                return courses;
            }

            public void setCourses(Courses courses) {
                this.courses = courses;
            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_course_item, parent, false);
                return new ViewHolder(v);
            }

            @Override
            public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
                holder.bind(courses.data.get(position));
            }

            @Override
            public int getItemCount() {
                if(courses == null || courses.data == null) return 0;
                return courses.data.size();
            }

            static class ViewHolder extends RecyclerView.ViewHolder {
                TextView nameAndIsExp, teacherAndAcademy, schedule, studentDetail;
                View btnAdd;

                public ViewHolder(View v) {
                    super(v);
                    nameAndIsExp = v.findViewById(R.id.search_course_item_name);
                    teacherAndAcademy = v.findViewById(R.id.search_course_item_teacher);
                    schedule = v.findViewById(R.id.search_course_item_schedule);
                    studentDetail = v.findViewById(R.id.search_course_item_detail);
                    btnAdd = v.findViewById(R.id.search_course_btn_add);
                }

                @SuppressLint("SetTextI18n")
                void bind(Courses.Course course) {
                    String name = course.course_name;
                    if(course.is_exp) {
                        name += "(实验课)";
                    }
                    nameAndIsExp.setText(name);
                    teacherAndAcademy.setText(course.teacher + " " + course.academy);
                    StringBuilder scheduleSb = new StringBuilder();
                    for(Courses.Course.Schedule sc : course.schedule) {
                        scheduleSb
                                .append(sc.weeks).append("周 ")
                                .append("周").append(sc.classtime).append(" ")
                                .append(sc.classroom).append("\n");
                    }
                    // remove the last '\n' char
                    schedule.setText(scheduleSb.deleteCharAt(scheduleSb.length()-1).toString());
                    studentDetail.setText(course.class_detail + " " + course.student_cnt + "人");

                    btnAdd.setOnClickListener(v -> {

                    });
                }
            }
        }
    }

}
