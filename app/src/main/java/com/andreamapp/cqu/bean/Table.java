package com.andreamapp.cqu.bean;

import java.util.List;

/**
 * Created by Andream on 2018/2/27.
 * Website: http://andream.com.cn
 * Email: me@andream.com.cn
 */

public class Table extends Resp {
    //    public boolean status;
//    public String msg;
//    public String err;
    public String semester_start_date;
    public List<Course> data;

    /*
    eg.
        {
            "course_name": "毛泽东思想和中国特色社会主义理论体系概论",
            "course_code": "IPT10400",
            "credit": "3.00",
            "hours_all": "48.0",
            "hours_teach": "48.0",
            "hours_practice": "0.0",
            "teacher": "张运清",
            "schedule": [{
                "weeks": "1,3-4,6-17",
                "classtime": "一[5-6节]",
                "classroom": "D1331"
            },
            {
                "weeks": "19",
                "classtime": "一[9-10节]",
                "classroom": "D1134"
            },
            {
                "weeks": "1,3,7,9,11,13,15,17",
                "classtime": "四[3-4节]",
                "classroom": "D1331"
            },
            {
                "weeks": "19",
                "classtime": "四[5-8节]",
                "classroom": "期末考试"
            }]
        }
    */
    public class Course {
        public String course_name;
        public String course_code;
        public String credit;
        public String hours_all;
        public String hours_teach;
        public String hours_practice;
        public String teacher;
        public List<Schedule> schedule;

        public class Schedule{
            public String weeks;
            public String classtime;
            public String classroom;
        }
    }

}
