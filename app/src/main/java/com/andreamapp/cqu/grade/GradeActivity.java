package com.andreamapp.cqu.grade;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
    RecyclerView mGradeRecyclerView;
    GradeAdapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    GradeViewModel mViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grade_activity);

        mToolBar = findViewById(R.id.tool_bar);
        mGradeRecyclerView = findViewById(R.id.grade_recycler_view);

        setSupportActionBar(mToolBar);

        mAdapter = new GradeAdapter();
        mGradeRecyclerView.setAdapter(mAdapter);

        mLayoutManager = new LinearLayoutManager(this);
        mGradeRecyclerView.setLayoutManager(mLayoutManager);

        mViewModel = ViewModelProviders.of(this).get(GradeViewModel.class);
        mViewModel.fetch().observe(this, new Observer<Grade>() {
            @Override
            public void onChanged(@Nullable Grade grade) {
                mAdapter.setGrade(grade);
            }
        });
    }

    public static class GradeAdapter extends RecyclerView.Adapter<GradeAdapter.ViewHolder> {
        Grade grade;

        public Grade getGrade() {
            return grade;
        }

        public void setGrade(Grade grade) {
            this.grade = grade;
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
            int index = 0;
            o:
            for (int i = grade.data.size() - 1; i >= 0; i--) {
                Grade.SemesterGrade sg = grade.data.get(i);
                if (sg.data != null) {
                    for (Grade.SemesterGrade.CourseGrade cg : sg.data) {
                        if (index == position) {
                            holder.bind(cg);
                            break o;
                        } else {
                            index++;
                        }
                    }
                }
            }
        }

        @Override
        public int getItemCount() {
            if (grade == null || grade.data == null || grade.data.size() == 0) {
                return 0;
            }
            int sum = 0;
            for (Grade.SemesterGrade sg : grade.data) {
                sum += sg.data == null ? 0 : sg.data.size();
            }
            return sum;
        }

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public static class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            public TextView name, credit, score;
            Grade.SemesterGrade.CourseGrade courseGrade;

            public ViewHolder(View v) {
                super(v);
                name = v.findViewById(R.id.grade_item_course_name);
                credit = v.findViewById(R.id.grade_item_credit);
                score = v.findViewById(R.id.grade_item_score);
            }

            public void bind(Grade.SemesterGrade.CourseGrade cg) {
                this.courseGrade = cg;
                name.setText(cg.course_name);
                credit.setText(cg.credit);
                score.setText(cg.grade);
            }
        }
    }
}
