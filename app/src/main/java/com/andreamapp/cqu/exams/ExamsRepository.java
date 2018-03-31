package com.andreamapp.cqu.exams;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;

import com.andreamapp.cqu.App;
import com.andreamapp.cqu.bean.Exams;
import com.andreamapp.cqu.utils.API;

import java.io.IOException;

/**
 * Created by Andream on 2018/3/27.
 * Email: andreamapp@qq.com
 * Website: http://andreamapp.com
 */

public class ExamsRepository {

    public static LiveData<Exams> fetch() {
        MutableLiveData<Exams> data = new MutableLiveData<>();
        new ExamsTask(data).execute();
        return data;
    }


    public static class ExamsTask extends AsyncTask<Void, Void, Exams> {
        MutableLiveData<Exams> res;

        ExamsTask(MutableLiveData<Exams> res) {
            this.res = res;
        }

        @Override
        protected Exams doInBackground(Void... args) {
            Exams exams = new Exams();
            try {
                exams = API.getExams(App.context());
            } catch (IOException e) {
                e.printStackTrace();
                exams.status = false;
                exams.err = "网络错误";
            }
            return exams;
        }

        @Override
        protected void onPostExecute(Exams exams) {
            res.setValue(exams);
        }
    }
}
