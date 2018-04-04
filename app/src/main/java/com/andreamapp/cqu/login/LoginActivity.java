package com.andreamapp.cqu.login;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.andreamapp.cqu.R;
import com.andreamapp.cqu.base.BaseModelActivity;
import com.andreamapp.cqu.bean.User;
import com.andreamapp.cqu.table.TableFragment;
import com.andreamapp.cqu.utils.API;

/**
 * Created by Andream on 2018/2/27.
 */

public class LoginActivity extends BaseModelActivity<User> {

    private ProgressBar mLoginProgress;
    private View mLoginForm;

    private EditText mStudentNumberEditor;
    private EditText mPasswordEditor;
    private Button mLoginBtn;

    private UserViewModel mUserViewModel;

    private boolean isLogining;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (API.cookieExpired(this)) {
            setContentView(R.layout.activity_login);
            initViews();
            mLoginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    attemptLogin();
                }
            });

            mUserViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        } else {
            // cookie not expired, skip login
            startActivity(new Intent(LoginActivity.this, TableFragment.class));
        }
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
        if (isLogining) {
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
            isLogining = true;
            hideInputMethod();
            showProgress(true);
            mUserViewModel.fetch(studentNum, password).observe(this, this);
        }
    }

    @Override
    public void onChanged(@Nullable User user) {
        super.onChanged(user);
        isLogining = false;
        if (user != null) {
            if (user.status()) {
                startActivity(new Intent(LoginActivity.this, TableFragment.class));
            }
        }
    }

    /**
     * 显示/关闭进度条
     * */
    private void showProgress(boolean state){
        mLoginProgress.setVisibility(state ? View.VISIBLE : View.GONE);
        mLoginForm.setVisibility(state ? View.GONE : View.VISIBLE);
    }

    private void hideInputMethod() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 判断密码是否有效，返回错误信息。无错误则返回null
     * 密码长度必须在[6,16]位之间
     * */
    private String checkPassword(String password){
        if(TextUtils.isEmpty(password)){
            return getString(R.string.login_error_input_pass);
        }
        String error = null;
        final int L = password.length();
        if(L < 6){
            error = getString(R.string.login_error_shorter_6);
        }
        else if(L > 16){
            error = getString(R.string.login_error_longer_16);
        }
        return error;
    }

    /**
     * 判断学号是否有效，返回错误信息。无错误则返回null
     * 因为暂时只支持本科生账号，所以要求必须8位数字
     * */
    private String checkStudentNum(String studentNum){
        if(TextUtils.isEmpty(studentNum)){
            return getString(R.string.login_error_input_stunum);
        }
        String error = null;
        final int L = studentNum.length();
        if(L < 8){
            error = getString(R.string.login_error_stunum_too_short);
        }
        else if(L > 8){
            error = getString(R.string.login_error_stunum_too_long);
        }
        else{
            for(int i = 0; i < L; i++){
                char c = studentNum.charAt(i);
                if(!(c >= '0' && c <= '9')){
                    error = getString(R.string.login_error_num_only);
                    break;
                }
            }
        }
        return error;
    }

}
