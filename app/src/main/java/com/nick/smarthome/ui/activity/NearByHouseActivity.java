package com.nick.smarthome.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.android.okhttp.OkHttpUtils;
import com.github.obsessive.library.eventbus.EventCenter;
import com.github.obsessive.library.netstatus.NetUtils;
import com.github.obsessive.library.widgets.XSwipeRefreshLayout;
import com.nick.smarthome.R;
import com.nick.smarthome.api.ServerApiConstants;
import com.nick.smarthome.bean.NearByHouseEntity;
import com.nick.smarthome.bean.NearByHouseListResult;
import com.nick.smarthome.callback.NearByHouseListCallback;
import com.nick.smarthome.ui.adapter.HouseResRecyclerAdapter;
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
import butterknife.OnClick;

/**
 * Author:  nick
 * Email:   nickdevp@gmail.com
 * Date:    15/12/29 16:47.
 * Description:
 */
public class NearByHouseActivity extends BaseSwipeBackActivity implements SwipeRefreshLayout.OnRefreshListener , View.OnClickListener{


    @InjectView(R.id.fragment_images_list_swipe_layout)
    XSwipeRefreshLayout mSwipeRefreshLayout;

    @InjectView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @InjectView(R.id.radiogroup)
    RadioGroup mRadioGroup;

    @InjectView(R.id.distance_btn)
    RadioButton distanceBtn;

    @InjectView(R.id.price_btn)
    RadioButton priceBtn;

    @InjectView(R.id.price_sort_img)
    ImageView priceSortImg;

    @InjectView(R.id.dis_sort_img)
    ImageView disSortImg;

    private StaggeredGridLayoutManager mLayoutManager;

    private int mCurrentPage = 1;

    private List<NearByHouseEntity> mListData = new ArrayList<NearByHouseEntity>();
    ;

   // private ListViewDataAdapter<NearByHouseEntity> mListViewAdapter = null;
    HouseResRecyclerAdapter mAdapter;

    private double latitude, longitude;

    private int order = 1;//区分价格高低排序1升,0降
    private int distanceOrder = 1;//区分距离高低排序

    private String distanceSort = "";
    private String priceSort = "0";
   // public int screenWidth,screenHeight;
    private int retTotalCnt;

    private boolean isLoadingMore;
    private boolean canLoadMore = true;

    @Override
    protected int getActionBarTitle() {
        return R.string.main_tab_name_nearby;
    }

