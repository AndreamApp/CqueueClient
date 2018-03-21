package com.andreamapp.cqu.bean;

import com.google.gson.Gson;

import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;

import static org.junit.Assert.*;

/**
 * Created by Andream on 2018/3/19.
 */
public class TableTest {
    @Test
    public void test() throws FileNotFoundException {
        FileReader fr = new FileReader("D:\\AndreamLab\\nodejs\\TableServer\\bean\\getTable.js");
        Table user = new Gson().fromJson(fr, Table.class);
    }
}