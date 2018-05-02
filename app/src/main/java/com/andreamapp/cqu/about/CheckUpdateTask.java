package com.andreamapp.cqu.about;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.andreamapp.cqu.R;
import com.andreamapp.cqu.base.BaseRespTask;
import com.andreamapp.cqu.bean.Update;
import com.andreamapp.cqu.utils.API;
import com.androidnetworking.error.ANError;

import java.lang.ref.WeakReference;

/**
 * Created by Andream on 2018/5/2.
 * Email: andreamapp@qq.com
 * Website: http://andreamapp.com
 */

public class CheckUpdateTask extends BaseRespTask<Void, Update> {
    private WeakReference<Activity> weakContext;
    boolean mShowNoUpdate;

    public CheckUpdateTask(@NonNull Activity context, boolean showNoUpdate) {
        super(null);
        this.weakContext = new WeakReference<>(context);
        this.mShowNoUpdate = showNoUpdate;
    }

    public CheckUpdateTask(@NonNull Activity context) {
        this(context, true);
    }

    @Override
    public Update newInstance() {
        return new Update();
    }

    @Override
    public Update getResult(Void[] voids) throws ANError {
        return API.checkUpdate();
    }

    @Override
    protected void onPostExecute(final Update update) {
        if (weakContext == null || weakContext.get() == null) {
            return;
        }
        if (update.status && update.data != null) {
            Activity context = weakContext.get();
            View v = context.getWindow().getDecorView();
            if (update.data.version_code > getVersionCode()) {
                Snackbar.make(v, update.data.description, Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.about_action_update, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                openUrl(update.data.download_url);
                            }
                        }).show();
            } else if (mShowNoUpdate) {
                Snackbar.make(v, R.string.about_no_update, Snackbar.LENGTH_SHORT)
                        .show();
            }
        }

    }

    public int getVersionCode() {
        if (weakContext == null || weakContext.get() == null) {
            return 0;
        }
        Context context = weakContext.get();
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo info = pm.getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void openUrl(String url) {
        if (weakContext == null || weakContext.get() == null) {
            return;
        }
        Activity context = weakContext.get();
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }
}
