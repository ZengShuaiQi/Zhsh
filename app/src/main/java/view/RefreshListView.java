package view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.cleverboy.myapplication.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**下拉刷新的ListView
 * Created by clever boy on 2017/5/4.
 */

public class RefreshListView extends ListView implements AbsListView.OnScrollListener{
    private static final int STATE_PULL_TO_REFRESH = 1;
    private static final int STATE_RELEASE_TO_REFRESH = 2;
    private static final int STATE_REFRESHING = 3;

    private int mCurrentState = STATE_PULL_TO_REFRESH;
    private int startY = -1;
    private View mHeaderView;
    private int mHeaderViewHeight;
    private RotateAnimation animUp;//箭头向上动画
    private RotateAnimation animDown;//箭头向上动画
    private boolean isLoadingMore;

    private TextView tvTitle;
    private ImageView ivArrow;
    private ProgressBar pbLoading;
    private TextView tvTime;
    private View mFooterView;
    private int mFooterViewHeight;

    public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHeaderView();
        initFooterView();
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initHeaderView();
        initFooterView();
    }

    public RefreshListView(Context context) {
        super(context);
        initHeaderView();
        initFooterView();
    }
    public void initHeaderView(){
        mHeaderView = View.inflate(getContext(), R.layout.list_refresh_header,null);
        tvTitle = (TextView) mHeaderView.findViewById(R.id.tv_title);
        ivArrow = (ImageView) mHeaderView.findViewById(R.id.iv_arrow);
        pbLoading = (ProgressBar) mHeaderView.findViewById(R.id.pb_loading);
        tvTime = (TextView) mHeaderView.findViewById(R.id.tv_time);
        this.addHeaderView(mHeaderView);//添加头布局

        mHeaderView.measure(0,0);//手动测量布局
        mHeaderViewHeight = mHeaderView.getMeasuredHeight();//测量之后的高度
        mHeaderView.setPadding(0,-mHeaderViewHeight,0,0);//隐藏头布局
        initAnim();
        setCurrentTime();
    }
    private void initFooterView(){
        mFooterView = View.inflate(getContext(), R.layout.list_refresh_footer,null);
        this.addFooterView(mFooterView);
        mFooterView.measure(0,0);
        mFooterViewHeight = mFooterView.getMeasuredHeight();
        mFooterView.setPadding(0,-mFooterViewHeight,0,0);

        this.setOnScrollListener(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN :
                startY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE :
                if(startY == -1){
                    startY = (int) ev.getY();
                }
                if(mCurrentState == STATE_REFRESHING){
                    break;
                }
                int endY = (int) ev.getY();
                int dy = endY - startY;
                if (dy > 0 && getFirstVisiblePosition() == 0){
                    int paddingTop = dy - mHeaderViewHeight;

                    if(paddingTop > 0 && mCurrentState != STATE_RELEASE_TO_REFRESH){//切换到松开刷新
                        mCurrentState = STATE_RELEASE_TO_REFRESH;
                        refreshState();
                    }else if(paddingTop < 0 && mCurrentState != STATE_PULL_TO_REFRESH){//切换到下拉刷新
                        mCurrentState = STATE_PULL_TO_REFRESH;
                        refreshState();
                    }

                    mHeaderView.setPadding(0,paddingTop,0,0);
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP :
                startY = -1;

                if(mCurrentState == STATE_RELEASE_TO_REFRESH){
                    mCurrentState = STATE_REFRESHING;
                    mHeaderView.setPadding(0,0,0,0);
                    refreshState();
                    // 下拉刷新回调
                    if (mListener != null) {
                        mListener.onRefresh();
                    }
                }else if(mCurrentState == STATE_PULL_TO_REFRESH){
                    mHeaderView.setPadding(0,-mHeaderViewHeight,0,0);
                }
                break;
        }

        return super.onTouchEvent(ev);
    }

    /**
     * 初始化箭头动画
     */
    private void initAnim(){
        animUp = new RotateAnimation(0,-180, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        animUp.setDuration(500);
        animUp.setFillAfter(true);
        animDown = new RotateAnimation(-180,0,  Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animDown.setDuration(500);
        animDown.setFillAfter(true);
    }

    /**
     * 根据当前状态刷新界面
     */
    private void refreshState(){
        switch (mCurrentState){
            case STATE_PULL_TO_REFRESH:
                tvTitle.setText("下拉刷新");
                ivArrow.startAnimation(animDown);
                pbLoading.setVisibility(INVISIBLE);
                ivArrow.setVisibility(VISIBLE);
                break;
            case STATE_RELEASE_TO_REFRESH:
                tvTitle.setText("松开刷新");
                ivArrow.startAnimation(animUp);
                pbLoading.setVisibility(INVISIBLE);
                ivArrow.setVisibility(VISIBLE);
                break;
            case STATE_REFRESHING:
                tvTitle.setText("正在刷新");
                pbLoading.setVisibility(VISIBLE);
                ivArrow.clearAnimation();
                ivArrow.setVisibility(INVISIBLE);
                break;
        }
    }
    /**
     * 设置刷新时间
     */
    private void setCurrentTime(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = format.format(new Date());
        tvTime.setText(time);
    }
    //刷新完成
    public void onRefreshComplete(boolean success){
        if(!isLoadingMore) {
            mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);
            mCurrentState = STATE_PULL_TO_REFRESH;
            tvTitle.setText("下拉刷新");
            pbLoading.setVisibility(INVISIBLE);
            ivArrow.setVisibility(VISIBLE);
            if (success)
                setCurrentTime();
        }else{
            mFooterView.setPadding(0, -mFooterViewHeight, 0, 0);
            isLoadingMore = false;
        }
    }


    public void setOnRefreshListener(OnRefreshListener mListener){
        this.mListener = mListener;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(scrollState == SCROLL_STATE_IDLE){
            int lastVisiblePosition = getLastVisiblePosition();
            if(lastVisiblePosition >= getCount() -1 &&!isLoadingMore){
                isLoadingMore = true;
                mFooterView.setPadding(0,0,0,0);
                //ListView设置当前显示位置
                setSelection(getCount() - 1);
                if (mListener != null) {
                    mListener.loadMore();
                }
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }
    /**
     * 刷新回调
     */
    private OnRefreshListener mListener;
    public interface OnRefreshListener{
        public void onRefresh();
        public void loadMore();
    }
}
