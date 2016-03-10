package com.nick.smarthome.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import com.google.gson.Gson;
import com.nick.smarthome.R;
import com.nick.smarthome.api.ServerApiConstants;
import com.nick.smarthome.bean.CommResult;
import com.nick.smarthome.bean.MyHouseListEntity;
import com.nick.smarthome.bean.MyHouseListResult;
import com.nick.smarthome.callback.CommonCallback;
import com.nick.smarthome.callback.MyHouseListCallback;
import com.nick.smarthome.ui.adapter.MyHouseRecyclerAdapter;
import com.nick.smarthome.ui.base.BaseSwipeBackActivity;
import com.nick.smarthome.utils.DialogHelp;
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
 * Date:    15/12/12 20:36.
 * Description:
 */
public class MyHouseActicity extends BaseSwipeBackActivity implements SwipeRefreshLayout.OnRefreshListener, ObservableScrollViewCallbacks {

    private ObservableRecyclerView.LayoutManager mLayoutManager;
    private MyHouseRecyclerAdapter mAdapter;
    private List<MyHouseListEntity> mHouseListItems = new ArrayList<MyHouseListEntity>();
    private static final int REQUEST_CODE_ADD_HOUSE = 0x10;
    private static final int REQUEST_CODE_MODIFY_HOUSE = 0x11;
    private String mCustomerId;
    private int pageNumber;
    private int mCurrentPage = 1;
    ProgressDialog mDialog;

    @InjectView(R.id.swipe_refresh_widget)
    XSwipeRefreshLayout mSwipeRefreshLayout;

    @InjectView(R.id.house_list)
    ObservableRecyclerView mRecyclerView;

    @Override
    protected int getActionBarTitle() {
        return R.string.my_house_resource;
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
        return R.layout.activity_my_house_list;
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

        mDialog = DialogHelp.getWaitDialog(this, "正在删除数据...");

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
//                        Bundle bundle = new Bundle();
//                        bundle.putString("houseId",String.valueOf(mHouseListItems.get(position).getHouseId()));
//                        readyGoForResult(ModifyMyHouseActivity.class, REQUEST_CODE_MODIFY_HOUSE, bundle);
//                    }
//                })
//        );

        mSwipeRefreshLayout.setColorSchemeColors(
                getResources().getColor(R.color.gplus_color_1),
                getResources().getColor(R.color.gplus_color_2),
                getResources().getColor(R.color.gplus_color_3),
                getResources().getColor(R.color.gplus_color_4));
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mAdapter = new MyHouseRecyclerAdapter(mContext, mHouseListItems);
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

    /**
     * @param adapter
     * @param currentPage
     */
    private void getNewsList(MyHouseRecyclerAdapter adapter, final int currentPage) {

        SharedPreferences settings = getSharedPreferences("secrecy", Activity.MODE_PRIVATE);
        mCustomerId = settings.getString("customerId", null);

        String url = ServerApiConstants.Urls.GET_MY_HOUSE_LIST_URLS;

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
                    .execute(new MyHouseListCallback() {
                        @Override
                        public void onError(Request request, Exception e) {
                            TLog.error(e.toString());
                        }

                        @Override
                        public void onResponse(MyHouseListResult response) {

                            hideLoading();

                            if (response.getStatuscode().equals("1")) {

                                int totalCnt = response.getData().getTotalCnt();
                                if (totalCnt > 0) {
                                    if (response.getData().getList().size() > 0) {
                                        pageNumber = currentPage;
                                        mHouseListItems.addAll(response.getData().getList());
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

        mHouseListItems.clear();

        mAdapter.notifyDataSetChanged();

        mCurrentPage = 1;

        getNewsList(mAdapter, mCurrentPage);
    }

    private void deleteHouse() {

        int len = mHouseListItems.size();
        List roomIdList = new ArrayList();
        for (int i = 0; i < len; i++) {
            MyHouseListEntity myHoouseEntity = mHouseListItems.get(i);
            if (myHoouseEntity.isSelected()) {
                roomIdList.add(myHoouseEntity.getRoomId());
            }
        }

        if (roomIdList.size() <= 0) {
            UIHelper.showToast(mContext, "请先勾选要删除的房源");
            return;
        }
        Gson gson = new Gson();
        final String roomIdListStr = gson.toJson(roomIdList);

        DialogHelp.getConfirmDialog(this, "确定删除已选房源?", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                mDialog.show();

                String url = ServerApiConstants.Urls.DELETE_HOUSE_RESOURCE_URLS;
                Map<String, String> params = new HashMap<String, String>();
                params.put("roomIdList", roomIdListStr);

                OkHttpUtils.
                        post()
                        .url(url)
                        .params(params)
                        .build()
                        .execute(new CommonCallback() {
                            @Override
                            public void onError(Request request, Exception e) {
                                UIHelper.showToast(mContext, "接口出错");
                                mDialog.hide();
                            }

                            @Override
                            public void onResponse(CommResult response) {
                                mDialog.hide();
                                if (response.statuscode.equals("1")) {
                                    UIHelper.showToast(mContext, response.message);
                                    onRefresh();
                                } else {
                                    UIHelper.showToast(mContext, response.message);
                                }
                            }
                        });
            }
        }).show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_my_house_resource, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_capture:
                deleteHouse();
                break;
            case R.id.action_add:
                readyGoForResult(PublishHouseActivity.class, REQUEST_CODE_ADD_HOUSE);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
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

        switch (requestCode) {
            case REQUEST_CODE_ADD_HOUSE:
                onRefresh();
                break;
            default:
                break;
        }
    }
}
