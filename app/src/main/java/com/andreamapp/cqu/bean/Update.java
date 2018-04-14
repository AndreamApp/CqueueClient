package com.andreamapp.cqu.bean;

/**
 * Created by Andream on 2018/4/14.
 * Email: andreamapp@qq.com
 * Website: http://andreamapp.com
 */

public class Update extends Resp {
    public Data data;

    public class Data {
        public int version_code;
        public String version_name;
        public String app_name;
        public String description;
        public String download_url;
    }
}
