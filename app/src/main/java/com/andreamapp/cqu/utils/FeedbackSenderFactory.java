package com.andreamapp.cqu.utils;

import android.content.Context;
import android.support.annotation.NonNull;

import org.acra.config.CoreConfiguration;
import org.acra.sender.ReportSender;
import org.acra.sender.ReportSenderFactory;

/**
 * Created by Andream on 2018/4/15.
 * Email: andreamapp@qq.com
 * Website: http://andreamapp.com
 */

public class FeedbackSenderFactory implements ReportSenderFactory {
    @NonNull
    @Override
    public ReportSender create(@NonNull Context context, @NonNull CoreConfiguration config) {
        return new FeedbackSender();
    }

    @Override
    public boolean enabled(@NonNull CoreConfiguration config) {
        return true;
    }
}
