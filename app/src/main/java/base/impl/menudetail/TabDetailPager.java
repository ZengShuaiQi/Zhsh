package base.impl.menudetail;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cleverboy.myapplication.R;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.viewpagerindicator.CirclePageIndicator;

import org.w3c.dom.Text;

import java.util.ArrayList;

import base.BaseMenuDetatilPager;
import domain.NewsData;
import domain.NewsMenuData;
import global.Constants;
import utils.CacheUtils;
import view.HorizontalScrollViewPager;
import view.RefreshListView;

/**
 * Created by clever boy on 2017/4/29.
 */

public class TabDetailPager extends BaseMenuDetatilPager {

    private NewsMenuData.NewsTabData mTabData;
    private HorizontalScrollViewPager mViewPager;
    private RefreshListView lvList;
    private String mUrl;
    private NewsData newsData;
    private ArrayList<NewsData.TopNews> mTopNewsList;
    private TopNewsAdapter mTopNewsAdapter;
    private CirclePageIndicator circlePageIndicator;
    private TextView tv_title;
    private ArrayList<NewsData.News> mNewsList;
    private MyAdapter mNewsAdapter;
    private String mMoreUrl;

    public TabDetailPager(Activity activity, NewsMenuData.NewsTabData tabData) {
        super(activity);
        mTabData = tabData;
        mUrl = Constants.SERVER_URL + mTabData.url;

    }

    @Override
    public View initView() {
//        view = new TextView(mActivity);
//        //view.setText("页签");
//        view.setTextColor(Color.RED);
//        view.setTextSize(22);
//        view.setGravity(Gravity.CENTER);
        View view = View.inflate(mActivity, R.layout.pager_tab_detail, null);
        View header = View.inflate(mActivity, R.layout.list_header_topnews, null);
        mViewPager = (HorizontalScrollViewPager) header.findViewById(R.id.vp_tab_detail);
        lvList = (RefreshListView) view.findViewById(R.id.lv_tab_detail);
        circlePageIndicator = (CirclePageIndicator) header.findViewById(R.id.indicator);
        tv_title = (TextView) header.findViewById(R.id.tv_title);
        lvList.addHeaderView(header);
        lvList.setOnRefreshListener(new RefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataFromServer();
            }

            @Override
            public void loadMore() {
                if(mMoreUrl != null){
                    getMoreDataFromServer();
                }else{
                    lvList.onRefreshComplete(true);
                    Toast.makeText(mActivity, "没有更多数据了", Toast.LENGTH_SHORT).show();
                }

            }
        });
        return view;
    }

    @Override
    public void initData() {
        String cache = CacheUtils.getCache(mUrl, mActivity);
        if (!TextUtils.isEmpty(cache)) {
            processResult(cache,false);
        }
        getDataFromServer();
    }

    private void getDataFromServer() {
        HttpUtils utils = new HttpUtils();
//        System.out.println(mUrl);
        utils.send(HttpRequest.HttpMethod.GET, mUrl, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
//                System.out.println(result);
                CacheUtils.setCache(mUrl, result, mActivity);
                processResult(result,false);
                lvList.onRefreshComplete(true);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
//                System.out.println(error+msg);
                lvList.onRefreshComplete(false);
                Toast.makeText(mActivity, "获取失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void getMoreDataFromServer(){
        HttpUtils utils = new HttpUtils();
        utils.send(HttpRequest.HttpMethod.GET, mMoreUrl, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                processResult(result, true);
                // 收起加载更多布局
                lvList.onRefreshComplete(true);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                error.printStackTrace();
                Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
                // 收起加载更多布局
                lvList.onRefreshComplete(false);
            }
        });

    }

    //处理网络数据
    private void processResult(String result,boolean isMore) {
        Gson gson = new Gson();
        newsData = gson.fromJson(result, NewsData.class);

            if (!TextUtils.isEmpty(newsData.data.more)) {

                mMoreUrl = Constants.SERVER_URL + newsData.data.more;
            } else {
                mMoreUrl = null;
            }
        if(!isMore) {
            mTopNewsList = newsData.data.topnews;

            if (mTopNewsList != null) {
                mTopNewsAdapter = new TopNewsAdapter();
                mViewPager.setAdapter(mTopNewsAdapter);
                //为ViewPager设置指示器
                circlePageIndicator.setViewPager(mViewPager);
                circlePageIndicator.setSnap(true);
                circlePageIndicator.onPageSelected(0);
                circlePageIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                        NewsData.TopNews topNews = mTopNewsList.get(position);
                        tv_title.setText(topNews.title);
                    }

                    @Override
                    public void onPageSelected(int position) {

                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
                // 初始化新闻列表
                mNewsList = newsData.data.news;
                if (mNewsList != null) {
                    mNewsAdapter = new MyAdapter();
                    lvList.setAdapter(mNewsAdapter);
                }
            }
        }
        else{
            ArrayList<NewsData.News> moreData = newsData.data.news;
            mNewsList.addAll(moreData);
            mNewsAdapter.notifyDataSetChanged();
        }
    }

    //定义ViewPager适配器
    class TopNewsAdapter extends PagerAdapter {

        BitmapUtils mBitmapUtils;

        public TopNewsAdapter() {
            mBitmapUtils = new BitmapUtils(mActivity);
            mBitmapUtils.configDefaultLoadingImage(R.mipmap.topnews_item_default);
        }

        @Override
        public int getCount() {
            return mTopNewsList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView view = new ImageView(mActivity);
            view.setScaleType(ImageView.ScaleType.FIT_XY);
            mBitmapUtils.display(view, mTopNewsList.get(position).topimage);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    //定义ListView 适配器
    class MyAdapter extends BaseAdapter {
        BitmapUtils mBitmapUtils;

        public MyAdapter() {
            mBitmapUtils = new BitmapUtils(mActivity);
            mBitmapUtils.configDefaultLoadingImage(R.mipmap.pic_item_list_default);
        }

        @Override
        public int getCount() {
            return mNewsList.size();
        }

        @Override
        public NewsData.News getItem(int position) {
            return mNewsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mActivity, R.layout.list_item_news, null);
                holder = new ViewHolder();
                holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
                holder.tvDate = (TextView) convertView.findViewById(R.id.tv_date);
                holder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            NewsData.News news = getItem(position);
            holder.tvTitle.setText(news.title);
            holder.tvDate.setText(news.pubdate);
            mBitmapUtils.display(holder.ivIcon, news.listimage);
            return convertView;
        }
    }

    static class ViewHolder {
        public TextView tvTitle;
        public TextView tvDate;
        public ImageView ivIcon;

    }
}
