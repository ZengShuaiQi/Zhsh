package base.impl.menudetail;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.cleverboy.myapplication.R;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;

import base.BaseMenuDetatilPager;
import domain.PhotoBean;
import global.Constants;
import utils.CacheUtils;

/**
 * Created by clever boy on 2017/4/26.
 */

public class PhotosMenuDetailPager extends BaseMenuDetatilPager implements View.OnClickListener{

    private ListView listView;
    private GridView gridView;
    private ArrayList mPhotoList;
    private ImageButton btnGrid;
    private boolean isList = true;

    public PhotosMenuDetailPager(Activity activity, ImageButton btnGrid) {
        super(activity);
//        btnGrid.setVisibility(View.VISIBLE);
        this.btnGrid = btnGrid;
        btnGrid.setOnClickListener(this);
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.pager_menu_detail_photo, null);
        listView = (ListView) view.findViewById(R.id.lv_photo);
        gridView = (GridView) view.findViewById(R.id.gv_photo);
        return view;
    }

    @Override
    public void initData() {
        String cache = CacheUtils.getCache(Constants.PHOTOS_URL,mActivity);
        if(!TextUtils.isEmpty(cache)){
            processResult(cache);
        }
        getDataFromServer();
    }

    private void getDataFromServer() {
        HttpUtils utils = new HttpUtils();
        utils.send(HttpRequest.HttpMethod.GET, Constants.PHOTOS_URL, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                processResult(responseInfo.result);
                CacheUtils.setCache(Constants.PHOTOS_URL,responseInfo.result,mActivity);
            }

            @Override
            public void onFailure(HttpException error, String msg) {

            }
        });
    }

    private void processResult(String result) {
        Gson gson = new Gson();
        PhotoBean photoBean = gson.fromJson(result, PhotoBean.class);
        mPhotoList = photoBean.data.news;

        listView.setAdapter(new PhotoAdapter());
        gridView.setAdapter(new PhotoAdapter());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_grid:
                if(isList){
                    isList = false;
                    listView.setVisibility(View.GONE);
                    gridView.setVisibility(View.VISIBLE);
                    System.out.println("sdfsdfsdf");
                    btnGrid.setImageResource(R.mipmap.grid_type);
                }else{
                    isList = true;
                    listView.setVisibility(View.VISIBLE);
                    gridView.setVisibility(View.GONE);
                    btnGrid.setImageResource(R.mipmap.icon_pic_list_type);
                }
                break;
        }
    }

    class PhotoAdapter extends BaseAdapter {
        private BitmapUtils bitmapUtils;
        public PhotoAdapter(){
            bitmapUtils = new BitmapUtils(mActivity);
            bitmapUtils.configDefaultLoadingImage(R.mipmap.pic_item_list_default);
        }
        @Override
        public int getCount() {
            return mPhotoList.size();
        }

        @Override
        public PhotoBean.PhotoNewsData getItem(int position) {
            return (PhotoBean.PhotoNewsData) mPhotoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = View.inflate(mActivity, R.layout.list_item_photo, null);
                holder = new ViewHolder();
                holder.tvtitle = (TextView) convertView.findViewById(R.id.tv_title);
                holder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }

            PhotoBean.PhotoNewsData item =  getItem(position);
            holder.tvtitle.setText(item.title);
            bitmapUtils.display(holder.ivIcon,item.listimage);

            return convertView;
        }
    }

    static class ViewHolder {
        public TextView tvtitle;
        public ImageView ivIcon;
    }
}
