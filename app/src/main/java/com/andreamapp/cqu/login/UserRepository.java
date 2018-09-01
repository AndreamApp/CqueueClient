package com.andreamapp.cqu.login;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;

import com.andreamapp.cqu.App;
import com.andreamapp.cqu.base.BaseRespTask;
import com.andreamapp.cqu.bean.User;
import com.andreamapp.cqu.utils.API;
import com.andreamapp.cqu.utils.Cache;
import com.androidnetworking.error.ANError;
import com.google.gson.Gson;

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

    public static class LoginTask extends BaseRespTask<String, User> {
        LoginTask(MutableLiveData<User> res) {
            super(res);
        }

        @Override
        public User newInstance() {
            return new User();
        }

        @Override
        public User getResult(String[] args) throws ANError {
            User user = API.login(args[0], args[1]);
            // save user profile info
            if(user != null){
                Cache.saveUser(user);
            }
            return user;
        }
    }

}
