package com.andreamapp.cqu.bean;

import java.util.List;

/**
 * Created by Andream on 2018/4/14.
 * Email: andreamapp@qq.com
 * Website: http://andreamapp.com
 */

public class Feedbacks extends Resp {

    public List<Feedback> data;

    public class Feedback {
        public String message;
        public String stackTrack;
    }
}
