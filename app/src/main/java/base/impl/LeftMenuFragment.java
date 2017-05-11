package base.impl;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.cleverboy.myapplication.MainActivity;
import com.example.cleverboy.myapplication.R;

import java.util.ArrayList;

import base.BaseFragment;
import domain.NewsMenuData;

/**
 * Created by clever boy on 2017/4/18.
 */

public class LeftMenuFragment extends BaseFragment {

    private ListView lvList;
    public ArrayList<NewsMenuData.NewsData> mMenuList;
    private int mCurrentPos;
    private MenuAdapter menuAdapter;

    @Override
    public View initView() {
        View view = View.inflate(getActivity(), R.layout.fragment_left_menu,null);
        lvList = (ListView) view.findViewById(R.id.lv_left_menu);
        return view;
    }

    public void setData(ArrayList<NewsMenuData.NewsData> data){
        mMenuList = data;
        menuAdapter = new MenuAdapter();
        lvList.setAdapter(menuAdapter);
        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCurrentPos = position;
                menuAdapter.notifyDataSetChanged();
                //通知新闻中心，切换页面
                setCurrentMenuDetailPager(position);
                toggle();
            }
        });
        mCurrentPos = 0;
    }

    private void setCurrentMenuDetailPager(int position) {
        MainActivity mainUI = (MainActivity) mActivity;
        ContentFragment contentFragment = mainUI.getContentFragment();
        NewsCenterPager newsCenterPager = contentFragment.getNewsCenterPager();
        newsCenterPager.setCurrentMenuDetailPager(position);
    }

    ;

    class MenuAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return mMenuList.size();
        }

        @Override
        public Object getItem(int position) {
            return mMenuList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(mActivity,R.layout.list_item_left_menu,null);
            TextView tvMenu = (TextView) view.findViewById(R.id.tv_item);
            tvMenu.setText(mMenuList.get(position).title);
            if(mCurrentPos == position){
                tvMenu.setEnabled(true);
            }else{
                tvMenu.setEnabled(false);
            }
            return view;
        }
    }
    private void toggle(){
        MainActivity mainUI = (MainActivity) mActivity;
        mainUI.getSlidingMenu().toggle();
    }
}
