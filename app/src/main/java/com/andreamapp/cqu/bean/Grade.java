package com.andreamapp.cqu.bean;

import android.support.annotation.Nullable;

import java.util.List;

/**
 * Created by Andream on 2018/2/27.
 * Website: http://andream.com.cn
 * Email: me@andream.com.cn
 */

public class Grade implements Resp {
    public boolean status;
    public String msg;
    public String err;
    public List<SemesterGrade> data;

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

    /*
    eg.
        {
            "semester": "学年学期：2016-2017学年第一学期",
            "data": [{
                "course_name": "形势与政策（1）",
                "course_code": "IPT10000",
                "credit": "0.50",
                "grade": "91.00"
            },
            {
                "course_name": "思想道德修养与法律基础",
                "course_code": "IPT10100",
                "credit": "2.00",
                "grade": "85.00"
            }]
        }
    */
    public class SemesterGrade {
        public String semester;
        public String gpa;
        public List<CourseGrade> data;
        
        public class CourseGrade {
            public String course_name;
            public String course_code;
            public String credit;
            public String grade;
        }
    }

}
