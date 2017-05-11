package com.example.cleverboy.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import base.impl.ContentFragment;
import base.impl.LeftMenuFragment;

/**
 * Created by clever boy on 2017/3/29.
 */

public class MainActivity extends SlidingFragmentActivity{
    private static final String TAG_CONTENT = "TAG_CONTENT";
    private static final String TAG_LEFT_MENU = "TAG_LEFT_MENU";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        //添加侧边栏
        setBehindContentView(R.layout.left_munu);
        SlidingMenu slidingMenu = getSlidingMenu();
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        slidingMenu.setBehindOffset(600);
        initFragment();
    }
    private void initFragment(){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction =fm.beginTransaction();
        //將幀佈局替換為對應的fragment
        transaction.replace(R.id.fl_content,new ContentFragment(),TAG_CONTENT);
        transaction.replace(R.id.fl_left_menu,new LeftMenuFragment(),TAG_LEFT_MENU);
        transaction.commit();//提交事務

    }
    public LeftMenuFragment getLeftMenuFragment(){
        FragmentManager fm = getSupportFragmentManager();
        LeftMenuFragment leftMenuFragment = (LeftMenuFragment) fm.findFragmentByTag(TAG_LEFT_MENU);
        return leftMenuFragment;
    }
    public ContentFragment getContentFragment(){
        FragmentManager fm = getSupportFragmentManager();
        ContentFragment leftMenuFragment = (ContentFragment) fm.findFragmentByTag(TAG_CONTENT);
        return leftMenuFragment;
    }
}
