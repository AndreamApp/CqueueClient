package com.andreamapp.cqu.utils;

import com.andreamapp.cqu.bean.Exams;
import com.andreamapp.cqu.bean.Grade;
import com.andreamapp.cqu.bean.Table;
import com.andreamapp.cqu.bean.User;

import org.junit.Test;

/**
 * Created by Andream on 2018/3/20.
 * Email: andreamapp@qq.com
 * Website: http://andreamapp.com
 */
public class APITest {
    @Test
    public void login() throws Exception {
        User user = API.login("20151597", "237231");
        Table table = API.getTable(true);
        Grade grade = API.getGrade(true);
        Exams exams = API.getExams(true);
    }

    @Test
    public void getTable() throws Exception {
        Table table = API.getTable(true);
    }

    @Test
    public void getGrade() throws Exception {
        Grade grade = API.getGrade(true);
    }

    @Test
    public void getExams() throws Exception {
        Exams exams = API.getExams(true);
    }

}