package com.andreamapp.cqu.bean;

import java.util.List;

/**
 * Created by Andream on 2018/10/24.
 * Email: andreamapp@qq.com
 * Website: http://andreamapp.com
 */
public class Courses extends Resp {

    public List<Course> data;

    public class Course {
        public String course_name;
        public String course_code;
        public String credit;
        public String hours_all;
        public String teacher;

        public String class_no;
        public String academy;
        public String class_detail;
        public boolean is_exp;
        public String student_cnt;

        public List<Schedule> schedule;

        public class Schedule{
            public String weeks;
            public String classtime;
            public String classroom;
        }
    }
}
