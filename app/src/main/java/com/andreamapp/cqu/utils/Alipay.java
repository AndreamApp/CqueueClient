package com.andreamapp.cqu.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.net.URISyntaxException;

/**
 * Created by Andream on 2018/4/8.
 * Email: andreamapp@qq.com
 * Website: http://andreamapp.com
 */

public class Alipay {

    private static final String ALIPAY_PACKAGE_NAME = "com.eg.android.AlipayGphone";

    private static final String QRCODE_URL = "intent://platformapi/startapp?saId=10000007&" +
            "clientVersion=3.7.0.0718&qrcode=https%3A%2F%2Fqr.alipay.com%2F{payCode}%3F_s" +
            "%3Dweb-other&_t=1472443966571#Intent;" +
            "scheme=alipayqr;package=com.eg.android.AlipayGphone;end";

    public static boolean hasInstalled(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(ALIPAY_PACKAGE_NAME, 0);
            return info != null;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean start(Context context, String payCode) {
        return openUrl(context, QRCODE_URL.replace("{payCode}", payCode));
    }

    private static boolean openUrl(Context context, String url) {
        try {
            context.startActivity(Intent.parseUri(url, Intent.URI_INTENT_SCHEME));
            return true;
        } catch (ActivityNotFoundException | URISyntaxException e) {
            e.printStackTrace();
            return false;
        }
    }

}
