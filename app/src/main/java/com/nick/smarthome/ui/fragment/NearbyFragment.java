package com.nick.smarthome.ui.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.obsessive.library.adapter.ListViewDataAdapter;
import com.github.obsessive.library.adapter.ViewHolderBase;
import com.github.obsessive.library.adapter.ViewHolderCreator;
import com.github.obsessive.library.eventbus.EventCenter;
import com.github.obsessive.library.netstatus.NetUtils;
import com.github.obsessive.library.pla.PLAAdapterView;
import com.github.obsessive.library.widgets.XSwipeRefreshLayout;
import com.nick.smarthome.R;
import com.nick.smarthome.bean.HouseListEntity;
import com.nick.smarthome.ui.base.BaseFragment;
import com.nick.smarthome.utils.UIHelper;
import com.nick.smarthome.widgets.PLALoadMoreListView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Author:  nick
 * Email:   nickdevp@gmail.com
 * Date:    15/12/9 14:13.
 * Description: 附近
 */
public class NearbyFragment extends BaseFragment implements PLALoadMoreListView.OnLoadMoreListener,
        PLAAdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener{

    @InjectView(R.id.fragment_images_list_swipe_layout)
    XSwipeRefreshLayout mSwipeRefreshLayout;

    @InjectView(R.id.fragment_house_list_view)
    PLALoadMoreListView mListView;

    /**
     * the page number
     */
    private int mCurrentPage = 0;

    private List<HouseListEntity> mListData = null;

    private ListViewDataAdapter<HouseListEntity> mListViewAdapter = null;

    @Override
    protected void onFirstUserVisible() {

        if (NetUtils.isNetworkConnected(mContext)) {

        }else {

        }


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

        mListData = new ArrayList<HouseListEntity>();
        for (int i = 0; i < 15; i++) {
            HouseListEntity houseListEntity = new HouseListEntity();
            houseListEntity.setImageUrl("http://120.25.164.72/resources/houseimg.png");
            houseListEntity.setHouseName("房屋名称" + i);
            houseListEntity.setDistance(100*i+"m");
            mListData.add(houseListEntity);
        }


       // mListViewAdapter.notifyDataSetChanged();

        mListViewAdapter = new ListViewDataAdapter<HouseListEntity>(new ViewHolderCreator<HouseListEntity>() {
            @Override
            public ViewHolderBase<HouseListEntity> createViewHolder(int position) {
                return new ViewHolderBase<HouseListEntity>() {

                    ImageView mItemImage;
                    TextView mHouseName;
                    TextView mDistance;

                    @Override
                    public View createView(LayoutInflater layoutInflater) {
                        View convertView = layoutInflater.inflate(R.layout.list_item_house_list, null);
                        mItemImage = ButterKnife.findById(convertView, R.id.list_item_house_list_image);
                        mHouseName = ButterKnife.findById(convertView,R.id.list_item_house_name);
                        mDistance = ButterKnife.findById(convertView,R.id.list_item_house_distance);

                        return convertView;
                    }

                    @Override
                    public void showData(int position, HouseListEntity itemData) {

                        String imageUrl = itemData.getImageUrl();
                        String houseName = itemData.getHouseName();
                        String distance = itemData.getDistance();

                        Picasso.with(mContext)
                                .load(imageUrl)
                                .placeholder(me.nereo.multi_image_selector.R.drawable.default_error)
                                .into(mItemImage);
                        mHouseName.setText(houseName);
                        mDistance.setText(distance);

                      //  mItemImage.setImageWidth(width);
                       // mItemImage.setImageHeight(height);
                    }
                };
            }
        });

        mListView.setOnItemClickListener(this);
        mListView.setOnLoadMoreListener(this);
        mListViewAdapter.getDataList().addAll(mListData);
        mListView.setAdapter(mListViewAdapter);

        mSwipeRefreshLayout.setColorSchemeColors(
                getResources().getColor(R.color.gplus_color_1),
                getResources().getColor(R.color.gplus_color_2),
                getResources().getColor(R.color.gplus_color_3),
                getResources().getColor(R.color.gplus_color_4));
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_nearby;
    }

    @Override
    protected void onEventComming(EventCenter eventCenter) {

    }

    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

    @Override
    public void onItemClick(PLAAdapterView<?> parent, View view, int position, long id) {

        UIHelper.showToast(getActivity(),"点击"+position);
    }

    @Override
    public void onLoadMore() {
        if (null != mListView) {
            mListView.onLoadMoreComplete();
        }

        if (null != mListViewAdapter) {
            mListViewAdapter.getDataList().addAll(mListData);
            mListViewAdapter.notifyDataSetChanged();
        }

        mListView.setCanLoadMore(true);
    }

    @Override
    public void onRefresh() {
        if (null != mSwipeRefreshLayout) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
        mListViewAdapter.getDataList().clear();
        mListViewAdapter.getDataList().addAll(mListData);
        mListViewAdapter.notifyDataSetChanged();

        mListView.setCanLoadMore(true);

    }
}
