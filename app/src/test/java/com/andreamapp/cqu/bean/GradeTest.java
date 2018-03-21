package com.andreamapp.cqu.bean;

import com.google.gson.Gson;

import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;

import static org.junit.Assert.*;

/**
 * Created by Andream on 2018/3/19.
 */
public class GradeTest {

    @Test
    public void test() throws FileNotFoundException {
        FileReader fr = new FileReader("D:\\AndreamLab\\nodejs\\TableServer\\bean\\getGrade.js");
        Grade grade = new Gson().fromJson(fr, Grade.class);
    }
}