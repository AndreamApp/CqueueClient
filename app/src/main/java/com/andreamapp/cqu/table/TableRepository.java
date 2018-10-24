package com.andreamapp.cqu.table;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.andreamapp.cqu.base.BaseRespTask;
import com.andreamapp.cqu.bean.Courses;
import com.andreamapp.cqu.bean.Table;
import com.andreamapp.cqu.utils.API;
import com.androidnetworking.error.ANError;

/**
 * Created by Andream on 2018/3/24.
 * Email: andreamapp@qq.com
 * Website: http://andreamapp.com
 */

public class TableRepository {

    /**
     * 从网络获取课表信息。要求已经通过API.login()登录教务网并且保存了有效的cookie
     *
     * @return LiveData<Table>
     */
    public static LiveData<Table> fetch() {
        MutableLiveData<Table> table = new MutableLiveData<>();
        new FetchTableTask(table).execute();
        return table;
    }

    /**
     * 从网络获取课表信息。要求已经通过API.login()登录教务网并且保存了有效的cookie
     *
     * @return LiveData<Table>
     */
    public static LiveData<CourseIndexWrapper> courseIndexes(boolean fromNetwork) {
        MutableLiveData<CourseIndexWrapper> table = new MutableLiveData<>();
        new CourseIndexTask(table).execute(fromNetwork);
        return table;
    }

    /**
     * 从网络搜索课程信息。要求已经通过API.login()登录教务网并且保存了有效的cookie
     *
     * @return LiveData<Table>
     */
    public static LiveData<Courses> searchCourse(String key) {
        MutableLiveData<Courses> courses = new MutableLiveData<>();
        new SearchCourseTask(courses).execute(key);
        return courses;
    }

    /**
     * 获取课表的异步任务
     */

    public static class FetchTableTask extends BaseRespTask<Void, Table> {
        FetchTableTask(MutableLiveData<Table> res) {
            super(res);
        }

        @Override
        public Table newInstance() {
            return new Table();
        }

        @Override
        public Table getResult(Void[] args) throws ANError {
            return API.getTable(true);
        }
    }


    public static class CourseIndexTask extends BaseRespTask<Boolean, CourseIndexWrapper> {
        CourseIndexTask(MutableLiveData<CourseIndexWrapper> res) {
            super(res);
        }

        @Override
        public CourseIndexWrapper newInstance() {
            CourseIndexWrapper wrapper = new CourseIndexWrapper();
            wrapper.source = new Table();
            return wrapper;
        }

        @Override
        public CourseIndexWrapper getResult(Boolean[] network) throws ANError {
            Table table = API.getTable(network[0]);
            return new CourseIndexWrapper(table);
        }

        @Override
        public boolean fromNetwork(Boolean[] network) {
            return network[0];
        }

        @Override
        public CourseIndexWrapper redo(Boolean[] network) {
            network[0] = true;
            return super.redo(network);
        }
    }


    public static class SearchCourseTask extends BaseRespTask<String, Courses> {
        SearchCourseTask(MutableLiveData<Courses> res) {
            super(res);
        }

        @Override
        public Courses newInstance() {
            return new Courses();
        }

        @Override
        public Courses getResult(String[] key) throws ANError {
            return API.searchCourse(key[0]);
        }
    }
}
