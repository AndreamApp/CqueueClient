package com.andreamapp.cqu.login;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.andreamapp.cqu.R;
import com.andreamapp.cqu.bean.User;
import com.andreamapp.cqu.utils.API;

import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * Created by Andream on 2018/2/27.
 * Website: http://andream.com.cn
 * Email: me@andream.com.cn
 */

public class LoginActivity extends AppCompatActivity{

    private ProgressBar mLoginProgress;
    private View mLoginForm;

    private EditText mStudentNumberEditor;
    private EditText mPasswordEditor;
    private Button mLoginBtn;

    private LoginJWCTask mLoginTask;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });
    }

    private void initViews(){
        mLoginProgress = findViewById(R.id.login_progress);
        mLoginForm = findViewById(R.id.login_form);
        mStudentNumberEditor = findViewById(R.id.login_student_number);
        mPasswordEditor = findViewById(R.id.login_password);
        mLoginBtn = findViewById(R.id.login_btn);
    }

    /**
     * 请求登录，会先在本地检查参数
     * */
    private void attemptLogin(){
        if(mLoginTask != null){
            return;
        }

        // 登录参数
        String studentNum = mStudentNumberEditor.getText().toString();
        String password = mPasswordEditor.getText().toString();

        // 参数检查
        String studentNumError = checkStudentNum(studentNum);
        String passwordError = checkPassword(password);
        mStudentNumberEditor.setError(studentNumError);
        mPasswordEditor.setError(passwordError);

        // 发起登录
        if(studentNumError != null){
            mStudentNumberEditor.requestFocus();
        }
        else if(passwordError != null){
            mPasswordEditor.requestFocus();
        }
        else{
            mLoginTask = new LoginJWCTask(this);
            mLoginTask.execute(studentNum, password);
        }
    }

    /**
     * 显示/关闭进度条
     * */
    private void showProgress(boolean state){
        mLoginProgress.setVisibility(state ? View.VISIBLE : View.GONE);
        mLoginForm.setVisibility(state ? View.GONE : View.VISIBLE);
    }

    /**
     * 判断密码是否有效，返回错误信息。无错误则返回null
     * 密码长度必须在[6,16]位之间
     * */
    private String checkPassword(String password){
        if(TextUtils.isEmpty(password)){
            return "请输入密码";
        }
        String error = null;
        final int L = password.length();
        if(L < 6){
            error = "密码不能少于6位";
        }
        else if(L > 16){
            error = "密码不能大于16位";
        }
        return error;
    }

    /**
     * 判断学号是否有效，返回错误信息。无错误则返回null
     * 因为暂时只支持本科生账号，所以要求必须8位数字
     * */
    private String checkStudentNum(String studentNum){
        if(TextUtils.isEmpty(studentNum)){
            return "请输入学号";
        }
        String error = null;
        final int L = studentNum.length();
        if(L < 8){
            error = "学号太短了！";
        }
        else if(L > 8){
            error = "学号太长了！";
        }
        else{
            for(int i = 0; i < L; i++){
                char c = studentNum.charAt(i);
                if(!(c >= '0' && c <= '9')){
                    error = "学号只能包含数字";
                    break;
                }
            }
        }
        return error;
    }

    /**
     * 登录教务网，需要参数：学号、密码，返回User对象，如果出现异常则返回null
     * */
    public static class LoginJWCTask extends AsyncTask<String, Void, User> {
        private WeakReference<LoginActivity> mActivity;

        public LoginJWCTask(LoginActivity activity){
            mActivity = new WeakReference<LoginActivity>(activity);
        }

        @Override
        protected void onPreExecute() {
            LoginActivity activity = mActivity.get();
            if(activity != null){
                activity.showProgress(true);
            }
            Log.i("Test", "Pre: " + System.currentTimeMillis() / 1000);
        }

        @Override
        protected User doInBackground(String... args) {
            User user = null;
            LoginActivity activity = mActivity.get();
            if(activity != null){
                try {
                    user = API.login(activity, args[0], args[1]);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else{
                // Activity stoped, do nothing
            }
            return user;
        }

        @Override
        protected void onPostExecute(User user) {
            // Save data
            // Switch to MainActivity
//            Toast.makeText(mActivity.get(), user.data.name, Toast.LENGTH_LONG).show();
            mActivity.get().mLoginTask = null;
            mActivity.get().showProgress(false);
        }

        @Override
        protected void onCancelled() {
            mActivity.get().mLoginTask = null;
        }
    }

}
