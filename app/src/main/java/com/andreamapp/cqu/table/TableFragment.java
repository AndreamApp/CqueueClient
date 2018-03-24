package com.andreamapp.cqu.table;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.andreamapp.cqu.App;
import com.andreamapp.cqu.R;
import com.andreamapp.cqu.bean.Table;
import com.andreamapp.cqu.utils.API;

import java.io.IOException;

/**
 * Created by Andream on 2018/3/24.
 * Email: andreamapp@qq.com
 * Website: http://andreamapp.com
 */

public class TableFragment extends AppCompatActivity {

    // TODO: Maybe can be replaced by RecyclerView
    TableView mTableView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        final View view = ViewGroup.inflate(this, R.layout.table_fragment, null);
        setContentView(R.layout.table_fragment);

        mTableView = findViewById(R.id.table_view);

        // TODO: Create ViewModel and Repository to replace it
        new AsyncTask<Void, Void, Table>() {

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
                mTableView.setData(table);
            }
        }.execute();

    }
}
