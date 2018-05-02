package com.andreamapp.cqu.about;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andreamapp.cqu.R;
import com.andreamapp.cqu.base.BaseModelActivity;
import com.andreamapp.cqu.base.BaseRespTask;
import com.andreamapp.cqu.bean.Resp;
import com.andreamapp.cqu.utils.API;
import com.andreamapp.cqu.utils.Alipay;
import com.androidnetworking.error.ANError;

/**
 * Created by Andream on 2018/4/7.
 * Email: andreamapp@qq.com
 * Website: http://andreamapp.com
 */

public class AboutActivity extends BaseModelActivity {

    LinearLayout container;

    public static final String TRELLO = "https://trello.com/b/rT2nOVtT/cqueue";
    public static final String QQ_GROUP = "744097714";
    public static final String MAIL = "andreamapp@qq.com";
    public static final String GITHUB = "https://github.com/AndreamApp/CqueueClient";

    public static final License[] LICENSES = new License[]{
            new License("Fast Android Networking - Amit Shekhar",
                    "https://github.com/amitshekhariitbhu/Fast-Android-Networking", License.APACHE),
            new License("PersistentCookieJar - Francisco José Montiel Navarro",
                    "https://github.com/franmontiel/PersistentCookieJar", License.APACHE),
            new License("acra - ACRA",
                    "https://github.com/ACRA/acra", License.APACHE),
            new License("gson - Google",
                    "https://github.com/google/gson", License.APACHE),
            new License("android support libraries - Google",
                    "https://source.android.com", License.APACHE),
            new License("android arch lifecycle - Google",
                    "https://source.android.com", License.APACHE),
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_activity);

        container = findViewById(R.id.about_container);

