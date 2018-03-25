package com.andreamapp.cqu.table;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;

import com.andreamapp.cqu.App;
import com.andreamapp.cqu.bean.Table;
import com.andreamapp.cqu.utils.API;

import java.io.IOException;
import java.util.List;
import java.util.Set;

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
    public static LiveData<List<Set<CourseIndex>>> courseIndexes() {
        MutableLiveData<List<Set<CourseIndex>>> table = new MutableLiveData<>();
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
            Table table = null;
            try {
                table = API.getTable(App.context());
            } catch (IOException e) {
                e.printStackTrace();
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
    public static class CourseIndexTask extends AsyncTask<Void, Void, List<Set<CourseIndex>>> {
        MutableLiveData<List<Set<CourseIndex>>> liveData;

        public CourseIndexTask(MutableLiveData<List<Set<CourseIndex>>> table) {
            this.liveData = table;
        }

        @Override
        protected List<Set<CourseIndex>> doInBackground(Void... voids) {
            List<Set<CourseIndex>> indexes = null;
            try {
                Table table = API.getTable(App.context());
                indexes = CourseIndex.generate(table);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return indexes;
        }

        @Override
        protected void onPostExecute(List<Set<CourseIndex>> indexes) {
            this.liveData.setValue(indexes);
        }
    }
}
