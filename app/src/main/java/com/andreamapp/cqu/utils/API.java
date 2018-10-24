package com.andreamapp.cqu.utils;

import android.content.Context;
import android.util.Log;

import com.andreamapp.cqu.App;
import com.andreamapp.cqu.bean.Courses;
import com.andreamapp.cqu.bean.Exams;
import com.andreamapp.cqu.bean.Feedbacks;
import com.andreamapp.cqu.bean.Grade;
import com.andreamapp.cqu.bean.Resp;
import com.andreamapp.cqu.bean.Table;
import com.andreamapp.cqu.bean.Update;
import com.andreamapp.cqu.bean.User;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.ANResponse;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.google.gson.Gson;

import org.acra.data.CrashReportData;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

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

    private static final String HOST = "https://cqu.andream.app";
    private static final String URL_LOGIN = "/api/v1/login";
    private static final String URL_LOGOUT = "/api/v1/logout";
    private static final String URL_GET_TABLE = "/api/v1/getTable";
    private static final String URL_GET_GRADE = "/api/v1/getGrade";
    private static final String URL_GET_EXAMS = "/api/v1/getExams";
    private static final String URL_LIKE = "/api/v1/like";
    private static final String URL_CRASH = "/api/v1/crash";
    private static final String URL_UPLOAD_FEEDBACK = "/api/v1/uploadFeedback";
    private static final String URL_GET_FEEDBACKS = "/api/v1/getFeedbacks";
    private static final String URL_CHECK_UPDATE = "/api/v1/checkUpdate";
    private static final String URL_SEARCH_COURSE = "/api/v1/searchCourse";


    private static OkHttpClient.Builder trustAll() {
        // Create a trust manager that does not validate certificate chains
        final TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                    }

                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                    }

                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[]{};
                    }
                }
        };

        SSLContext sslContext = null;
        try {
            // Install the all-trusting trust manager
            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager

        } catch (Exception e) {
            e.printStackTrace();
        }

        final SSLSocketFactory sslSocketFactory = sslContext == null ? null : sslContext.getSocketFactory();

        return new OkHttpClient.Builder()
                .sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0])
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                });
    }

    private static OkHttpClient withCookie(Context context) {
        CookieJar cookieJar =  new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(context));

        return trustAll()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .cookieJar(cookieJar)
            .build();
    }

    private static OkHttpClient withSaveOnlyCookie(Context context) {
        CookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(context)){
            @Override
            public synchronized List<Cookie> loadForRequest(HttpUrl url) {
                return new ArrayList<>();
            }
        };
        return trustAll()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .cookieJar(cookieJar)
                .build();
    }

    public static boolean cookieExpired(Context context) {
        SharedPrefsCookiePersistor persistor = new SharedPrefsCookiePersistor(context);
        List<Cookie> cookies = persistor.loadAll();
        boolean res = false;
        // no cookie, treat as expired
        if (cookies == null || cookies.size() == 0) {
            res = true;
        } else {
            for (Cookie c : cookies) {
                if (c.expiresAt() < System.currentTimeMillis()) {
                    res = true;
                    break;
                }
            }
        }
        return res;
    }

    public static void clearCookies(Context context){
        SharedPrefsCookiePersistor persistor = new SharedPrefsCookiePersistor(context);
        persistor.clear();
    }

    public static boolean logout(Context context) throws ANError {
        // clear cookie
        clearCookies(context);

        // clear cache
        App.context().getSharedPreferences("cache", Context.MODE_PRIVATE)
                .edit()
                .putString("user_profile", "")
                .apply();

        ANRequest request = AndroidNetworking.post(HOST + URL_LOGOUT)
                .setPriority(Priority.LOW)
                .setOkHttpClient(withCookie(App.context()))
                .getResponseOnlyFromNetwork()
                .build();

        ANResponse response = request.executeForJSONObject();

        if (response.isSuccess()) {
            JSONObject object = (JSONObject) response.getResult();
            try {
                return object.getBoolean("status");
            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            throw response.getError();
        }
    }

    public static User login(String stunum, String password) throws ANError {
        ANRequest request = AndroidNetworking.post(HOST + URL_LOGIN)
                .addBodyParameter("stunum", stunum)
                .addBodyParameter("password", password)
                .setPriority(Priority.LOW)
                .setOkHttpClient(withSaveOnlyCookie(App.context()))
                .getResponseOnlyFromNetwork()
                .build();
        ANResponse response = request.executeForObject(User.class);

        if (response.isSuccess()) {
            return (User) response.getResult();
        } else {
            throw response.getError();
        }
    }

    public static Table getTable(boolean fromNetwork) throws ANError {
        ANRequest.GetRequestBuilder builder = AndroidNetworking.get(HOST + URL_GET_TABLE)
                .setPriority(Priority.LOW)
                .setOkHttpClient(withCookie(App.context()));
        if (!fromNetwork) {
            builder.getResponseOnlyIfCached();
        } else {
            builder.getResponseOnlyFromNetwork();
        }

        ANRequest request = builder.build();
        ANResponse response = request.executeForObject(Table.class);

        if (response.isSuccess()) {
            return (Table) response.getResult();
        } else {
            throw response.getError();
        }
    }

    public static Grade getGrade(boolean fromNetwork) throws ANError {
        ANRequest.GetRequestBuilder builder = AndroidNetworking.get(HOST + URL_GET_GRADE)
                .setPriority(Priority.LOW)
                .setOkHttpClient(withCookie(App.context()));
        if (!fromNetwork) {
            builder.getResponseOnlyIfCached();
        }

        ANRequest request = builder.build();
        ANResponse response = request.executeForObject(Grade.class);

        if (response.isSuccess()) {
            return (Grade) response.getResult();
        } else {
            throw response.getError();
        }
    }

    public static Exams getExams(boolean fromNetwork) throws ANError {
        ANRequest.GetRequestBuilder builder = AndroidNetworking.get(HOST + URL_GET_EXAMS)
                .setPriority(Priority.LOW)
                .setOkHttpClient(withCookie(App.context()));
        if (!fromNetwork) {
            builder.getResponseOnlyIfCached();
        }

        ANRequest request = builder.build();
        ANResponse response = request.executeForObject(Exams.class);

        if (response.isSuccess()) {
            return (Exams) response.getResult();
        } else {
            throw response.getError();
        }
    }

    public static Resp like() throws ANError {
        ANRequest.GetRequestBuilder builder = AndroidNetworking.get(HOST + URL_LIKE)
                .setPriority(Priority.LOW)
                .setOkHttpClient(withCookie(App.context()));

        ANRequest request = builder.build();
        ANResponse response = request.executeForObject(Resp.class);

        if (response.isSuccess()) {
            return (Resp) response.getResult();
        } else {
            throw response.getError();
        }
    }

    public static Resp crash(CrashReportData data) throws ANError {
        String json = null;
        try {
            json = data.toJSON();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (json == null) {
            return null;
        }

        Log.i("CRASH", json);

        ANRequest.PostRequestBuilder builder = AndroidNetworking.post(HOST + URL_CRASH)
                .addBodyParameter("crash_data", json)
                .setPriority(Priority.LOW)
                .setOkHttpClient(withCookie(App.context()));

        ANRequest request = builder.build();
        ANResponse response = request.executeForObject(Resp.class);

        if (response.isSuccess()) {
            return (Resp) response.getResult();
        } else {
            throw response.getError();
        }
    }

    public static Resp uploadFeedback(String message) throws ANError {
        ANRequest.PostRequestBuilder builder = AndroidNetworking.post(HOST + URL_UPLOAD_FEEDBACK)
                .addBodyParameter("message", message)
                .setPriority(Priority.LOW)
                .setOkHttpClient(withCookie(App.context()));

        ANRequest request = builder.build();
        ANResponse response = request.executeForObject(Resp.class);

        if (response.isSuccess()) {
            return (Resp) response.getResult();
        } else {
            throw response.getError();
        }
    }

    public static Feedbacks getFeedbacks() throws ANError {
        ANRequest.GetRequestBuilder builder = AndroidNetworking.get(HOST + URL_GET_FEEDBACKS)
                .setPriority(Priority.LOW)
                .setOkHttpClient(withCookie(App.context()));

        ANRequest request = builder.build();
        ANResponse response = request.executeForObject(Feedbacks.class);

        if (response.isSuccess()) {
            return (Feedbacks) response.getResult();
        } else {
            throw response.getError();
        }
    }

    public static Update checkUpdate() throws ANError {
        ANRequest.GetRequestBuilder builder = AndroidNetworking.get(HOST + URL_CHECK_UPDATE)
                .setPriority(Priority.LOW)
                .setOkHttpClient(withCookie(App.context()));

        ANRequest request = builder.build();
        ANResponse response = request.executeForObject(Update.class);

        if (response.isSuccess()) {
            return (Update) response.getResult();
        } else {
            throw response.getError();
        }
    }

    public static Courses searchCourse(String key) throws ANError {
        ANRequest.GetRequestBuilder builder = AndroidNetworking.get(HOST + URL_SEARCH_COURSE)
                .addQueryParameter("key", key)
                .setPriority(Priority.LOW)
                .setOkHttpClient(withCookie(App.context()));

        ANRequest request = builder.build();
        ANResponse response = request.executeForObject(Courses.class);

        if (response.isSuccess()) {
            return (Courses) response.getResult();
        } else {
            throw response.getError();
        }
    }

    @Deprecated
    public static User login(Context context, String stunum, String password) throws IOException {
        OkHttpClient client = withSaveOnlyCookie(context);
        Request request = new Request.Builder()
                .url(HOST + URL_LOGIN + "?stunum=" + stunum + "&password=" + password)
                .build();
        Response response = client.newCall(request).execute();
        if (response.code() == 200) {
            return new Gson().fromJson(response.body().string(), User.class);
        } else {
            throw new IOException("Response with code " + response.code());
        }
    }

    @Deprecated
    public static Table getTable(Context context) throws IOException {
        OkHttpClient client = withCookie(context);
        Request request = new Request.Builder()
                .url(HOST + URL_GET_TABLE)
                .build();
        Response response = client.newCall(request).execute();
        if (response.code() == 200) {
            return new Gson().fromJson(response.body().string(), Table.class);
        } else {
            throw new IOException("Response with code " + response.code());
        }
    }

    @Deprecated
    public static Grade getGrade(Context context) throws IOException {
        OkHttpClient client = withCookie(context);
        Request request = new Request.Builder()
                .url(HOST + URL_GET_GRADE)
                .build();
        Response response = client.newCall(request).execute();
        if (response.code() == 200) {
            return new Gson().fromJson(response.body().string(), Grade.class);
        } else {
            throw new IOException("Response with code " + response.code());
        }
    }

    @Deprecated
    public static Exams getExams(Context context) throws IOException {
        OkHttpClient client = withCookie(context);
        Request request = new Request.Builder()
                .url(HOST + URL_GET_EXAMS)
                .build();
        Response response = client.newCall(request).execute();
        if (response.code() == 200) {
            return new Gson().fromJson(response.body().string(), Exams.class);
        } else {
            throw new IOException("Response with code " + response.code());
        }
    }
}
