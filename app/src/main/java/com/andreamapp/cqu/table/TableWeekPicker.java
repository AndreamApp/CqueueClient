package com.andreamapp.cqu.table;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by Andream on 2018/3/25.
 * Email: andreamapp@qq.com
 * Website: http://andreamapp.com
 */

public class TableWeekPicker extends RelativeLayout {
    public TableWeekPicker(Context context) {
        super(context);
    }

    public TableWeekPicker(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TableWeekPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public TableWeekPicker(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

}
