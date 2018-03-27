package com.andreamapp.cqu.grade;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.andreamapp.cqu.bean.Grade;

/**
 * Created by Andream on 2018/3/27.
 * Email: andreamapp@qq.com
 * Website: http://andreamapp.com
 */

public class GradeViewModel extends ViewModel {
    LiveData<Grade> data;

    LiveData<Grade> fetch() {
        this.data = GradeRepository.fetch();
        return this.data;
    }
}
