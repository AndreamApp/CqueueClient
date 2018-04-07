package com.andreamapp.cqu.table;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.View;
import android.widget.TextView;

import com.andreamapp.cqu.R;
import com.andreamapp.cqu.bean.User;

/**
 * Created by Andream on 2018/4/7.
 * Email: andreamapp@qq.com
 * Website: http://andreamapp.com
 */

public class ProfileDialogFragment extends AppCompatDialogFragment {

    User user;

    /**
     * @deprecated Use {@link #newInstance(User user)} instead
     */
    @Deprecated
    public ProfileDialogFragment() {
    }

    public static ProfileDialogFragment newInstance(User user) {
        ProfileDialogFragment fragment = new ProfileDialogFragment();
//        Bundle args = new Bundle();
//        args.put
//        fragment.setArguments(args);
        fragment.user = user;
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v;

        // avoid null pointer exception
        try {
            v = getActivity().getLayoutInflater()
                    .inflate(R.layout.user_profile_dialog, null, false);
            ((TextView) (v.findViewById(R.id.user_profile_name))).setText(user.data.name);
            ((TextView) (v.findViewById(R.id.user_profile_academy))).setText(user.data.academy);
            ((TextView) (v.findViewById(R.id.user_profile_stunum))).setText(user.data.stunum);
        } catch (Exception e) {
            e.printStackTrace();
            v = new View(getActivity());
        }
        return new AlertDialog.Builder(getActivity(), getTheme())
                .setView(v)
                .setPositiveButton(R.string.logout, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (getListener() != null) {
                            getListener().onUserLogout();
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
        void onUserLogout();
    }
}