        newItem().name(R.string.about_item_update)
                .description("v0.0.1")
                .click(new View.OnClickListener() {
                    @SuppressLint("StaticFieldLeak")
                    @Override
                    public void onClick(final View v) {
                        new CheckUpdateTask(AboutActivity.this).execute();
                    }
                }).add();
        newHeader().name(R.string.about_header_support).add();
        newItem().name(R.string.about_item_like)
                .click(new View.OnClickListener() {
                    @SuppressLint("StaticFieldLeak")
                    @Override
                    public void onClick(final View v) {
                        new BaseRespTask<Void, Resp>(null) {
                            @Override
                            public Resp newInstance() {
                                return new Resp();
                            }

                            @Override
                            public Resp getResult(Void[] voids) throws ANError {
                                return API.like();
                            }

                            @Override
                            protected void onPostExecute(final Resp like) {
                                if (like.status) {
                                    if (like.msg != null) {
                                        TextView des = v.findViewById(R.id.about_item_description);
                                        des.setText(like.msg);
                                        des.setVisibility(View.VISIBLE);
//                                        Snackbar.make(v, like.msg, Snackbar.LENGTH_SHORT)
//                                                .show();
                                    }
                                }
                            }
                        }.execute();
                    }
                }).add();
        newItem().name(R.string.about_item_donate).description(R.string.about_des_donate)
                .click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        donateAlipay("FKX07306I25NIMHDEQUK1F");
                    }
                }).add();

        newHeader().name(R.string.about_header_contact).add();
        newItem().name(R.string.about_item_trello).description(TRELLO)
                .click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openUrl(TRELLO);
                    }
                }).add();
        newItem().name(R.string.about_item_feedback).description(R.string.about_des_feedback)
                .click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FeedbackDialog.newInstance().show(getSupportFragmentManager(), "Feedback");
                    }
                }).add();
        newItem().name(R.string.about_item_qq_group).description(QQ_GROUP)
                .click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        joinQQGroup("PSKn1IvaCJg-c6hHaVmuBwS05wb-X_qn");
                    }
                }).add();
        newItem().name(R.string.about_item_mail).description(MAIL)
                .click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openUrl("mailto://" + MAIL);
                    }
                }).add();
        newItem().name(R.string.about_item_github).description(GITHUB)
                .click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openUrl(GITHUB);
                    }
                }).add();

        newHeader().name(R.string.about_header_os_license).add();
        for (final License license : LICENSES) {
            newItem().name(license.name).description(license.url + "\n" + license.lisence)
                    .click(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openUrl(license.url);
                        }
                    }).add();
        }
    }

    public void openUrl(String url) {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    /****************
     *
     * 发起添加群流程。群号：Cqueue反馈交流群(744097714) 的 key 为： PSKn1IvaCJg-c6hHaVmuBwS05wb-X_qn
     * 调用 joinQQGroup(PSKn1IvaCJg-c6hHaVmuBwS05wb-X_qn) 即可发起手Q客户端申请加群 Cqueue反馈交流群(744097714)
     *
     * @param key 由官网生成的key
     * @return 返回true表示呼起手Q成功，返回false表示呼起失败
     ******************/
    public boolean joinQQGroup(String key) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            startActivity(intent);
            return true;
        } catch (Exception e) {
            // 未安装手Q或安装的版本不支持
            return false;
        }
    }

    public int getVersionCode() {
        try {
            PackageManager pm = getPackageManager();
            PackageInfo info = pm.getPackageInfo(getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private void donateAlipay(String payCode) {
        if (Alipay.hasInstalled(this)) {
            Alipay.start(this, payCode);
        } else {
            Snackbar.make(getWindow().getDecorView(), R.string.about_snack_no_alipay, Snackbar.LENGTH_SHORT).show();
        }
    }

    private Header newHeader() {
        View v = getLayoutInflater().inflate(R.layout.about_header, container, false);
        return new Header(v);
    }

    class Header {
        View view;
        TextView name;

        Header(View v) {
            this.view = v;
            name = v.findViewById(R.id.header);
        }

        Header name(@StringRes int resId) {
            name.setText(resId);
            return this;
        }

        void add() {
            container.addView(view);
        }
    }

    private Item newItem() {
        View v = getLayoutInflater().inflate(R.layout.about_item, container, false);
        return new Item(v);
    }

    class Item {
        View view;
        ImageView icon;
        TextView name;
        TextView description;

        Item(View v) {
            this.view = v;
            icon = v.findViewById(R.id.about_item_icon);
            name = v.findViewById(R.id.about_item_name);
            description = v.findViewById(R.id.about_item_description);
        }

        Item icon(@DrawableRes int resId) {
            if (resId > 0) {
                icon.setImageResource(resId);
                icon.setVisibility(View.VISIBLE);
            }
            return this;
        }

        Item name(@StringRes int resId) {
            name.setText(resId);
            return this;
        }

        Item description(@StringRes int resId) {
            description.setText(resId);
            description.setVisibility(View.VISIBLE);
            return this;
        }

        Item name(CharSequence s) {
            name.setText(s);
            return this;
        }

        Item description(CharSequence s) {
            description.setText(s);
            description.setVisibility(View.VISIBLE);
            return this;
        }

        Item click(View.OnClickListener listener) {
            view.setOnClickListener(listener);
            return this;
        }

        void add() {
            container.addView(view);
        }
    }

    static class License {
        static final String APACHE = "Apache Software License 2.0";
        public static final String MIT = "MIT License";

        String name, url, lisence;

        License(String name, String url, String lisence) {
            this.name = name;
            this.url = url;
            this.lisence = lisence;
        }
    }

    public static class FeedbackDialog extends AppCompatDialogFragment {

        public static FeedbackDialog newInstance() {
            return new FeedbackDialog();
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            View v = getActivity().getLayoutInflater()
                    .inflate(R.layout.about_feedback_dialog, null);
            EditText text = v.findViewById(R.id.feedback_edit_text);
            AlertDialog dialog = new AlertDialog.Builder(getActivity(), getTheme())
                    .setTitle(R.string.about_dialog_title_feedback)
                    .setView(v)
                    .setPositiveButton(R.string.about_dialog_post_feedback, new DialogInterface.OnClickListener() {
                        @SuppressLint("StaticFieldLeak")
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String message = text.getText().toString();
                            new BaseRespTask<String, Resp>(null) {
                                @Override
                                public Resp newInstance() {
                                    return new Resp();
                                }

                                @Override
                                public Resp getResult(String[] message) throws ANError {
                                    return API.uploadFeedback(message[0]);
                                }

                                @Override
                                protected void onPostExecute(Resp resp) {
//                                    if(resp != null && resp.status && getActivity() != null){
//                                        Snackbar.make(getActivity().getWindow().getDecorView(),
//                                                R.string.feedback_snack_success, Snackbar.LENGTH_SHORT)
//                                                .show();
//                                    }
                                }
                            }.execute(message);
                        }
                    })
                    .setNegativeButton(R.string.cancel, null)
                    .create();
            if (dialog.getWindow() != null) {
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            }
            return dialog;
        }
    }
}
