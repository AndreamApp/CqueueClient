package com.andreamapp.cqu.table;

import android.support.annotation.NonNull;

import com.andreamapp.cqu.bean.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Andream on 2018/3/24.
 * Email: andreamapp@qq.com
 * Website: http://andreamapp.com
 */

public class CourseIndex implements Comparable<CourseIndex> {
    int weekday;
    int sectionStart;
    int sectionEnd;
    String classroom;
    Table.Course course;

    private static void parseSchedule(Table.Course course, Table.Course.Schedule schedule, List<Set<CourseIndex>> res) {
        CourseIndex index = new CourseIndex();

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
        index.sectionStart = Integer.parseInt(time[1]);
        index.sectionEnd = Integer.parseInt(time.length > 2 ? time[2] : time[1]);
        index.classroom = schedule.classroom;
        index.course = course;

        for (String segment : schedule.weeks.split(",")) {
            String[] period = segment.split("-");
            if (period.length == 2) {
                int weekStart = Integer.parseInt(period[0]);
                int weekEnd = Integer.parseInt(period[1]);
                for (int i = weekStart; i < weekEnd; i++) {
                    res.get(i).add(index);
                }
            }
        }
    }

    public static List<Set<CourseIndex>> generate(Table table) {
        List<Set<CourseIndex>> res = new ArrayList<>(25);
        for (int i = 0; i < 25; i++) {
            res.add(new TreeSet<CourseIndex>());
        }

        List<Table.Course> courses = table.data;
        if (courses != null && courses.size() > 0) {
            for (Table.Course course : courses) {
                for (Table.Course.Schedule schedule : course.schedule) {
                    parseSchedule(course, schedule, res);
                }
            }
        }
        return res;
    }

    @Override
    public int compareTo(@NonNull CourseIndex o) {
        if (this.weekday == o.weekday) {
            if (this.sectionStart == o.sectionStart) {
                return this.sectionEnd - o.sectionEnd;
            }
            return this.sectionStart - o.sectionStart;
        }
        return this.weekday - o.weekday;
    }
}
