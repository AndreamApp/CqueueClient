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
import com.andreamapp.cqu.bean.Grade;

/**
 * Created by Andream on 2018/3/27.
 * Email: andreamapp@qq.com
 * Website: http://andreamapp.com
 */

public class GradeActivity extends BaseModelActivity<Grade> {

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
        mPagerAdapter = new SemesterPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        refresh(false, null);
    }

    public void refresh(boolean fromNetwork, final GradeSemesterFragment fragment) {
        if (mPagerAdapter.getCount() == 0) {
            showState(R.string.state_loading);
        }
        mViewModel.fetch(fromNetwork).observe(this, new Observer<Grade>() {
            @Override
            public void onChanged(@Nullable Grade grade) {
                // Call for msg snack bar
                GradeActivity.super.onChanged(grade);

                if (fragment != null) {
                    fragment.mRefresh.setRefreshing(false);
                }

                if (grade != null && grade.status()) {
                    mPagerAdapter.setGrade(grade);
                    mPagerAdapter.notifyDataSetChanged();


                    // show empty
                    if (mPagerAdapter.getCount() == 0) {
                        showState(R.string.state_no_grade);
                    }
                    // show pager content
                    else {
                        hideState();
                    }
                } else {
                    // show error
                    if (mPagerAdapter.getCount() == 0) {
                        showState(R.string.state_request_error, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                refresh(true, fragment);
                            }
                        });
                    }
                    // keep old data
                    else {
                        hideState();
                    }
                }
            }
        });
    }

    @Override
    protected boolean showState(boolean show, int resId, View.OnClickListener clickHandler) {
        mPager.setVisibility(show ? View.GONE : View.VISIBLE);
        return super.showState(show, resId, clickHandler);
    }

    class SemesterPagerAdapter extends FragmentPagerAdapter {

        Grade grade;

        SemesterPagerAdapter(FragmentManager fm) {
            super(fm);
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

        public Grade getGrade() {
            return grade;
        }

        public void setGrade(Grade grade) {
            this.grade = grade;
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
        TextView mGpaText;
        RecyclerView mGradeRecyclerView;
        GradeAdapter mAdapter;
        LinearLayoutManager mLayoutManager;

        SwipeRefreshLayout mRefresh;

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.grade_semester_pager, container, false);
            mTitleText = v.findViewById(R.id.grade_semester_text);
            mGpaText = v.findViewById(R.id.grade_gpa_text);
            mGradeRecyclerView = v.findViewById(R.id.grade_recycler_view);
            mRefresh = v.findViewById(R.id.refresh);

            mTitleText.setText(sg.semester);
            mGpaText.setText(getString(R.string.grade_gpa_text, sg.gpa));

            mAdapter = new GradeAdapter();
            mAdapter.setSemesterGrade(sg);
            mGradeRecyclerView.setAdapter(mAdapter);

            mLayoutManager = new LinearLayoutManager(getContext());
            mGradeRecyclerView.setLayoutManager(mLayoutManager);

            GradeActivity activity = (GradeActivity) getActivity();
            if (activity != null) {
                mRefresh.setColorSchemeColors(activity.getPrimiryColor(), activity.getPrimiryColor(), activity.getAccentColor());
            }
            mRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    if (getActivity() != null) {
                        ((GradeActivity) getActivity()).refresh(true, GradeSemesterFragment.this);
                    }
                }
            });
            mGradeRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    mRefresh.setEnabled(mLayoutManager.findFirstCompletelyVisibleItemPosition() == 0);
                }
            });

            return v;
        }
    }

    public static class GradeAdapter extends RecyclerView.Adapter<GradeAdapter.ViewHolder> {
        Grade.SemesterGrade sg;

        void setSemesterGrade(Grade.SemesterGrade sg) {
            this.sg = sg;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.grade_item, parent, false);
            return new ViewHolder(v);
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
        static class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            TextView name, credit, score;
            Grade.SemesterGrade.CourseGrade courseGrade;

            ViewHolder(View v) {
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
