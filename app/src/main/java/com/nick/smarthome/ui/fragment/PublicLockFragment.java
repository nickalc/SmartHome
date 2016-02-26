package com.nick.smarthome.ui.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.obsessive.library.eventbus.EventCenter;
import com.github.obsessive.library.widgets.XSwipeRefreshLayout;
import com.nick.smarthome.R;
import com.nick.smarthome.bean.MyLockEntity;
import com.nick.smarthome.ui.adapter.MyLockRecyclerAdapter;
import com.nick.smarthome.ui.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;


public class PublicLockFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,
        ObservableScrollViewCallbacks {

    private static final String ARG_POSITION = "position";

    private int position;
    /**
     * the page number
     */
    private int mCurrentPage = 0;

    private ObservableRecyclerView.LayoutManager mLayoutManager;
    private MyLockRecyclerAdapter mAdapter;
    private List<MyLockEntity> mLockListItems = new ArrayList<MyLockEntity>();

    @InjectView(R.id.swipe_refresh_widget)
    XSwipeRefreshLayout mSwipeRefreshLayout;

    @InjectView(R.id.m_recyclerview)
    ObservableRecyclerView mRecyclerView;



    public static PublicLockFragment newInstance(int position) {
        PublicLockFragment fragment = new PublicLockFragment();
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
        return R.layout.fragment_public_lock;
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

        mSwipeRefreshLayout.setColorSchemeColors(
                getResources().getColor(R.color.gplus_color_1),
                getResources().getColor(R.color.gplus_color_2),
                getResources().getColor(R.color.gplus_color_3),
                getResources().getColor(R.color.gplus_color_4));
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mLayoutManager = new LinearLayoutManager(getActivity());

        mRecyclerView.setScrollViewCallbacks(this);

        mRecyclerView.setLayoutManager(mLayoutManager);

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

        getNewsList(mAdapter, 0);

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
    private void getNewsList(MyLockRecyclerAdapter adapter,int currentPage) {

        MyLockEntity myLockEntity = new MyLockEntity();
        myLockEntity.setLockCode("江南新兴苑风之声");
        myLockEntity.setLockType("房间");
        myLockEntity.setDoorNo("1");
        mLockListItems.add(myLockEntity);

        MyLockEntity myLockEntity1 = new MyLockEntity();
        myLockEntity1.setLockCode("江南新兴苑风之声");
        myLockEntity1.setLockType("房间");
        myLockEntity1.setDoorNo("2");
        mLockListItems.add(myLockEntity1);

        MyLockEntity myLockEntity2 = new MyLockEntity();
        myLockEntity2.setLockCode("江南新兴苑风之声");
        myLockEntity2.setLockType("房间");
        myLockEntity2.setDoorNo("3");
        mLockListItems.add(myLockEntity2);


        mAdapter = new MyLockRecyclerAdapter(getActivity(),mLockListItems);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        mSwipeRefreshLayout.setRefreshing(false);

    }

    @Override
    public void onRefresh() {
        mLockListItems.clear();

        mCurrentPage = 0;

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