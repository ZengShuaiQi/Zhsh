package base.impl.menudetail;

import android.app.Activity;
import android.graphics.Color;
import android.media.MediaRouter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cleverboy.myapplication.MainActivity;
import com.example.cleverboy.myapplication.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;

import base.BaseMenuDetatilPager;
import domain.NewsMenuData;

/**
 * Created by clever boy on 2017/4/26.
 */

public class NewsMenuDetailPager extends BaseMenuDetatilPager implements ViewPager.OnPageChangeListener{

    private ViewPager viewPager;
    private ArrayList<NewsMenuData.NewsTabData> mTabList;
    private ArrayList<TabDetailPager> mTabPagers;
    private TabPageIndicator mIndicator;
    private ImageView iv_next_page;

    public NewsMenuDetailPager(Activity activity, ArrayList<NewsMenuData.NewsTabData> children) {
        super(activity);
        mTabList = children;
    }

    @Override
    public View initView() {
       final View view = View.inflate(mActivity, R.layout.pager_menu_detail,null);
        viewPager = (ViewPager) view.findViewById(R.id.vp_detail);
        mIndicator = (TabPageIndicator) view.findViewById(R.id.indicator);
        iv_next_page = (ImageView) view.findViewById(R.id.iv_next_page);
        iv_next_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentItem = viewPager.getCurrentItem();
                if(currentItem<11)
                currentItem++;
                viewPager.setCurrentItem(currentItem);
            }
        });
        return view;
    }

    @Override
    public void initData() {
        mTabPagers = new ArrayList<TabDetailPager>();
        for (NewsMenuData.NewsTabData tabData : mTabList) {
            TabDetailPager pager = new TabDetailPager(mActivity,tabData);
            mTabPagers.add(pager);
        }
        viewPager.setAdapter(new MyAdapter());
        //viewPager.setOnPageChangeListener(this);
        mIndicator.setViewPager(viewPager);
        mIndicator.setOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == 0) {// 在第一个页签,允许侧边栏出现
            // 开启侧边栏
            setSlidingMenuEnable(true);
        } else {// 其他页签,禁用侧边栏, 保证viewpager可以正常向右滑动
            // 关闭侧边栏
            setSlidingMenuEnable(false);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
    /**
     * 设置侧边栏可用不可用
     *
     * @param enable
     */
    private void setSlidingMenuEnable(boolean enable) {
        MainActivity mainUI = (MainActivity) mActivity;
        SlidingMenu slidingMenu = mainUI.getSlidingMenu();

        if (enable) {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        } else {
            // 禁用掉侧边栏滑动效果
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }
    }


    class MyAdapter extends PagerAdapter{

        @Override
        public CharSequence getPageTitle(int position) {
            return mTabList.get(position).title;
        }

        @Override
        public int getCount() {
            return mTabPagers.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TabDetailPager pager = mTabPagers.get(position);
            container.addView(pager.mRootView);
            pager.initData();
            return pager.mRootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

}
