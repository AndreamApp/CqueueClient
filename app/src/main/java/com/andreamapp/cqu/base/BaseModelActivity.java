package com.andreamapp.cqu.base;

import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import com.andreamapp.cqu.bean.Resp;

/**
 * Created by Andream on 2018/3/31.
 * Email: andreamapp@qq.com
 * Website: http://andreamapp.com
 */

public class BaseModelActivity<T extends Resp> extends AppCompatActivity implements Observer<T> {
    @Override
    public void onChanged(@Nullable T t) {
        if (t.status()) {
            String msg = t.msg();
            if (msg != null) {
                Snackbar.make(getWindow().getDecorView(), msg, Snackbar.LENGTH_SHORT).show();
            }
        } else {
            String err = t.err();
            if (err != null) {
                Snackbar.make(getWindow().getDecorView(), err, Snackbar.LENGTH_SHORT).show();
            }
        }
    }
}
