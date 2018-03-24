package com.andreamapp.cqu;

import android.app.Application;

/**
 * Created by Andream on 2018/3/24.
 * Email: andreamapp@qq.com
 * Website: http://andreamapp.com
 */

public class App extends Application {

    private static Application context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    public static Application context() {
        return context;
    }
}
