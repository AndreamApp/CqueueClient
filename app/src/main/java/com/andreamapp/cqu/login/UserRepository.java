package com.andreamapp.cqu.login;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;

import com.andreamapp.cqu.bean.User;
import com.andreamapp.cqu.utils.API;
import com.androidnetworking.error.ANError;

/**
 * Created by Andream on 2018/3/24.
 * Email: andreamapp@qq.com
 * Website: http://andreamapp.com
 */

public class UserRepository {

    /**
     * 从网络登录教务网账号，返回LiveData<User>
     * User.status标识了返回的状态
     */
    public static LiveData<User> login(String studentNum, String password) {
        MutableLiveData<User> res = new MutableLiveData<>();
        new LoginTask(res).execute(studentNum, password);
        return res;
    }

    public static class LoginTask extends AsyncTask<String, Void, User> {
        MutableLiveData<User> res;

        LoginTask(MutableLiveData<User> res) {
            this.res = res;
        }

        @Override
        protected User doInBackground(String... args) {
            User user = new User();
            try {
                user = API.login(args[0], args[1]);
            } catch (ANError e) {
                e.printStackTrace();
                user.status = false;
                switch (e.getErrorCode()) {
                    case 0:
                        user.err = "网络错误";
                        break;
                    default:
                        user.err = e.getErrorCode() + ": " + e.getErrorDetail();
                }
            }
            return user;
        }

        @Override
        protected void onPostExecute(User user) {
            res.setValue(user);
        }
    }
}
