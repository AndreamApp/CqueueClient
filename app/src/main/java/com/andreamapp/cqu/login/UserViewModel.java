package com.andreamapp.cqu.login;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.andreamapp.cqu.bean.User;

/**
 * Created by Andream on 2018/3/22.
 * Email: andreamapp@qq.com
 * Website: http://andreamapp.com
 */

public class UserViewModel extends ViewModel {
    private LiveData<User> user;

    public void fetch(String studentNum, String password) {
        user = UserRepository.login(studentNum, password);
    }

    public LiveData<User> getUser() {
        return this.user;
    }
}
