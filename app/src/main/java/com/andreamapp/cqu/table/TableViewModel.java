package com.andreamapp.cqu.table;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.andreamapp.cqu.bean.Table;

import java.util.List;
import java.util.Set;

/**
 * Created by Andream on 2018/3/24.
 * Email: andreamapp@qq.com
 * Website: http://andreamapp.com
 */

public class TableViewModel extends ViewModel {
    private LiveData<Table> data;
    private LiveData<List<Set<CourseIndex>>> indexes;

    LiveData<Table> fetch() {
        this.data = TableRepository.fetch();
        return this.data;
    }

    LiveData<List<Set<CourseIndex>>> fetchIndexes() {
        this.indexes = TableRepository.courseIndexes();
        return this.indexes;
    }

    LiveData<Table> get() {
        return this.data;
    }


    LiveData<List<Set<CourseIndex>>> getIndexes() {
        return this.indexes;
    }

}
