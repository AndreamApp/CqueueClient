package com.andreamapp.cqu.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.format.DateFormat;

import com.andreamapp.cqu.App;
import com.andreamapp.cqu.bean.User;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Andream on 2018/9/1.
 * Email: andreamapp@qq.com
 * Website: http://andreamapp.com
 */

public class Cache {

    public static void saveUser(@NonNull User user){
        App.context().getSharedPreferences("cache", Context.MODE_PRIVATE)
                .edit()
                .putString("user_profile", new Gson().toJson(user))
                .apply();
    }

    @Nullable
    public static User currentUser() {
        String json = App.context().getSharedPreferences("cache", Context.MODE_PRIVATE)
                .getString("user_profile", "");
        User user = null;
        try {
            user = new Gson().fromJson(json, User.class);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return user;
    }
}
