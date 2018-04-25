package com.andreamapp.cqu.base;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.andreamapp.cqu.R;
import com.andreamapp.cqu.bean.Resp;
import com.andreamapp.cqu.login.LoginActivity;
import com.andreamapp.cqu.table.LogoutTask;

/**
 * Created by Andream on 2018/3/31.
 * Email: andreamapp@qq.com
 * Website: http://andreamapp.com
 */

public class BaseModelActivity<T extends Resp> extends AppCompatActivity
        implements Observer<T>,
        LogoutTask.Callback {

    @ColorInt
    int primiryColor;
    @ColorInt
    int accentColor;

    public @ColorInt
    int getPrimiryColor() {
        return primiryColor;
    }

    public int getAccentColor() {
        return accentColor;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TypedValue typedValue = new TypedValue();

        getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        primiryColor = typedValue.data;

        getTheme().resolveAttribute(R.attr.colorAccent, typedValue, true);
        accentColor = typedValue.data;
    }

    @Override
    public void onChanged(@Nullable T t) {
        if (t == null) {
            return;
        }
        if (t.status) {
            String msg = t.msg;
            if (msg != null) {
                Snackbar.make(getWindow().getDecorView(), msg, Snackbar.LENGTH_SHORT).show();
            }
        } else {
            String err = t.err;
            if (err != null) {
                Snackbar.make(getWindow().getDecorView(), err, Snackbar.LENGTH_SHORT).show();
                if (err.equals("登录身份已过期")) {
                    new LogoutTask(this).execute();
                }
            }
        }
    }

    public @StyleRes
    int getThemeRes() {
        String themeSetting = getSharedPreferences("settings", MODE_PRIVATE)
                .getString("theme", "Transparent");
        int res = R.style.AppThemeTransparent;
        if ("Transparent".equals(themeSetting)) {
            res = R.style.AppThemeTransparent;
        } else if ("Black".equals(themeSetting)) {
            res = R.style.AppThemeBlack;
        } else if ("Blue".equals(themeSetting)) {
            res = R.style.AppThemeBlue;
        } else if ("Pink".equals(themeSetting)) {
            res = R.style.AppThemePink;
        }
        return res;
    }

    @Override
    public Resources.Theme getTheme() {
        Resources.Theme theme = super.getTheme();
        theme.applyStyle(getThemeRes(), true);
        return theme;
    }

    /**
     * Triggered if login status expired
     *
     * @param res result of logout, true in most case
     */
    @Override
    public void onLogout(boolean res) {
        if (res) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    /**
     * Show the state text with specified msg and clickHandler.
     * Attention that if show=false, the msg and clickHandler would be ignored.
     * Subclass can override this method to add related action as state changed.
     *
     * @param show         show or hide
     * @param resId          the message would show
     * @param clickHandler the action when click state text
     * @return return false if has no TextView with id R.id.state_text
     */
    protected boolean showState(boolean show, int resId, View.OnClickListener clickHandler) {
        TextView stateText = findViewById(R.id.state_text);
        if (stateText != null) {
            if (show) {
                if (resId > 0) {
                    stateText.setText(getResources().getString(resId));
                }
                stateText.setVisibility(View.VISIBLE);
                stateText.setOnClickListener(clickHandler);
            } else {
                stateText.setVisibility(View.GONE);
            }
            return true;
        }
        return false;
    }

    /**
     * Show the state text with specified msg and clickHandler
     *
     * @param resId          the message would show
     * @param clickHandler the action when click state text
     * @return return false if has no TextView with id R.id.state_text
     */
    protected boolean showState(int resId, View.OnClickListener clickHandler) {
        return showState(true, resId, clickHandler);
    }

    /**
     * convenient for showState(CharSequence msg, View.OnClickListener clickHandler)
     *
     * @param resId the message would show
     * @return return false if has no TextView with id R.id.state_text
     */
    protected boolean showState(int resId) {
        return showState(resId, null);
    }

    protected boolean hideState() {
        return showState(false, 0, null);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        if (R.style.AppThemeTransparent == getThemeRes()) {
            overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
        } else {
            overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_half_out);
        }
    }

    /**
     * 使用默认的动画启动Activity
     *
     * @param intent intent
     */
    public void superStartActivity(Intent intent) {
        super.startActivity(intent);
    }

    @Override
    public void finish() {
        super.finish();
        if (R.style.AppThemeTransparent == getThemeRes()) {
            overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
        } else {
            overridePendingTransition(R.anim.slide_left_half_in, R.anim.slide_right_out);
        }
    }

    /**
     * 使用默认的动画结束Activity
     */
    public void superFinish() {
        super.finish();
    }
}
