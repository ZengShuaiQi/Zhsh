package base;

import android.app.Activity;
import android.view.View;

/**
 * Created by clever boy on 2017/4/26.
 */

public abstract class BaseMenuDetatilPager {

    public Activity mActivity;
    public View mRootView;

    public BaseMenuDetatilPager(Activity activity){
        mActivity = activity;
        mRootView = initView();
    }

    public abstract View initView();

    public void initData(){}
}
