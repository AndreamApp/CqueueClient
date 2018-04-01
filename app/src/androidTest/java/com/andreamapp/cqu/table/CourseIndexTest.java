package com.andreamapp.cqu.table;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.andreamapp.cqu.bean.Table;
import com.andreamapp.cqu.bean.User;
import com.andreamapp.cqu.utils.API;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Andream on 2018/3/24.
 * Email: andreamapp@qq.com
 * Website: http://andreamapp.com
 */
public class CourseIndexTest {

    @Test
    public void test() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.andreamapp.cqu", appContext.getPackageName());

        User user = API.login("20151597", "976655");
        Table table = API.getTable();
        for (int i = 0; i < 100; i++) {
            long start = System.currentTimeMillis();
            CourseIndexWrapper res = CourseIndex.generate(table);
            long end = System.currentTimeMillis();
            long use = end - start;
            System.out.print(use);
        }

    }
}