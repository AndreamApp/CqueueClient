package com.andreamapp.cqu.bean;

/**
 * Created by Andream on 2018/2/27.
 * Website: http://andream.com.cn
 * Email: me@andream.com.cn
 */

public class User extends Resp {
//    public boolean status;
//    public String msg;
//    public String err;

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
