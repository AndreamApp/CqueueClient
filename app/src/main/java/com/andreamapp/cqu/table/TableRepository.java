package com.andreamapp.cqu.table;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.util.Log;

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
    public static LiveData<CourseIndexWrapper> courseIndexes() {
        MutableLiveData<CourseIndexWrapper> table = new MutableLiveData<>();
        new CourseIndexTask(table).execute();
        return table;
    }

    /**
     * 获取课表的异步任务
     */
    public static class FetchTableTask extends AsyncTask<Void, Void, Table> {
        MutableLiveData<Table> liveData;

        public FetchTableTask(MutableLiveData<Table> table) {
            this.liveData = table;
        }

        @Override
        protected Table doInBackground(Void... voids) {
            Table table = new Table();
            try {
                long start = System.currentTimeMillis();
                table = API.getTable();
                long end = System.currentTimeMillis();
                Log.i("HTTPTest", "" + (end - start) + "ms");
            } catch (ANError e) {
                e.printStackTrace();
                table.status = false;
                table.err = e.getErrorCode() + ": " + e.getErrorBody(); //"网络错误";
            }
            return table;
        }

        @Override
        protected void onPostExecute(Table table) {
            this.liveData.setValue(table);
        }
    }

    /**
     * 获取课表的异步任务
     */
    public static class CourseIndexTask extends AsyncTask<Void, Void, CourseIndexWrapper> {
        MutableLiveData<CourseIndexWrapper> liveData;

        public CourseIndexTask(MutableLiveData<CourseIndexWrapper> table) {
            this.liveData = table;
        }

        @Override
        protected CourseIndexWrapper doInBackground(Void... voids) {
            CourseIndexWrapper indexes = new CourseIndexWrapper();
            try {
                Table table = API.getTable();
                indexes = CourseIndex.generate(table);
            } catch (ANError e) {
                e.printStackTrace();
                indexes.source = new Table();
                indexes.source.status = false;
                switch (e.getErrorCode()) {
                    case 0:
                        indexes.source.err = "网络错误";
                        break;
                    default:
                        indexes.source.err = e.getErrorCode() + ": " + e.getErrorDetail();
                }
            }
            return indexes;
        }

        @Override
        protected void onPostExecute(CourseIndexWrapper indexes) {
            this.liveData.setValue(indexes);
        }
    }
}
