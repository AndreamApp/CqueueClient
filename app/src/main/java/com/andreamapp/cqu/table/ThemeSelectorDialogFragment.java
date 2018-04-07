package com.andreamapp.cqu.table;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDialogFragment;

import com.andreamapp.cqu.App;
import com.andreamapp.cqu.R;

/**
 * Created by Andream on 2018/4/7.
 * Email: andreamapp@qq.com
 * Website: http://andreamapp.com
 */

public class ThemeSelectorDialogFragment extends AppCompatDialogFragment {

    public static ThemeSelectorDialogFragment newInstance() {
        return new ThemeSelectorDialogFragment();
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity(), getTheme())
                .setItems(R.array.themes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String theme = "Transparent";
                        switch (which) {
                            case 0:
                                theme = "Transparent";
                                break;
                            case 1:
                                theme = "Pink";
                                break;
                            case 2:
                                theme = "Blue";
                                break;
                            case 3:
                                theme = "Black";
                                break;
                        }
                        App.context().getSharedPreferences("settings", Context.MODE_PRIVATE)
                                .edit()
                                .putString("theme", theme)
                                .apply();
                        if (getListener() != null) {
                            getListener().onThemeChanged(theme);
                        }
                    }
                })
                .create();
    }

    public Listener getListener() {
        return (Listener) getActivity();
    }

    public interface Listener {
        void onThemeChanged(String theme);
    }
}
