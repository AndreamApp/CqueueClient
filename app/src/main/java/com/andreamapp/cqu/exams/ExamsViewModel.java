package com.andreamapp.cqu.exams;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.andreamapp.cqu.bean.Exams;

/**
 * Created by Andream on 2018/3/27.
 * Email: andreamapp@qq.com
 * Website: http://andreamapp.com
 */

public class ExamsViewModel extends ViewModel {

    LiveData<Exams> data;

    LiveData<Exams> fetch() {
        this.data = ExamsRepository.fetch();
        return this.data;
    }
}
