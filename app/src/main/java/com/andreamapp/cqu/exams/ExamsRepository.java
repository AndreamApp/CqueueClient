package com.andreamapp.cqu.exams;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;

import com.andreamapp.cqu.bean.Exams;
import com.andreamapp.cqu.utils.API;
import com.androidnetworking.error.ANError;

/**
 * Created by Andream on 2018/3/27.
 * Email: andreamapp@qq.com
 * Website: http://andreamapp.com
 */

public class ExamsRepository {

    public static LiveData<Exams> fetch(boolean fromNetwork) {
        MutableLiveData<Exams> data = new MutableLiveData<>();
        new ExamsTask(data).execute(fromNetwork);
        return data;
    }


    public static class ExamsTask extends AsyncTask<Boolean, Void, Exams> {
        MutableLiveData<Exams> res;

        ExamsTask(MutableLiveData<Exams> res) {
            this.res = res;
        }

        @Override
        protected Exams doInBackground(Boolean... fromNetwork) {
            Exams exams = new Exams();
            try {
                exams = API.getExams(fromNetwork[0]);
                // maybe get error response from cache, recall from network
                if (!fromNetwork[0] && !exams.status) {
                    return doInBackground(Boolean.TRUE);
                }
            } catch (ANError e) {
                e.printStackTrace();
                // maybe get error response from cache, recall from network
                if (!fromNetwork[0]) {
                    return doInBackground(Boolean.TRUE);
                }
                exams.status = false;
                switch (e.getErrorCode()) {
                    case 0:
                        exams.err = "网络错误";
                        break;
                    default:
                        exams.err = e.getErrorCode() + ": " + e.getErrorDetail();
                }
            }
            return exams;
        }

        @Override
        protected void onPostExecute(Exams exams) {
            res.setValue(exams);
        }
    }
}
