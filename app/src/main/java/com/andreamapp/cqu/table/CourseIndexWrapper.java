package com.andreamapp.cqu.table;

import com.andreamapp.cqu.bean.Resp;
import com.andreamapp.cqu.bean.Table;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Andream on 2018/3/31.
 * Email: andreamapp@qq.com
 * Website: http://andreamapp.com
 */

public class CourseIndexWrapper extends Resp {

    public static final int MAX_WEEK_NUM = 25;

    public Table source;
    public List<Set<CourseIndex>> indexes;

    public CourseIndexWrapper() {
    }

    public CourseIndexWrapper(Table table) {
        generate(table);
    }

    private void parseSchedule(Table.Course course, Table.Course.Schedule schedule, List<Set<CourseIndex>> res) {
        CourseIndex index = new CourseIndex();

        // 解析在星期几上课
        String[] time = schedule.classtime.replaceAll("[\\[\\]\\-节]", " ").split(" ");
        int weekday = 0;
        switch (time[0]) {
            case "一":
                weekday = 1;
                break;
            case "二":
                weekday = 2;
                break;
            case "三":
                weekday = 3;
                break;
            case "四":
                weekday = 4;
                break;
            case "五":
                weekday = 5;
                break;
            case "六":
                weekday = 6;
                break;
            case "日":
                weekday = 7;
                break;
        }
        index.weekday = weekday - 1;
        // 上课节次
        index.sectionStart = Integer.parseInt(time[1]);
        index.sectionEnd = Integer.parseInt(time.length > 2 ? time[2] : time[1]);
        // 上课教室
        index.classroom = schedule.classroom;
        index.course = course;

        // 上课周次
        for (String segment : schedule.weeks.split(",")) {
            String[] period = segment.split("-");
            if (period.length == 2) {
                int weekStart = Integer.parseInt(period[0]);
                int weekEnd = Integer.parseInt(period[1]);
                // [weekStart, weekEnd], inclusive
                for (int i = weekStart; i <= weekEnd; i++) {
                    res.get(i).add(index);
                }
            } else if (period.length == 1) {
                int week = Integer.parseInt(period[0]);
                res.get(week).add(index);
            }
        }
    }


    public void generate(Table table) {
        List<Set<CourseIndex>> res = new ArrayList<>(MAX_WEEK_NUM + 1);
        for (int i = 0; i < MAX_WEEK_NUM + 1; i++) {
            res.add(new TreeSet<CourseIndex>());
            for(int w = 0; w < 7; w++){
                CourseIndex separator = new CourseIndex();
                separator.weekday = w;
                separator.course = null;
                separator.classroom = null;
                separator.sectionStart = 11;
                separator.sectionEnd = -1;
                res.get(i).add(separator);
            }
        }

        List<Table.Course> courses = table.data;
        if (courses != null && courses.size() > 0) {
            for (Table.Course course : courses) {
                for (Table.Course.Schedule schedule : course.schedule) {
                    parseSchedule(course, schedule, res);
                }
            }
        }

        this.source = table;
        this.status = table.status;
        this.msg = table.msg;
        this.err = table.err;
        this.indexes = res;
    }


    public Calendar getSemesterStartDate(){
        if(source.semester_start_date == null) {
            return null;
        }
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
