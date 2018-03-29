package com.andreamapp.cqu.table;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.andreamapp.cqu.R;
import com.andreamapp.cqu.exams.ExamsActivity;
import com.andreamapp.cqu.grade.GradeActivity;

import java.util.Calendar;
import java.util.List;
import java.util.Set;

/**
 * Created by Andream on 2018/3/24.
 * Email: andreamapp@qq.com
 * Website: http://andreamapp.com
 */

public class TableFragment extends AppCompatActivity {

    // TODO: Maybe can be replaced by RecyclerView
//    TableView mTableView;
    Toolbar mToolBar;
    ViewPager mViewPager;
    TableViewModel mTableViewModel;

    Calendar semesterStartDate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.table_fragment);

//        mTableView = findViewById(R.id.table_view);
        mToolBar = findViewById(R.id.tool_bar);
        mViewPager = findViewById(R.id.table_view_pager);

        mTableViewModel = ViewModelProviders.of(this).get(TableViewModel.class);
        mTableViewModel.fetchIndexes().observe(this, new Observer<List<Set<CourseIndex>>>() {
            @Override
            public void onChanged(@Nullable List<Set<CourseIndex>> indexes) {
                WeekPagerAdapter adapter = new WeekPagerAdapter(getSupportFragmentManager(), indexes);
                mViewPager.setAdapter(adapter);
                Calendar c = Calendar.getInstance();
                // Todo: Fetch it from network
                c.set(2018, Calendar.MARCH, 5, 0, 0, 0);
                setSemesterStartDate(c);
            }
        });

        setSupportActionBar(mToolBar);
        mToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_exams:
                        startActivity(new Intent(TableFragment.this, ExamsActivity.class));
                        break;
                    case R.id.action_grade:
                        startActivity(new Intent(TableFragment.this, GradeActivity.class));
                        break;
                }
                return true;
            }
        });

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
        mViewPager.setCurrentItem((int) weekNum);
    }

    class WeekPagerAdapter extends FragmentPagerAdapter {

        List<Set<CourseIndex>> indexes;

        public WeekPagerAdapter(FragmentManager fm, List<Set<CourseIndex>> indexes) {
            super(fm);
            this.indexes = indexes;
        }

        @Override
        public Fragment getItem(int position) {
            return TableWeekFragment.newInstance(indexes, position + 1);
        }

        @Override
        public int getCount() {
            return 25;
        }
    }

    public static class TableWeekFragment extends Fragment {
        List<Set<CourseIndex>> indexes;
        int week;

        static TableWeekFragment newInstance(List<Set<CourseIndex>> indexes, int week) {
            TableWeekFragment f = new TableWeekFragment();

            f.indexes = indexes;
            f.week = week;
            return f;
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            TableView v = new TableView(inflater.getContext(), null);
            v.setIndexes(indexes);
            v.setCurrentWeek(week);
            return v;
        }
    }
}
