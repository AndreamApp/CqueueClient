package com.andreamapp.cqu.table;

import com.andreamapp.cqu.bean.Resp;
import com.andreamapp.cqu.bean.Table;

import java.util.List;
import java.util.Set;

/**
 * Created by Andream on 2018/3/31.
 * Email: andreamapp@qq.com
 * Website: http://andreamapp.com
 */

public class CourseIndexWrapper extends Resp {
    public Table source;
    public List<Set<CourseIndex>> indexes;
}
