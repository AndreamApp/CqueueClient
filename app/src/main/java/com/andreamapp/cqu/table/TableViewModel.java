package com.andreamapp.cqu.table;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.andreamapp.cqu.bean.Table;

/**
 * Created by Andream on 2018/3/24.
 * Email: andreamapp@qq.com
 * Website: http://andreamapp.com
 */

public class TableViewModel extends ViewModel {
    private LiveData<Table> data;
    private LiveData<CourseIndexWrapper> indexes;

    LiveData<Table> fetch() {
        this.data = TableRepository.fetch();
        return this.data;
    }

    LiveData<CourseIndexWrapper> fetchIndexes() {
        this.indexes = TableRepository.courseIndexes();
        return this.indexes;
    }

    LiveData<Table> get() {
        return this.data;
    }


    LiveData<CourseIndexWrapper> getIndexes() {
        return this.indexes;
    }

}
