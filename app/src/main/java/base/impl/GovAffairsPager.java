package base.impl;


import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

import base.BasePager;

/**
 * 政务
 * Created by clever boy on 2017/4/22.
 */

public class GovAffairsPager extends BasePager {
    public GovAffairsPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        tvTitle.setText("政务");
        TextView view = new TextView(mActivity);
        view.setText("政务");
        view.setTextColor(Color.RED);
        view.setTextSize(22);
        view.setGravity(Gravity.CENTER);
        flContent.addView(view);
    }
}
