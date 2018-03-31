package com.andreamapp.cqu;

import org.junit.Test;

import java.util.Calendar;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {

        Calendar c = Calendar.getInstance();
        c.set(2018, Calendar.MARCH, 5, 0, 0, 0);

        long a = Calendar.getInstance().getTimeInMillis();
        long b = c.getTimeInMillis();
        long d = System.currentTimeMillis();

        long days = (a - b) / (24 * 60 * 60 * 1000);
        long weekNum = days / 7;

    }
}