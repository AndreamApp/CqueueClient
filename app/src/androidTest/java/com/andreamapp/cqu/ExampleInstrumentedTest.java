package com.andreamapp.cqu;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.andreamapp.cqu.bean.Table;
import com.andreamapp.cqu.bean.User;
import com.andreamapp.cqu.table.TableRepository;
import com.andreamapp.cqu.utils.API;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        final Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.andreamapp.cqu", appContext.getPackageName());

        User user = API.login(appContext, "20151597", "976655");
        Table table = API.getTable(appContext);
        for (int i = 0; i < 1; i++) {
            MutableLiveData<Table> res = new MutableLiveData<>();
            new TableRepository.FetchTableTask(res).execute();
//            Grade grade = API.getGrade(appContext);
//            Exams exams = API.getExams(appContext);
        }
        int i;
    }
}
