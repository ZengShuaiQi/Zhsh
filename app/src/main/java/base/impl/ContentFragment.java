package base.impl;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.example.cleverboy.myapplication.MainActivity;
import com.example.cleverboy.myapplication.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;

import base.BaseFragment;
import base.BasePager;

/**
 * Created by clever boy on 2017/4/18.
 */

public class ContentFragment extends BaseFragment {

    private ViewPager viewPager;
    private ArrayList<BasePager> mPagers;
    private RadioGroup rgGroup;

    @Override
    public View initView() {
        View view = View.inflate(getActivity(), R.layout.fragment_content,null);
        viewPager = (ViewPager) view.findViewById(R.id.vp_content);
        rgGroup = (RadioGroup) view.findViewById(R.id.rg_group);
        return view;
    }

    @Override
    public void initData() {
        //初始化5个标签
        mPagers = new ArrayList<BasePager>();
        mPagers.add(new HomePager(mActivity));
        mPagers.add(new NewsCenterPager(mActivity));
        mPagers.add(new SmartServicePager(mActivity));
        mPagers.add(new GovAffairsPager(mActivity));
        mPagers.add(new SettingPager(mActivity));

        viewPager.setAdapter(new ContentAdapter());

        rgGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_home :
                        viewPager.setCurrentItem(0,false);
                        mPagers.get(0).initData();
                        setSlidingMenuEnable(false);
                        break;
                    case R.id.rb_news :
                        viewPager.setCurrentItem(1,false);
                        mPagers.get(1).initData();
                        setSlidingMenuEnable(true);
                        break;
                    case R.id.rb_smart :
                        viewPager.setCurrentItem(2,false);
                        mPagers.get(2).initData();
                        setSlidingMenuEnable(true);
                        break;
                    case R.id.rb_gov:
                        viewPager.setCurrentItem(3,false);
                        mPagers.get(3).initData();
                        setSlidingMenuEnable(true);
                        break;
                    case R.id.rb_setting :
                        viewPager.setCurrentItem(4,false);
                        mPagers.get(4).initData();
                        setSlidingMenuEnable(false);
                        break;
                }
            }
       });
        mPagers.get(0).initData();
        setSlidingMenuEnable(false);
    }

    // 定义ViewPager适配器
    class ContentAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return mPagers.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BasePager basePager = mPagers.get(position);
            container.addView(basePager.mRootView);
            //basePager.initData();//初始化数据
            return basePager.mRootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
    private void setSlidingMenuEnable(boolean enable){
        MainActivity mainActivity = (MainActivity) mActivity;
        SlidingMenu slidingMenu = mainActivity.getSlidingMenu();
        if(enable){
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        }else{
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }
    }
    public NewsCenterPager getNewsCenterPager(){
        return (NewsCenterPager) mPagers.get(1);
    }

}
