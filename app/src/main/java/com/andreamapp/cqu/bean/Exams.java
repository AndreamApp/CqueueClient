package com.andreamapp.cqu.bean;

import java.util.List;

/**
 * Created by Andream on 2018/2/27.
 * Website: http://andream.com.cn
 * Email: me@andream.com.cn
 */

public class Exams extends Resp {
    //    public boolean status;
//    public String msg;
//    public String err;
    public List<Exam> data;

    /*
        eg.
        {
            "course_name": "科技翻译",
                "course_code": "EDS20502",
                "credit": "2",
                "time_str": "2018-01-17(20周 星期三)14:30-16:30",
                "start_time": "2018-01-17 14:30:00",
                "end_time": "2018-01-17 16:30:00",
                "classroom": "D区D一教学楼D1143",
                "seat": "5"
        }
    */
    public class Exam {
        public String course_name;
        public String course_code;
        public String credit;
        public String time_str;
        public String start_time;
        public String end_time;
        public String classroom;
        public String seat;
    }

}
