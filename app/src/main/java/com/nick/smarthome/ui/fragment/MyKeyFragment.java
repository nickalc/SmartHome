package com.nick.smarthome.ui.fragment;

import android.app.Activity;
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
import com.nick.smarthome.bean.MyKeyListResult;
import com.nick.smarthome.callback.MyKeyListCallback;
import com.nick.smarthome.ui.adapter.MyKeyRecyclerAdapter;
import com.nick.smarthome.ui.base.BaseFragment;
import com.nick.smarthome.utils.TLog;
import com.nick.smarthome.utils.UIHelper;
import com.squareup.okhttp.Request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.InjectView;


public class MyKeyFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,
        ObservableScrollViewCallbacks {

    private static final String ARG_POSITION = "position";

    private int position;

    private int mCurrentPage = 1;

    private String mCustomerId;

    private ObservableRecyclerView.LayoutManager mLayoutManager;
    private MyKeyRecyclerAdapter mAdapter;
    private List<MyKeyListResult.DataEntity.ListEntity> mKeyListItems = new ArrayList<MyKeyListResult.DataEntity.ListEntity>();

    @InjectView(R.id.swipe_refresh_widget)
    XSwipeRefreshLayout mSwipeRefreshLayout;

    @InjectView(R.id.m_recyclerview)
    ObservableRecyclerView mRecyclerView;



    public static MyKeyFragment newInstance(int position) {
        MyKeyFragment fragment = new MyKeyFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_POSITION, position);
        fragment.setArguments(bundle);
        return fragment;
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        position = getArguments().getInt(ARG_POSITION);
//        View rootView = inflater.inflate(R.layout.fragment_my_lock_list, container, false);
//
//        return rootView;
//    }

    @Override
    protected int getContentViewLayoutID() {
        position = getArguments().getInt(ARG_POSITION);
        return R.layout.fragment_my_key_list;
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

        showLoading(mContext.getString(R.string.common_loading_message));

        SharedPreferences settings = getActivity().getSharedPreferences("secrecy", Activity.MODE_PRIVATE);
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
                    // getNewsList(mAdapter, mCurrentPage, false);
                }
            }
        });

    }

    /**
     * @param adapter
     * @param currentPage
     */
    private void getNewsList(MyKeyRecyclerAdapter adapter,int currentPage) {

        String url = ServerApiConstants.Urls.GET_MY_KEY_LIST_URLS;

        Map<String, String> params = new HashMap<String,String>();
        params.put("customerId", mCustomerId);
        params.put("pageNum",String.valueOf(currentPage));
        params.put("pageSize", "15");


        if (NetUtils.isNetworkConnected(mContext)) {
            OkHttpUtils
                    .get()
                    .url(url)
                    .params(params)
                    .build()
                    .execute(new MyKeyListCallback() {
                        @Override
                        public void onError(Request request, Exception e) {
                            TLog.error(e.toString());
                        }

                        @Override
                        public void onResponse(MyKeyListResult response) {

                            hideLoading();

                            if (response.getStatuscode().equals("1")) {

                                int totalCnt = response.getData().getTotalCnt();
                                if (totalCnt > 0) {
                                    mKeyListItems = response.getData().getList();
                                    mAdapter = new MyKeyRecyclerAdapter(getActivity(),mKeyListItems);
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
        mKeyListItems.clear();

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