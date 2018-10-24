package com.andreamapp.cqu;

import android.app.Application;
import android.content.Context;

import com.andreamapp.cqu.utils.FeedbackSenderFactory;
import com.androidnetworking.AndroidNetworking;

import org.acra.ACRA;
import org.acra.annotation.AcraCore;
import org.acra.data.StringFormat;

/**
 * Created by Andream on 2018/3/24.
 * Email: andreamapp@qq.com
 * Website: http://andreamapp.com
 */

@AcraCore(
        buildConfigClass = BuildConfig.class,
        reportFormat = StringFormat.JSON,
        reportSenderFactoryClasses = FeedbackSenderFactory.class
)
public class App extends Application {

    private static Application context;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

//        ACRA.init(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        // init network
        AndroidNetworking.initialize(this);
    }

    public static Application context() {
        return context;
    }
}
