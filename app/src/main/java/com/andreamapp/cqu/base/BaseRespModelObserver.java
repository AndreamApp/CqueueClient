package com.andreamapp.cqu.base;

import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import com.andreamapp.cqu.bean.Resp;

/**
 * Created by Andream on 2018/3/31.
 * Email: andreamapp@qq.com
 * Website: http://andreamapp.com
 */

public class BaseRespModelObserver<T extends Resp> implements Observer<T> {
    @Override
    public void onChanged(@Nullable T t) {

    }
}
