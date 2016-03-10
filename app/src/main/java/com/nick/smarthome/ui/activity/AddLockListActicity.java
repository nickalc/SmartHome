package com.nick.smarthome.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.okhttp.OkHttpUtils;
import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.obsessive.library.eventbus.EventCenter;
import com.github.obsessive.library.netstatus.NetUtils;
import com.github.obsessive.library.widgets.XSwipeRefreshLayout;
import com.nick.smarthome.R;
import com.nick.smarthome.api.ServerApiConstants;
import com.nick.smarthome.bean.MyLockEntity;
import com.nick.smarthome.bean.MyLockListResult;
import com.nick.smarthome.callback.MyLockListCallback;
import com.nick.smarthome.ui.adapter.AutoRVAdapter;
import com.nick.smarthome.ui.adapter.ViewHolder;
import com.nick.smarthome.ui.base.BaseSwipeBackActivity;
import com.nick.smarthome.utils.TLog;
import com.nick.smarthome.utils.UIHelper;
import com.squareup.okhttp.Request;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.InjectView;

/**
 * Author:  nick
 * Email:   nickdevp@gmail.com
 * Date:    15/12/12 20:36.
 * Description:添加锁
 */
public class AddLockListActicity extends BaseSwipeBackActivity implements SwipeRefreshLayout.OnRefreshListener, ObservableScrollViewCallbacks {

    private ObservableRecyclerView.LayoutManager mLayoutManager;
    private AddLockedRecyclerAdapter mAdapter;
    private List<MyLockEntity> mLockListItems = new ArrayList<MyLockEntity>();
    private List<MyLockEntity> mSelectedItems = new ArrayList<MyLockEntity>();
    /**
     * the page number
     */
    private int mCurrentPage = 1;

    private int pageNumber;

    @InjectView(R.id.swipe_refresh_widget)
    XSwipeRefreshLayout mSwipeRefreshLayout;

    @InjectView(R.id.lock_list)
    ObservableRecyclerView mRecyclerView;

    private String mCustomerId;

    @Override
    protected int getActionBarTitle() {
        return R.string.add_lock;
    }

