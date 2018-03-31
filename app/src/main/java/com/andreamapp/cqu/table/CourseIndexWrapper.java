package com.andreamapp.cqu.table;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.andreamapp.cqu.bean.Resp;
import com.andreamapp.cqu.bean.Table;

import java.util.List;
import java.util.Set;

/**
 * Created by Andream on 2018/3/31.
 * Email: andreamapp@qq.com
 * Website: http://andreamapp.com
 */

public class CourseIndexWrapper implements Resp {
    public Table source;
    public List<Set<CourseIndex>> indexes;

    @NonNull
    @Override
    public boolean status() {
        return source.status();
    }

    @Nullable
    @Override
    public String err() {
        return source.err();
    }

    @Nullable
    @Override
    public String msg() {
        return source.msg();
    }
}