    @Override
    protected boolean isApplyKitKatTranslucency() {
        return true;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {
        latitude = extras.getDouble("latitude");
        longitude = extras.getDouble("longitude");
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_nearby_house;
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

        mLayoutManager = new StaggeredGridLayoutManager(3,
                StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (canLoadMore)
                    NearByHouseActivity.this.onScrolled(recyclerView, dx, dy);
            }
        });

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        Intent intent = new Intent(mContext, HouseDetailActivity.class);
                        intent.putExtra("roomId", mListData.get(position).getRoomId());
                        intent.putExtra("longitude",String.valueOf(longitude));
                        intent.putExtra("latitude",String.valueOf(latitude));
                        startActivity(intent);
                    }
                })
        );

        mAdapter = new HouseResRecyclerAdapter(mContext, mListData);
        mRecyclerView.setAdapter(mAdapter);

        getData(mCurrentPage, distanceSort, priceSort);
    }


    private void getData(final int currentPage, String distanceRange, String sortType) {

        String url = ServerApiConstants.Urls.GET_NEARBY_HOUSE_LIST_URLS;

        Map<String, String> params = new HashMap<String, String>();
        params.put("pageNum", String.valueOf(currentPage));
        params.put("pageSize", "15");
        params.put("longitude", String.valueOf(longitude));
        params.put("latitude", String.valueOf(latitude));
        params.put("distanceRange", distanceRange);
        params.put("sortType", sortType);


        if (NetUtils.isNetworkConnected(mContext)) {
            OkHttpUtils
                    .get()
                    .url(url)
                    .params(params)
                    .build()
                    .execute(new NearByHouseListCallback() {
                        @Override
                        public void onError(Request request, Exception e) {
                            TLog.error(e.toString());
                        }

                        @Override
                        public void onResponse(NearByHouseListResult response) {

                            hideLoading();

                            if (response.getStatuscode().equals("1")) {
                                retTotalCnt = response.getData().getTotalCnt();
                                if (retTotalCnt > 0) {
//                                    if (response.getData().getList() != null && response.getData().getList().size()>0) {
//                                        mListData.addAll(response.getData().getList()) ;//= response.getData().getList();
//                                        mListViewAdapter.getDataList().addAll(mListData);
//                                        mListViewAdapter.notifyDataSetChanged();
//                                    }
//                                    if (retTotalCnt < currentPage * 15) {
//                                        mListView.setCanLoadMore(false);
//                                    }else {
//                                        mListView.setCanLoadMore(true);
//                                    }

                                    if (response.getData().getList().size() > 0) {
                                       // pageNumber = currentPage;
                                        mListData.addAll(response.getData().getList());
                                        mAdapter.notifyDataSetChanged();
                                        mSwipeRefreshLayout.setRefreshing(false);

                                    }else {
                                        UIHelper.showToast(mContext, "没有更多数据");
                                    }
                                    if (retTotalCnt < currentPage * 15) {
                                        canLoadMore = false;
                                    }else {
                                        canLoadMore =true;
                                    }
                                    isLoadingMore = false;
                                } else {
                                    UIHelper.showToast(mContext, "没有查询到数据");
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


//    @Override
//    public void onItemClick(PLAAdapterView<?> parent, View view, int position, long id) {
//        if (null != mListViewAdapter) {
//            if (position >= 0 && position < mListViewAdapter.getDataList().size()) {
//                Intent intent = new Intent(mContext, HouseDetailActivity.class);
//                intent.putExtra("roomId", mListData.get(position).getRoomId());
//                startActivity(intent);
//            }
//        }
//    }

//    @Override
//    public void onLoadMore() {
//        if (null != mListView) {
//            mListView.onLoadMoreComplete();
//        }
//
//      //  if (null != mListViewAdapter) {
//            mCurrentPage++;
//            getData(mCurrentPage, distanceSort, priceSort);
//     //   }
//
//     //   mListView.setCanLoadMore(true);
//    }
//
//    @Override
//    public void onRefresh() {
//        if (null != mSwipeRefreshLayout) {
//            mSwipeRefreshLayout.setRefreshing(false);
//        }
//        mListData.clear();
//        mListViewAdapter.getDataList().clear();
////        mListViewAdapter.getDataList().addAll(mListData);
////        mListViewAdapter.notifyDataSetChanged();
//        mCurrentPage = 1;
//        distanceSort = "";
//        priceSort = "0";
//        getData(mCurrentPage, distanceSort, priceSort);
//
//        mListView.setCanLoadMore(true);
//
//    }

    @Override
    public void onRefresh() {
        if (null != mSwipeRefreshLayout) {
            mSwipeRefreshLayout.setRefreshing(false);
        }

        mListData.clear();

   //     mAdapter.notifyDataSetChanged();

        mCurrentPage = 1;
//        distanceSort = "";
//        priceSort = "0";
        getData(mCurrentPage, distanceSort, priceSort);
    }

    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        int[] visibleItem = mLayoutManager.findLastVisibleItemPositions(null);
        int totalItemCount = mLayoutManager.getItemCount();
        //lastVisibleItem >= totalItemCount - 2 表示剩下2个item自动加载，各位自由选择
        // dy>0 表示向下滑动
        int lastitem = Math.max(visibleItem[0], visibleItem[1]);

        if (!isLoadingMore && lastitem >= totalItemCount - 4 && dy > 0) {
            isLoadingMore = true;
            mCurrentPage++;
            getData(mCurrentPage, distanceSort, priceSort);
        }

    }

    /**经纬度距离单位转换*/
    private String trans(double distance) {
        double dis = 0.0;
        if (distance >= 1000) {
            dis =  Math.round(distance/100d)/10d;
        }else {
            dis = Math.round(distance/100d)*100d;
            return String.valueOf((int)dis)+"m";
        }
        return Double.toString(dis)+"km";
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
        return true;
    }

    @Override
    protected TransitionMode getOverridePendingTransitionMode() {
        return TransitionMode.SCALE;
    }

    @Override
    @OnClick({R.id.distance_btn,R.id.price_btn})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.distance_btn:
                mSwipeRefreshLayout.setRefreshing(true);
             //   mListViewAdapter.getDataList().clear();
                mListData.clear();
                mCurrentPage = 1;
                if(distanceOrder == 1) {
                    priceSortImg.setVisibility(View.GONE);
                    disSortImg.setVisibility(View.VISIBLE);
                    disSortImg.setImageDrawable(getResources().getDrawable(R.drawable.homepage_list_price));
                    distanceOrder = 0;
                    distanceSort = "";
                    priceSort = "2";
                    getData(mCurrentPage, distanceSort, priceSort);
                }else {
                    priceSortImg.setVisibility(View.GONE);
                    disSortImg.setVisibility(View.VISIBLE);
                    disSortImg.setImageDrawable(getResources().getDrawable(R.drawable.homepage_list_price_down));
                    distanceOrder = 1;
                    distanceSort = "";
                    priceSort = "1";
                    getData(mCurrentPage, distanceSort, priceSort);
                }
                mSwipeRefreshLayout.setRefreshing(false);
                break;
            case R.id.price_btn:
                mSwipeRefreshLayout.setRefreshing(true);
             //   mListViewAdapter.getDataList().clear();
                mListData.clear();
                mCurrentPage = 1;
                if (order == 1) {
                    priceSortImg.setVisibility(View.VISIBLE);
                    disSortImg.setVisibility(View.GONE);
                    priceSortImg.setImageDrawable(getResources().getDrawable(R.drawable.homepage_list_price));
                    order = 0;
                    distanceSort = "";
                    priceSort = "4";
                    getData(mCurrentPage, distanceSort, priceSort);
                }else {
                    priceSortImg.setVisibility(View.VISIBLE);
                    disSortImg.setVisibility(View.GONE);
                    priceSortImg.setImageDrawable(getResources().getDrawable(R.drawable.homepage_list_price_down));
                    order = 1;
                    distanceSort = "";
                    priceSort = "3";
                    getData(mCurrentPage, distanceSort, priceSort);
                }
                mSwipeRefreshLayout.setRefreshing(false);
                break;
            default:
                break;
        }
    }
}
