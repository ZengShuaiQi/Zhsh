package com.example.cleverboy.myapplication;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import utils.PrefUtils;

/**
 * Created by clever boy on 2017/3/30.
 */

public class GuideActivity extends Activity implements View.OnClickListener{

    private ViewPager viewPager;
    private int[] mImageIds = new int[]{R.mipmap.guide_1,R.mipmap.guide_2,R.mipmap.guide_3};
    private ArrayList<ImageView> imageViewList;
    private LinearLayout ll_container;
    private ImageView iv_red_point;
    private int mPointWidth;
    private Button btn_start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_guide);
        viewPager = (ViewPager) findViewById(R.id.vp_pager);
        ll_container = (LinearLayout) findViewById(R.id.ll_container);
        iv_red_point = (ImageView) findViewById(R.id.iv_red_point);
        btn_start = (Button) findViewById(R.id.btn_start);

        btn_start.setOnClickListener(this);

        //初始化ImageView
        imageViewList = new ArrayList<ImageView>();
        for(int i = 0;i<mImageIds.length;i++){
            ImageView view = new ImageView(this);
            view.setBackgroundResource(mImageIds[i]);
            imageViewList.add(view);
            ImageView pointView = new ImageView(this);
            pointView.setBackgroundResource(R.drawable.shape_circle_default);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            if(i > 0){
                params.leftMargin = 20;
            }
            pointView.setLayoutParams(params);

            ll_container.addView(pointView);
        }
        viewPager.setAdapter(new Myadapter());

        //页面绘制结束之后，计算两个小圆点的间距
        //视图树
        iv_red_point.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                iv_red_point.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                mPointWidth = ll_container.getChildAt(1).getLeft() - ll_container.getChildAt(0).getLeft();}
        });

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                int leftMargin = (int) (mPointWidth * positionOffset + position * mPointWidth);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) iv_red_point.getLayoutParams();
                params.leftMargin = leftMargin;
                iv_red_point.setLayoutParams(params);
            }

            @Override
            public void onPageSelected(int position) {
                if(position == mImageIds.length - 1){
                    btn_start.setVisibility(View.VISIBLE);
                }
                else{
                    btn_start.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_start :
                PrefUtils.putBoolean("is_guide_show",true,this);
                startActivity(new Intent(this,MainActivity.class));
                finish();
        break;
        }
    }

    private class Myadapter extends PagerAdapter{

        @Override
        public int getCount() {
            return mImageIds.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
//            ImageView view = new ImageView(getApplicationContext());
//            view.setBackgroundResource(mImageIds[position]);
            ImageView view = imageViewList.get(position);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
