package base.impl;


import android.app.Activity;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.cleverboy.myapplication.MainActivity;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;

import base.BaseMenuDetatilPager;
import base.BasePager;
import base.impl.menudetail.InteractMenuDetailPager;
import base.impl.menudetail.NewsMenuDetailPager;
import base.impl.menudetail.PhotosMenuDetailPager;
import base.impl.menudetail.TopicMenuDetailPager;
import domain.NewsMenuData;
import global.Constants;
import utils.CacheUtils;

/**
 * 新闻中心
 * Created by clever boy on 2017/4/22.
 */

public class NewsCenterPager extends BasePager {

    private ArrayList<BaseMenuDetatilPager> pagers;
    private NewsMenuData newsMenuData;

    public NewsCenterPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {


        String cache = CacheUtils.getCache(Constants.CATEGORIES_URL,mActivity);
        if(!TextUtils.isEmpty(cache)){
            processResult(cache);
        }
            getDataFromServer();


    }
    //从服务器得到数据
    private void getDataFromServer() {
        HttpUtils utils = new HttpUtils();
        utils.send(HttpRequest.HttpMethod.GET, Constants.CATEGORIES_URL, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                System.out.println("result:" + result);
                processResult(result);
                //写缓存
                CacheUtils.setCache(Constants.CATEGORIES_URL,result,mActivity);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                System.out.println(e);
                Toast.makeText(mActivity, "请求失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void processResult(String result){
        Gson gson = new Gson();
        newsMenuData = gson.fromJson(result, NewsMenuData.class);
        System.out.println("解析结果"+ newsMenuData);
        MainActivity mainUI = (MainActivity) mActivity;
        LeftMenuFragment leftMenuFragment = mainUI.getLeftMenuFragment();
        leftMenuFragment.setData(newsMenuData.data);

        pagers = new ArrayList<BaseMenuDetatilPager>();
        pagers.add(new NewsMenuDetailPager(mActivity,newsMenuData.data.get(0).children));
        pagers.add(new TopicMenuDetailPager(mActivity));
        pagers.add(new PhotosMenuDetailPager(mActivity));
        pagers.add(new InteractMenuDetailPager(mActivity));

        setCurrentMenuDetailPager(0);
    }
    //新闻中心页面的Fragment填充布局
    public void setCurrentMenuDetailPager(int position){
        BaseMenuDetatilPager pager = pagers.get(position);
        flContent.removeAllViews();
        flContent.addView(pager.mRootView);
        pager.initData();
        tvTitle.setText(newsMenuData.data.get(position).title);
    }
}
