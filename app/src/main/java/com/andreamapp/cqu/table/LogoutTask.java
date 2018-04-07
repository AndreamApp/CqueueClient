package com.andreamapp.cqu.table;

import android.os.AsyncTask;

import com.andreamapp.cqu.App;
import com.andreamapp.cqu.utils.API;
import com.androidnetworking.error.ANError;

/**
 * Created by Andream on 2018/4/7.
 * Email: andreamapp@qq.com
 * Website: http://andreamapp.com
 */


public class LogoutTask extends AsyncTask<Void, Void, Boolean> {

    private Callback callback;

    public LogoutTask(Callback callback) {
        this.callback = callback;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        Boolean res = false;
        try {
            API.logout(App.context());
            res = true;
        } catch (ANError anError) {
            anError.printStackTrace();
        }
        return res;
    }

    @Override
    protected void onPostExecute(Boolean res) {
        if (callback != null) {
            callback.onLogout(res);
        }
    }

    public interface Callback {
        void onLogout(boolean res);
    }
}
