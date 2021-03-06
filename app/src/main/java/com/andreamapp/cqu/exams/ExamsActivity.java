package com.andreamapp.cqu.exams;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andreamapp.cqu.R;
import com.andreamapp.cqu.base.BaseModelActivity;
import com.andreamapp.cqu.bean.Exams;

/**
 * Created by Andream on 2018/3/27.
 * Email: andreamapp@qq.com
 * Website: http://andreamapp.com
 */

public class ExamsActivity extends BaseModelActivity<Exams> {

    Toolbar mToolBar;                   // App tool bar
    SwipeRefreshLayout mRefresh;        // Refresh exams
    RecyclerView mExamsRecyclerView;    // Show exams
    ExamsAdapter mAdapter;              // Exams adapter
    LinearLayoutManager mLayoutManager; // LayoutManager

    ExamsViewModel mViewModel;          // Exams model

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exams_activity);

        mToolBar = findViewById(R.id.tool_bar);
        mRefresh = findViewById(R.id.refresh);
        mExamsRecyclerView = findViewById(R.id.exams_recycler_view);

        setSupportActionBar(mToolBar);

        // setup recycler
        mAdapter = new ExamsAdapter();
        mExamsRecyclerView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(this);
        mExamsRecyclerView.setLayoutManager(mLayoutManager);

        // Refresh

        mExamsRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                mRefresh.setEnabled(mLayoutManager.findFirstCompletelyVisibleItemPosition() == 0);
                super.onScrolled(recyclerView, dx, dy);
            }
        });


        mViewModel = ViewModelProviders.of(this).get(ExamsViewModel.class);

        SwipeRefreshLayout.OnRefreshListener listener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh(true);
            }
        };
        mRefresh.setColorSchemeColors(getPrimiryColor(), getPrimiryColor(), getAccentColor());
        mRefresh.setOnRefreshListener(listener);

        // perform refresh in programming way
        mRefresh.setRefreshing(true);
        refresh(false);
    }


    public void refresh(boolean fromNetwork) {
        if (mAdapter.getItemCount() == 0) {
            showState(R.string.state_loading);
        }
        mViewModel.fetch(fromNetwork).observe(ExamsActivity.this, ExamsActivity.this);
    }

    @Override
    public void onChanged(@Nullable Exams exams) {
        super.onChanged(exams);

        mRefresh.setRefreshing(false);
        if (exams != null && exams.status) {
            mAdapter.setExams(exams);
            mAdapter.notifyDataSetChanged();

            // show empty
            if (mAdapter.getItemCount() == 0) {
                showState(R.string.state_no_exams);
            }
            // show content
            else {
                hideState();
            }
        } else {
            // show error
            if (mAdapter.getItemCount() == 0) {
                showState(R.string.state_request_error, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        refresh(true);
                    }
                });
            }
            // keep old data
            else {
                hideState();
            }
        }
    }

    @Override
    protected boolean showState(boolean show, int resId, View.OnClickListener clickHandler) {
        mExamsRecyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
        return super.showState(show, resId, clickHandler);
    }

    public static class ExamsAdapter extends RecyclerView.Adapter<ExamsAdapter.ViewHolder> {
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
        public ExamsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.exams_item, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.bind(exams.data.get(position));
        }

        @Override
        public int getItemCount() {
            if (exams == null || exams.data == null) {
                return 0;
            }
            return exams.data.size();
        }

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        static class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            TextView name, time, classroom, seat;
            Exams.Exam exam;

            ViewHolder(View v) {
                super(v);
                name = v.findViewById(R.id.exams_item_name);
                time = v.findViewById(R.id.exams_item_time);
                classroom = v.findViewById(R.id.exams_item_classroom);
                seat = v.findViewById(R.id.exams_item_seat);
            }

            void bind(Exams.Exam exam) {
                this.exam = exam;
                name.setText(exam.course_name);
                time.setText(exam.time_str);
                classroom.setText(exam.classroom);
                seat.setText(seat.getResources().getString(R.string.exams_item_seat, exam.seat));
            }
        }
    }
}
