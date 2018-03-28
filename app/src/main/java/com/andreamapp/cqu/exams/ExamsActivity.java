package com.andreamapp.cqu.exams;

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
import com.andreamapp.cqu.bean.Exams;
import com.andreamapp.cqu.bean.Grade;
import com.andreamapp.cqu.grade.GradeActivity;

/**
 * Created by Andream on 2018/3/27.
 * Email: andreamapp@qq.com
 * Website: http://andreamapp.com
 */

public class ExamsActivity extends AppCompatActivity {

    Toolbar mToolBar;
    RecyclerView mExamsRecyclerView;
    ExamsAdapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    ExamsViewModel mViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exams_activity);

        mToolBar = findViewById(R.id.tool_bar);
        mExamsRecyclerView = findViewById(R.id.exams_recycler_view);

        setSupportActionBar(mToolBar);

        mAdapter = new ExamsAdapter();
        mExamsRecyclerView.setAdapter(mAdapter);

        mLayoutManager = new LinearLayoutManager(this);
        mExamsRecyclerView.setLayoutManager(mLayoutManager);

        mViewModel = ViewModelProviders.of(this).get(ExamsViewModel.class);
        mViewModel.fetch().observe(this, new Observer<Exams>() {
            @Override
            public void onChanged(@Nullable Exams exams) {
                mAdapter.setExams(exams);
            }
        });
    }

    public static class ExamsAdapter extends RecyclerView.Adapter<GradeActivity.GradeAdapter.ViewHolder> {
        Exams exams;

        public Exams getExams() {
            return exams;
        }

        public void setExams(Exams grade) {
            this.exams = grade;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public GradeActivity.GradeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.grade_item, parent, false);
            GradeActivity.GradeAdapter.ViewHolder vh = new GradeActivity.GradeAdapter.ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull GradeActivity.GradeAdapter.ViewHolder holder, int position) {
            int index = 0;
        }

        @Override
        public int getItemCount() {
            return 0;
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
