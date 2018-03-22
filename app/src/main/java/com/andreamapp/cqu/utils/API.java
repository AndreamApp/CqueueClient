package com.andreamapp.cqu.utils;

import android.content.Context;

import com.andreamapp.cqu.bean.Exams;
import com.andreamapp.cqu.bean.Grade;
import com.andreamapp.cqu.bean.Table;
import com.andreamapp.cqu.bean.User;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Andream on 2018/2/27.
 * Website: http://andream.com.cn
 * Email: me@andream.com.cn
 */

public class API {

    public static final String HOST = "http://39.107.228.154";
    public static final String URL_LOGIN = "/api/login";
    public static final String URL_GET_TABLE = "/api/getTable";
    public static final String URL_GET_GRADE = "/api/getGrade";
    public static final String URL_GET_EXAMS = "/api/getExams";

    private static OkHttpClient withCookie(Context context){
        CookieJar cookieJar =  new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(context));
        OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .cookieJar(cookieJar)
            .build();
        return client;
    }

    private static OkHttpClient withSaveOnlyCookie(Context context){
        CookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(context)){
            @Override
            public synchronized List<Cookie> loadForRequest(HttpUrl url) {
                return new ArrayList<Cookie>();
            }
        };
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .cookieJar(cookieJar)
                .build();
        return client;
    }

    public static User login(Context context, String stunum, String password) throws IOException {
        OkHttpClient client = withSaveOnlyCookie(context);
        Request request = new Request.Builder()
                .url(HOST + URL_LOGIN + "?stunum=" + stunum + "&password=" + password)
                .build();
        Response response = client.newCall(request).execute();
        if (response.code() == 200) {
            return new Gson().fromJson(response.body().string(), User.class);
        }
        else {
            throw new IOException("Response with code " + response.code());
        }
    }

    public static Table getTable(Context context) throws IOException {
        OkHttpClient client = withCookie(context);
        Request request = new Request.Builder()
                .url(HOST + URL_GET_TABLE)
                .build();
        Response response = client.newCall(request).execute();
        if (response.code() == 200) {
            return new Gson().fromJson(response.body().string(), Table.class);
        }
        else {
            throw new IOException("Response with code " + response.code());
        }
    }

    public static Grade getGrade(Context context) throws IOException {
        OkHttpClient client = withCookie(context);
        Request request = new Request.Builder()
                .url(HOST + URL_GET_GRADE)
                .build();
        Response response = client.newCall(request).execute();
        if (response.code() == 200) {
            return new Gson().fromJson(response.body().string(), Grade.class);
        }
        else {
            throw new IOException("Response with code " + response.code());
        }
    }

    public static Exams getExams(Context context) throws IOException {
        OkHttpClient client = withCookie(context);
        Request request = new Request.Builder()
                .url(HOST + URL_GET_EXAMS)
                .build();
        Response response = client.newCall(request).execute();
        if (response.code() == 200) {
            return new Gson().fromJson(response.body().string(), Exams.class);
        }
        else {
            throw new IOException("Response with code " + response.code());
        }
    }
}
