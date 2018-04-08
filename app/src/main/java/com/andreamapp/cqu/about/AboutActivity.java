package com.andreamapp.cqu.about;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andreamapp.cqu.R;
import com.andreamapp.cqu.base.BaseModelActivity;
import com.andreamapp.cqu.utils.Alipay;

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

        newHeader().name(R.string.about_header_support).add();
        newItem().name(R.string.about_item_like).add();
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
}
