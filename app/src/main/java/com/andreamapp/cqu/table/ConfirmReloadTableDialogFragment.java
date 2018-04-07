package com.andreamapp.cqu.table;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;

import com.andreamapp.cqu.R;

/**
 * Created by Andream on 2018/4/7.
 * Email: andreamapp@qq.com
 * Website: http://andreamapp.com
 */

public class ConfirmReloadTableDialogFragment extends AppCompatDialogFragment {

    /**
     * @deprecated Use {@link #newInstance()} instead
     */
    @Deprecated
    public ConfirmReloadTableDialogFragment() {
    }

    public static ConfirmReloadTableDialogFragment newInstance() {
        return new ConfirmReloadTableDialogFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity(), getTheme())
                .setTitle(R.string.table_reload_confirm_title)
                .setMessage(R.string.table_reload_confirm)
                .setPositiveButton(R.string.reload, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (getListener() != null) {
                            getListener().onReloadTable();
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create();
    }

    @Nullable
    public Listener getListener() {
        return (Listener) getActivity();
    }

    public interface Listener {
        void onReloadTable();
    }
}