package com.andreamapp.cqu.grade;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;

import com.andreamapp.cqu.App;
import com.andreamapp.cqu.bean.Grade;
import com.andreamapp.cqu.utils.API;

import java.io.IOException;

/**
 * Created by Andream on 2018/3/27.
 * Email: andreamapp@qq.com
 * Website: http://andreamapp.com
 */

public class GradeRepository {

    public static LiveData<Grade> fetch() {
        MutableLiveData<Grade> data = new MutableLiveData<>();
        new GradeTask(data).execute();
        return data;
    }

    public static class GradeTask extends AsyncTask<Void, Void, Grade> {
        MutableLiveData<Grade> res;

        GradeTask(MutableLiveData<Grade> res) {
            this.res = res;
        }

        @Override
        protected Grade doInBackground(Void... args) {
            Grade grade = new Grade();
            try {
                grade = API.getGrade(App.context());
            } catch (IOException e) {
                e.printStackTrace();
                grade.status = false;
                grade.err = "网络错误";
            }
            return grade;
        }

        @Override
        protected void onPostExecute(Grade grade) {
            res.setValue(grade);
        }
    }
}
