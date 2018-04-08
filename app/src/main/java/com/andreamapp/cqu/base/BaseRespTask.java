package com.andreamapp.cqu.base;

import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;

import com.andreamapp.cqu.bean.Resp;
import com.androidnetworking.error.ANError;

/**
 * Created by Andream on 2018/4/8.
 * Email: andreamapp@qq.com
 * Website: http://andreamapp.com
 */

public abstract class BaseRespTask<Params, Result extends Resp> extends AsyncTask<Params, Void, Result> {

    MutableLiveData<Result> res;

    public BaseRespTask(MutableLiveData<Result> res) {
        this.res = res;
    }

    @Override
    protected Result doInBackground(Params... params) {
        Result result = newInstance();
        try {
            result = getResult(params);
            // maybe get error response from cache, recall from network
            if (!fromNetwork(params) && !result.status) {
                return redo(params);
            }
        } catch (ANError e) {
            e.printStackTrace();
            // maybe get error response from cache, recall from network
            if (!fromNetwork(params)) {
                return redo(params);
            }
            result.status = false;
            switch (e.getErrorCode()) {
                case 0:
                    result.err = "网络错误";
                    break;
                case 429:
                    result.err = "请求太频繁啦";
                    break;
                default:
                    result.err = e.getErrorCode() + ": " + e.getErrorDetail();
            }
        }
        return result;
    }

    @Override
    protected void onPostExecute(Result result) {
        res.setValue(result);
    }

    public abstract Result newInstance();

    public abstract Result getResult(Params[] params) throws ANError;

    public boolean fromNetwork(Params[] params) {
        return true;
    }

    public Result redo(Params[] params) {
        return doInBackground(params);
    }
}
