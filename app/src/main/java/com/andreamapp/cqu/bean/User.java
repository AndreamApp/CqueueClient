package com.andreamapp.cqu.bean;

import android.support.annotation.Nullable;

/**
 * Created by Andream on 2018/2/27.
 * Website: http://andream.com.cn
 * Email: me@andream.com.cn
 */

public class User implements Resp {
    public boolean status;
    public String msg;
    public String err;

    @Override
    public boolean status() {
        return status;
    }

    @Nullable
    @Override
    public String err() {
        return err;
    }

    @Nullable
    @Override
    public String msg() {
        return msg;
    }

    public UserData data;

    public class UserData {
        public String stunum;
        public String name;
        public String sex;
        public String birthday;
        public String nation;
        public String academy;
        public String class_name;
        public String tel;
    }
}
