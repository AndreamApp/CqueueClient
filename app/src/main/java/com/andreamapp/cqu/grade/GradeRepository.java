package com.andreamapp.cqu.grade;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;

import com.andreamapp.cqu.bean.Grade;
import com.andreamapp.cqu.utils.API;
import com.androidnetworking.error.ANError;

/**
 * Created by Andream on 2018/3/27.
 * Email: andreamapp@qq.com
 * Website: http://andreamapp.com
 */

public class GradeRepository {

    public static LiveData<Grade> fetch(boolean fromNetwork) {
        MutableLiveData<Grade> data = new MutableLiveData<>();
        new GradeTask(data).execute(fromNetwork);
        return data;
    }

    public static class GradeTask extends AsyncTask<Boolean, Void, Grade> {
        MutableLiveData<Grade> res;

        GradeTask(MutableLiveData<Grade> res) {
            this.res = res;
        }

        @Override
        protected Grade doInBackground(Boolean... fromNetwork) {
            Grade grade = new Grade();
            try {
                grade = API.getGrade(fromNetwork[0]);
            } catch (ANError e) {
                e.printStackTrace();
                grade.status = false;
                switch (e.getErrorCode()) {
                    case 0:
                        grade.err = "网络错误";
                        break;
                    case 504:
                        if (!fromNetwork[0]) {
                            return doInBackground(Boolean.TRUE);
                        }
                    default:
                        grade.err = e.getErrorCode() + ": " + e.getErrorDetail();
                }
            }
            return grade;
        }

        @Override
        protected void onPostExecute(Grade grade) {
            res.setValue(grade);
        }
    }
}
