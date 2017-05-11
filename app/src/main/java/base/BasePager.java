package base;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.cleverboy.myapplication.GuideActivity;
import com.example.cleverboy.myapplication.MainActivity;
import com.example.cleverboy.myapplication.R;

/**
 * Created by clever boy on 2017/4/22.
 */

public abstract class BasePager {

    public Activity mActivity;
    public View mRootView;
    public TextView tvTitle;
    public ImageButton btnMenu;
    public FrameLayout flContent;

    public BasePager(Activity activity) {
        mActivity = activity;
        initView();
    }

    /**
     * 初始化布局
     */
    public void initView() {
        mRootView = View.inflate(mActivity, R.layout.base_pager, null);
        tvTitle = (TextView) mRootView.findViewById(R.id.tv_title);
        btnMenu = (ImageButton) mRootView.findViewById(R.id.btn_menu);
        flContent = (FrameLayout) mRootView.findViewById(R.id.fl_content);

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle();
            }
        });
    }
    /**
     * 初始化数据
     */
    public abstract void initData();
    private void toggle(){
        MainActivity mainUI = (MainActivity) mActivity;
        mainUI.getSlidingMenu().toggle();
    }
}
