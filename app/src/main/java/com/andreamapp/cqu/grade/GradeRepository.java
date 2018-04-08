package com.andreamapp.cqu.grade;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.andreamapp.cqu.base.BaseRespTask;
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

    public static class GradeTask extends BaseRespTask<Boolean, Grade> {
        GradeTask(MutableLiveData<Grade> res) {
            super(res);
        }

        @Override
        public Grade newInstance() {
            return new Grade();
        }

        @Override
        public Grade getResult(Boolean[] network) throws ANError {
            return API.getGrade(network[0]);
        }

        @Override
        public boolean fromNetwork(Boolean[] network) {
            return network[0];
        }

        @Override
        public Grade redo(Boolean[] network) {
            network[0] = true;
            return super.redo(network);
        }
    }
}
