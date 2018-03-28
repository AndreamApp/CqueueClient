package com.andreamapp.cqu.grade;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andreamapp.cqu.R;
import com.andreamapp.cqu.bean.Grade;

/**
 * Created by Andream on 2018/3/27.
 * Email: andreamapp@qq.com
 * Website: http://andreamapp.com
 */


/**
 * TODO: Semester Header divider
 * Card Semester
 * SwipeRefresh
 */
public class GradeActivity extends AppCompatActivity {

    Toolbar mToolBar;
    ViewPager mPager;
    SemesterPagerAdapter mPagerAdapter;

    GradeViewModel mViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grade_activity);

        mToolBar = findViewById(R.id.tool_bar);
        mPager = findViewById(R.id.grade_pager);

        setSupportActionBar(mToolBar);

        mViewModel = ViewModelProviders.of(this).get(GradeViewModel.class);
        mViewModel.fetch().observe(this, new Observer<Grade>() {
            @Override
            public void onChanged(@Nullable Grade grade) {
                if (grade != null) {
                    mPagerAdapter = new SemesterPagerAdapter(getSupportFragmentManager(), grade);
                    mPager.setAdapter(mPagerAdapter);
                }
            }
        });
    }

    class SemesterPagerAdapter extends FragmentPagerAdapter {
        Grade grade;

        SemesterPagerAdapter(FragmentManager fm, @NonNull Grade grade) {
            super(fm);
            this.grade = grade;
        }

        @Override
        public Fragment getItem(int position) {
            return GradeSemesterFragment.newInstance(grade.data.get(grade.data.size() - position - 1));
        }

        @Override
        public int getCount() {
            if (grade == null || grade.data == null) {
                return 0;
            }
            return grade.data.size();
        }
    }

    public static class GradeSemesterFragment extends Fragment {

        public static GradeSemesterFragment newInstance(@NonNull Grade.SemesterGrade sg) {
            GradeSemesterFragment fragment = new GradeSemesterFragment();
            fragment.sg = sg;
            return fragment;
        }

        Grade.SemesterGrade sg;
        TextView mTitleText;
        RecyclerView mGradeRecyclerView;
        GradeAdapter mAdapter;
        RecyclerView.LayoutManager mLayoutManager;

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.grade_semester_pager, container, false);
            mTitleText = v.findViewById(R.id.grade_semester_text);
            mGradeRecyclerView = v.findViewById(R.id.grade_recycler_view);

            mTitleText.setText(sg.semester);

            mAdapter = new GradeAdapter();
            mAdapter.setSemesterGrade(sg);
            mGradeRecyclerView.setAdapter(mAdapter);

            mLayoutManager = new LinearLayoutManager(getContext());
            mGradeRecyclerView.setLayoutManager(mLayoutManager);
            return v;
        }
    }

    public static class GradeAdapter extends RecyclerView.Adapter<GradeAdapter.ViewHolder> {
        @NonNull
        Grade.SemesterGrade sg;

        public void setSemesterGrade(Grade.SemesterGrade sg) {
            this.sg = sg;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.grade_item, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.bind(sg.data.get(position));
        }

        @Override
        public int getItemCount() {
            if (sg == null || sg.data == null) {
                return 0;
            }
            return sg.data.size();
        }

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public static class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            TextView name, credit, score;
            Grade.SemesterGrade.CourseGrade courseGrade;

            public ViewHolder(View v) {
                super(v);
                name = v.findViewById(R.id.grade_item_course_name);
                credit = v.findViewById(R.id.grade_item_credit);
                score = v.findViewById(R.id.grade_item_score);
            }

            void bind(@NonNull Grade.SemesterGrade.CourseGrade cg) {
                this.courseGrade = cg;
                name.setText(cg.course_name);
                credit.setText(cg.credit);
                score.setText(cg.grade);
            }
        }
    }
}
