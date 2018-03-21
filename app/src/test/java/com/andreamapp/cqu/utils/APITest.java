package com.andreamapp.cqu.utils;

import com.andreamapp.cqu.bean.Exams;
import com.andreamapp.cqu.bean.Grade;
import com.andreamapp.cqu.bean.Table;
import com.andreamapp.cqu.bean.User;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Andream on 2018/3/20.
 */
public class APITest {
    @Test
    public void login() throws Exception {
        User user = API.login(null, "20151597", "976655");
    }

    @Test
    public void getTable() throws Exception {
        Table table = API.getTable(null);
    }

    @Test
    public void getGrade() throws Exception {
        Grade grade = API.getGrade(null);
    }

    @Test
    public void getExams() throws Exception {
        Exams exams = API.getExams(null);
    }

}