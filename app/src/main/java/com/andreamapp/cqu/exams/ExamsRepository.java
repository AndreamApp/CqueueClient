package com.andreamapp.cqu.exams;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.andreamapp.cqu.base.BaseRespTask;
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


    public static class ExamsTask extends BaseRespTask<Boolean, Exams> {
        ExamsTask(MutableLiveData<Exams> res) {
            super(res);
        }

        @Override
        public Exams newInstance() {
            return new Exams();
        }

        @Override
        public Exams getResult(Boolean[] network) throws ANError {
            return API.getExams(network[0]);
        }

        @Override
        public boolean fromNetwork(Boolean[] network) {
            return network[0];
        }

        @Override
        public Exams redo(Boolean[] network) {
            network[0] = true;
            return super.redo(network);
        }
    }
}
