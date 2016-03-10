package com.nick.smarthome.ui.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.nick.smarthome.common.Constants;
import com.nick.smarthome.ui.adapter.MyLockRecyclerAdapter;
import com.nick.smarthome.ui.base.BaseFragment;
import com.nick.smarthome.utils.TLog;
import com.nick.smarthome.utils.UIHelper;
import com.squareup.okhttp.Request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.InjectView;


public class MyLockFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,
        ObservableScrollViewCallbacks {

    private static final String ARG_POSITION = "position";

    private int position;

    private int mCurrentPage = 1;

    private int pageNumber;

    private String mUsername,mCustomerId;

    private ObservableRecyclerView.LayoutManager mLayoutManager;
    private MyLockRecyclerAdapter mAdapter;
    private List<MyLockEntity> mLockListItems = new ArrayList<MyLockEntity>();

    @InjectView(R.id.swipe_refresh_widget)
    XSwipeRefreshLayout mSwipeRefreshLayout;

    @InjectView(R.id.m_recyclerview)
    ObservableRecyclerView mRecyclerView;


    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Constants.INTENT_ACTION_LOCK_BOUGHT)) {
                onRefresh();
            }
        }
    };



    public static MyLockFragment newInstance(int position) {
        MyLockFragment fragment = new MyLockFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_POSITION, position);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected int getContentViewLayoutID() {
        position = getArguments().getInt(ARG_POSITION);
        return R.layout.fragment_my_lock_list;
    }


    @Override
    protected void onFirstUserVisible() {

    }

    @Override
    protected void onUserVisible() {

    }

    @Override
    protected void onUserInvisible() {

    }

    @Override
    protected View getLoadingTargetView() {
        return mSwipeRefreshLayout;
    }

    @Override
    protected void initViewsAndEvents() {

        IntentFilter filter = new IntentFilter(Constants.INTENT_ACTION_LOCK_UPDATE);
        filter.addAction(Constants.INTENT_ACTION_LOCK_BOUGHT);
        getActivity().registerReceiver(mReceiver, filter);

        showLoading(mContext.getString(R.string.common_loading_message));

        SharedPreferences settings = getActivity().getSharedPreferences("secrecy", Activity.MODE_PRIVATE);
        mUsername = settings.getString("phoneNumber", null);
        mCustomerId = settings.getString("customerId",null);

        mSwipeRefreshLayout.setColorSchemeColors(
                getResources().getColor(R.color.gplus_color_1),
                getResources().getColor(R.color.gplus_color_2),
                getResources().getColor(R.color.gplus_color_3),
                getResources().getColor(R.color.gplus_color_4));
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mLayoutManager = new LinearLayoutManager(getActivity());

        mRecyclerView.setScrollViewCallbacks(this);

        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

//        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
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

        mAdapter = new MyLockRecyclerAdapter(getActivity(), mLockListItems);
        mRecyclerView.setAdapter(mAdapter);

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
                    int loadPage= pageNumber + 1;
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
    private void getNewsList(MyLockRecyclerAdapter adapter,final int currentPage) {

        String url = ServerApiConstants.Urls.GET_MY_LOCK_LIST_URLS;

        Map<String, String> params = new HashMap<String,String>();
        params.put("customerId", mCustomerId);
        params.put("pageNum",String.valueOf(currentPage));
        params.put("pageSize", "15");
        params.put("isBound", String.valueOf(position));


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
                                    if(response.getData().getList().size()>0){
                                        pageNumber = currentPage;
                                        mLockListItems.addAll(response.getData().getList());
                                        mAdapter.notifyDataSetChanged();
                                        mSwipeRefreshLayout.setRefreshing(false);
                                    }else{
                                        UIHelper.showToast(mContext,"没有更多数据");
                                    }
                                } else if(totalCnt <= 0 && mCurrentPage == 1){
                                    toggleShowEmpty(true, "没有查询到数据", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            onRefresh();
                                        }
                                    });
                                }
                            } else {
                                UIHelper.showToast(getActivity(), response.getMessage());
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

        mAdapter.notifyDataSetChanged();

        mCurrentPage = 1;

        getNewsList(mAdapter, mCurrentPage);
    }

    @Override
    protected void onEventComming(EventCenter eventCenter) {

    }

    @Override
    protected boolean isBindEventBusHere() {
        return false;
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


}