    @Override
    protected boolean isApplyKitKatTranslucency() {
        return true;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {


    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_add_lock_list;
    }

    @Override
    protected void onEventComming(EventCenter eventCenter) {

    }

    @Override
    protected View getLoadingTargetView() {
        return mSwipeRefreshLayout;
    }

    @Override
    protected void initViewsAndEvents() {

        showLoading(mContext.getString(R.string.common_loading_message));

        mSwipeRefreshLayout.setColorSchemeColors(
                getResources().getColor(R.color.gplus_color_1),
                getResources().getColor(R.color.gplus_color_2),
                getResources().getColor(R.color.gplus_color_3),
                getResources().getColor(R.color.gplus_color_4));
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mLayoutManager = new LinearLayoutManager(this);

        mRecyclerView.setScrollViewCallbacks(this);

        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

//        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(View view, int position) {
//
//                        showToast("recycleview 点击");
//                    }
//                })
//        );

        mSwipeRefreshLayout.setColorSchemeColors(
                getResources().getColor(R.color.gplus_color_1),
                getResources().getColor(R.color.gplus_color_2),
                getResources().getColor(R.color.gplus_color_3),
                getResources().getColor(R.color.gplus_color_4));
        mSwipeRefreshLayout.setOnRefreshListener(this);

        getNewsList(mAdapter, 1);

        //监听list滑动事件
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItem = ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();
                int totalItem = mLayoutManager.getItemCount();
                //当剩下2个item时加载下一页
                if (lastVisibleItem > totalItem - 2 && dy > 0) {
                    int loadPage = pageNumber + 1;
                    if (mCurrentPage < loadPage) {
                        mCurrentPage = loadPage;
                        getNewsList(mAdapter, mCurrentPage);
                    }
                }
            }
        });
    }

    /**
     * @param adapter
     * @param currentPage
     */
    private void getNewsList(AddLockedRecyclerAdapter adapter,final int currentPage) {

        SharedPreferences settings = getSharedPreferences("secrecy", Activity.MODE_PRIVATE);
        mCustomerId = settings.getString("customerId",null);

        String url = ServerApiConstants.Urls.GET_MY_LOCK_LIST_URLS;

        Map<String, String> params = new HashMap<String,String>();
        params.put("customerId", mCustomerId);
        params.put("pageNum",String.valueOf(currentPage));
        params.put("pageSize", "15");
        params.put("isBound", "0");
        params.put("isHaveModify","1");

        if (NetUtils.isNetworkConnected(mContext)) {
            OkHttpUtils
                    .get()
                    .url(url)
                    .params(params)
                    .build()
                    .execute(new MyLockListCallback() {
                        @Override
                        public void onError(Request request, Exception e) {
                            TLog.error(e.toString());
                        }

                        @Override
                        public void onResponse(MyLockListResult response) {

                            hideLoading();

                            if (response.getStatuscode().equals("1")) {

                                int totalCnt = response.getData().getTotalCnt();
                                if (totalCnt > 0) {
                                    mLockListItems = response.getData().getList();
                                    pageNumber = currentPage;
                                    mAdapter = new AddLockedRecyclerAdapter(mContext, mLockListItems);
                                    mRecyclerView.setAdapter(mAdapter);
                                    mAdapter.notifyDataSetChanged();
                                    mSwipeRefreshLayout.setRefreshing(false);
                                } else{
                                    toggleShowEmpty(true, "没有查询到数据", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            onRefresh();
                                        }
                                    });
                                }
                            } else {
                                UIHelper.showToast(mContext, response.getMessage());
                            }

                        }
                    });
        }else {
            toggleNetworkError(true, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRefresh();
                }
            });
        }
    }

    @Override
    public void onRefresh() {
        if (null != mSwipeRefreshLayout) {
            mSwipeRefreshLayout.setRefreshing(false);
        }

        mLockListItems.clear();

        mCurrentPage = 1;

        getNewsList(mAdapter, mCurrentPage);

    }

    @Override
    protected void onNetworkConnected(NetUtils.NetType type) {

    }

    @Override
    protected void onNetworkDisConnected() {

    }

    @Override
    protected boolean isApplyStatusBarTranslucency() {
        return false;
    }

    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

    @Override
    protected boolean toggleOverridePendingTransition() {
        return false;
    }

    @Override
    protected TransitionMode getOverridePendingTransitionMode() {
        return null;
    }


    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {

    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_lock, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.finish:

                int len = mLockListItems.size();
                for (int i = 0; i < len; i++) {
                    MyLockEntity myLockEntity = mLockListItems.get(i);
                    if (myLockEntity.isSelected()) {
                        mSelectedItems.add(myLockEntity);
                    }
                }

                Intent result = new Intent();
                result.putExtra("lockList",(Serializable)mSelectedItems);
                setResult(Activity.RESULT_OK, result);

                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public class AddLockedRecyclerAdapter extends AutoRVAdapter {


        private Context context;

        /**
         * @param context
         * @param list
         */
        public AddLockedRecyclerAdapter(Context context, List<MyLockEntity> list) {
            super(context, list);
            this.context = context;
            this.list = mLockListItems;
        }

        @Override
        public int onCreateViewLayoutID(int viewType) {
            return R.layout.item_add_lock_recyclerview;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {

            final MyLockEntity item=(MyLockEntity) mLockListItems.get(position);
            holder.getTextView(R.id.tv_house_info).setText(item.getLockCode());
            if ("ROOM".equals(item.getLockType())) {
                holder.getTextView(R.id.room_type).setText("房间");
            }else{
                holder.getTextView(R.id.room_type).setText("客厅");
            }
            holder.getTextView(R.id.tv_room_no).setText(item.getDoorNo());
            holder.getCheckBox(R.id.ckeckbox_lock).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item.isSelected()) {
                        holder.getCheckBox(R.id.ckeckbox_lock).setChecked(false);
                        item.setIsSelected(false);
                    }else{
                        holder.getCheckBox(R.id.ckeckbox_lock).setChecked(true);
                        item.setIsSelected(true);
                    }
                }
            });

            holder.getConvertView().findViewById(R.id.item_add_lock_list_layout).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item.isSelected()) {
                        holder.getCheckBox(R.id.ckeckbox_lock).setChecked(false);
                        item.setIsSelected(false);
                    }else{
                        holder.getCheckBox(R.id.ckeckbox_lock).setChecked(true);
                        item.setIsSelected(true);
                    }
                }
            });
        }

        public Object getItems() {
            return list;
        }
    }
}
