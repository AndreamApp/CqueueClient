package com.andreamapp.cqu.utils;

import android.content.Context;
import android.support.annotation.NonNull;

import com.androidnetworking.error.ANError;

import org.acra.data.CrashReportData;
import org.acra.sender.ReportSender;
import org.acra.sender.ReportSenderException;

/**
 * Created by Andream on 2018/4/15.
 * Email: andreamapp@qq.com
 * Website: http://andreamapp.com
 */

public class FeedbackSender implements ReportSender {
    @Override
    public void send(@NonNull Context context, @NonNull CrashReportData errorContent) throws ReportSenderException {
        try {
            API.crash(errorContent);
        } catch (ANError anError) {
            anError.printStackTrace();
        }
    }
}
