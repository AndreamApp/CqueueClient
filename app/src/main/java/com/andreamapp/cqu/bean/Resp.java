package com.andreamapp.cqu.bean;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Andream on 2018/3/31.
 * Email: andreamapp@qq.com
 * Website: http://andreamapp.com
 */

public interface Resp {

    /**
     * @return the status of network response
     */
    @NonNull
    boolean status();

    /**
     * @return the error information of network, usually need it when status is false
     */
    @Nullable
    String err();

    /**
     * @return the toast information of network, usually need it when status is true
     */
    @Nullable
    String msg();
}
