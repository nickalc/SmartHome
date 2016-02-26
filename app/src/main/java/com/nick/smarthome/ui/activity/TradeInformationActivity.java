package com.nick.smarthome.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.nick.smarthome.bean.TradeListResult;
import com.nick.smarthome.callback.OrderInfoListCallback;
import com.nick.smarthome.ui.adapter.TradeInfoRecyclerAdapter;
import com.nick.smarthome.ui.base.BaseSwipeBackActivity;
import com.nick.smarthome.ui.base.RecyclerItemClickListener;
import com.nick.smarthome.utils.TLog;
import com.nick.smarthome.utils.UIHelper;
import com.squareup.okhttp.Request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.InjectView;

/**
 * Author:  nick
 * Email:   nickdevp@gmail.com
 * Date:    15/12/14 09:46.
 * Description:
 */
public class TradeInformationActivity extends BaseSwipeBackActivity implements SwipeRefreshLayout.OnRefreshListener, ObservableScrollViewCallbacks {

    private ObservableRecyclerView.LayoutManager mLayoutManager;
    private TradeInfoRecyclerAdapter mAdapter;
    private List<TradeListResult.DataEntity.ListEntity> mTradeListItems = new ArrayList<TradeListResult.DataEntity.ListEntity>();
    private int pageNumber;
    private int mCurrentPage = 1;
    private String mCustomerId;
    private static final int REQUEST_CODE_ORDER_INFO = 0x10;

    @InjectView(R.id.swipe_refresh_widget)
    XSwipeRefreshLayout mSwipeRefreshLayout;

    @InjectView(R.id.trade_list)
    ObservableRecyclerView mRecyclerView;

    protected int getActionBarTitle() {
        return R.string.trade_information;
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
        return R.layout.activity_trade_information;
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

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        Bundle bundle = new Bundle();
                        bundle.putString("custOrderId", String.valueOf(mTradeListItems.get(position).getCustOrderId()));
                        bundle.putString("title", "交易详情");
                        readyGoForResult(PayActivity.class, REQUEST_CODE_ORDER_INFO, bundle);
                    }
                })
        );

        mSwipeRefreshLayout.setColorSchemeColors(
                getResources().getColor(R.color.gplus_color_1),
                getResources().getColor(R.color.gplus_color_2),
                getResources().getColor(R.color.gplus_color_3),
                getResources().getColor(R.color.gplus_color_4));
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mAdapter = new TradeInfoRecyclerAdapter(mContext, mTradeListItems);
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
                    int loadPage = pageNumber + 1;
                    if (mCurrentPage < loadPage) {
                        mCurrentPage = loadPage;
                        getNewsList(mAdapter, mCurrentPage);
                    }
                }
            }
        });
    }


    private void getNewsList(TradeInfoRecyclerAdapter adapter, final int currentPage) {

        SharedPreferences settings = getSharedPreferences("secrecy", Activity.MODE_PRIVATE);
        mCustomerId = settings.getString("customerId", null);

        String url = ServerApiConstants.Urls.GET_PAY_ORDER_INFO_LIST_URLS;

        Map<String, String> params = new HashMap<String, String>();
        params.put("customerId", mCustomerId);
        params.put("pageNum", String.valueOf(currentPage));
        params.put("pageSize", "10");

        if (NetUtils.isNetworkConnected(mContext)) {
            OkHttpUtils
                    .get()
                    .url(url)
                    .params(params)
                    .build()
                    .execute(new OrderInfoListCallback() {
                        @Override
                        public void onError(Request request, Exception e) {
                            TLog.error(e.toString());
                        }

                        @Override
                        public void onResponse(TradeListResult response) {

                            hideLoading();

                            if (response.getStatuscode().equals("1")) {

                                int totalCnt = response.getData().getTotalCnt();
                                if (totalCnt > 0) {
                                    if (response.getData().getList().size() > 0) {
                                        pageNumber = currentPage;
                                        mTradeListItems.addAll(response.getData().getList());
                                        mAdapter.notifyDataSetChanged();
                                        mSwipeRefreshLayout.setRefreshing(false);
                                    } else {
                                        UIHelper.showToast(mContext, "没有更多数据");
                                    }
                                } else {
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
        } else {
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

        mTradeListItems.clear();

        mAdapter.notifyDataSetChanged();

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != Activity.RESULT_OK)
            return;

        if (requestCode == REQUEST_CODE_ORDER_INFO) {
            onRefresh();
        }
//        switch (requestCode) {
//            case REQUEST_CODE_ORDER_INFO:
//                onRefresh();
//                break;
//            default:
//                break;
//        }
    }

}
