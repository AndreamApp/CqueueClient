package com.andreamapp.cqu.table;

import com.andreamapp.cqu.bean.Resp;
import com.andreamapp.cqu.bean.Table;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Created by Andream on 2018/3/31.
 * Email: andreamapp@qq.com
 * Website: http://andreamapp.com
 */

public class CourseIndexWrapper extends Resp {
    public Table source;
    public List<Set<CourseIndex>> indexes;

    public Calendar getSemesterStartDate(){
        Calendar calendar = null;
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).parse(source.semester_start_date);
            calendar = Calendar.getInstance();
            calendar.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar;
    }
}
