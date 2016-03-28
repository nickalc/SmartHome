//package com.nick.smarthome.ui.fragment;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v4.widget.SwipeRefreshLayout;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.WindowManager;
//import android.widget.ImageView;
//import android.widget.RadioButton;
//import android.widget.RadioGroup;
//import android.widget.TextView;
//
//import com.android.okhttp.OkHttpUtils;
//import com.github.obsessive.library.adapter.ListViewDataAdapter;
//import com.github.obsessive.library.adapter.ViewHolderBase;
//import com.github.obsessive.library.adapter.ViewHolderCreator;
//import com.github.obsessive.library.loading.VaryViewHelperController;
//import com.github.obsessive.library.netstatus.NetUtils;
//import com.github.obsessive.library.pla.PLAAbsListView;
//import com.github.obsessive.library.pla.PLAAdapterView;
//import com.github.obsessive.library.pla.PLAImageView;
//import com.github.obsessive.library.utils.CommonUtils;
//import com.github.obsessive.library.widgets.XSwipeRefreshLayout;
//import com.nick.smarthome.R;
//import com.nick.smarthome.api.ServerApiConstants;
//import com.nick.smarthome.bean.NearByHouseEntity;
//import com.nick.smarthome.bean.NearByHouseListResult;
//import com.nick.smarthome.callback.NearByHouseListCallback;
//import com.nick.smarthome.ui.activity.HouseDetailActivity;
//import com.nick.smarthome.utils.TLog;
//import com.nick.smarthome.utils.UIHelper;
//import com.nick.smarthome.widgets.PLALoadMoreListView;
//import com.nostra13.universalimageloader.core.ImageLoader;
//import com.squareup.okhttp.Request;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import butterknife.ButterKnife;
//import butterknife.InjectView;
//import butterknife.OnClick;
//
///**
// * Author:  nick
// * Email:   nickdevp@gmail.com
// * Date:    15/12/9 14:13.
// * Description: 附近
// */
//public class NearbyTestFragment extends Fragment implements PLALoadMoreListView.OnLoadMoreListener,
//        PLAAdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
//
//    @InjectView(R.id.fragment_images_list_swipe_layout)
//    XSwipeRefreshLayout mSwipeRefreshLayout;
//
////    @InjectView(R.id.fragment_house_list_view)
////    PLALoadMoreListView mListView;
//
//    @InjectView(R.id.radiogroup)
//    RadioGroup mRadioGroup;
//
//    @InjectView(R.id.distance_btn)
//    RadioButton distanceBtn;
//
//    @InjectView(R.id.price_btn)
//    RadioButton priceBtn;
//
//    @InjectView(R.id.price_sort_img)
//    ImageView priceSortImg;
//
//    @InjectView(R.id.dis_sort_img)
//    ImageView disSortImg;
//
//    private int mCurrentPage = 1;
//
//    private List<NearByHouseEntity> mListData = new ArrayList<NearByHouseEntity>();
//    ;
//
//    private ListViewDataAdapter<NearByHouseEntity> mListViewAdapter = null;
//
//    private View rootView;//缓存Fragment view
//
//    private VaryViewHelperController mVaryViewHelperController = null;
//
//    private int order = 1;//区分价格高低排序1升,0降
//    private int distanceOrder = 1;//区分距离高低排序
//
//    private String distanceSort = "";
//    private String priceSort = "0";
//    public int screenWidth, screenHeight;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//
//        TLog.log("", "call onCreate() method");
//        super.onCreate(savedInstanceState);
//    }
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//
//        if (rootView == null) {
//            rootView = inflater.inflate(R.layout.fragment_nearby, container, false);
//            ButterKnife.inject(this, rootView);
//            mVaryViewHelperController = new VaryViewHelperController(getLoadingTargetView());
//            initView(rootView);
//        }
//        //缓存的rootView需要判断是否已经被加过parent， 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
//        ViewGroup parent = (ViewGroup) rootView.getParent();
//        if (parent != null) {
//            parent.removeView(rootView);
//        }
//
//        return rootView;
//    }
//
//
//    protected View getLoadingTargetView() {
//        return mSwipeRefreshLayout;
//    }
//
//    protected void initView(View view) {
//
//        WindowManager wm = getActivity().getWindowManager();
//
//        screenWidth = wm.getDefaultDisplay().getWidth();
//        screenHeight = wm.getDefaultDisplay().getHeight();
//
////        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
////            @Override
////            public void onCheckedChanged(RadioGroup group, int checkedId) {
////                switch (checkedId) {
////                    case R.id.distance_btn:
////                        mSwipeRefreshLayout.setRefreshing(true);
////                        mListViewAdapter.getDataList().clear();
////                        mCurrentPage = 1;
////                        getData(mCurrentPage, "", "0");
////                        mSwipeRefreshLayout.setRefreshing(false);
////                        break;
////                    case R.id.price_btn:
////                        mSwipeRefreshLayout.setRefreshing(true);
////                        mListViewAdapter.getDataList().clear();
////                        mCurrentPage = 1;
////                        getData(mCurrentPage, "", "0");
////                        mSwipeRefreshLayout.setRefreshing(false);
////                        break;
////                }
////            }
////        });
//
//        toggleShowLoading(true, "正在加载...");
//
//        mListViewAdapter = new ListViewDataAdapter<NearByHouseEntity>(new ViewHolderCreator<NearByHouseEntity>() {
//            @Override
//            public ViewHolderBase<NearByHouseEntity> createViewHolder(int position) {
//                return new ViewHolderBase<NearByHouseEntity>() {
//
//                    PLAImageView mItemImage;
//                    TextView mHouseName;
//                    TextView mDistance;
//
//                    @Override
//                    public View createView(LayoutInflater layoutInflater) {
//                        View convertView = layoutInflater.inflate(R.layout.list_item_house_list, null);
//                        mItemImage = ButterKnife.findById(convertView, R.id.list_item_house_list_image);
//                        mHouseName = ButterKnife.findById(convertView, R.id.list_item_house_name);
//                        mDistance = ButterKnife.findById(convertView, R.id.list_item_house_distance);
//
//                        return convertView;
//                    }
//
//                    @Override
//                    public void showData(int position, NearByHouseEntity itemData) {
//
//                        String imageUrl = itemData.getPhotoUrl();
//                        String houseName = itemData.getHouseTitle();
//                        String distance = trans(itemData.getHouseDistance());//String.valueOf(itemData.getHouseDistance()) + "米";
//
//                        if (!CommonUtils.isEmpty(imageUrl)) {
//                            ImageLoader.getInstance().displayImage(imageUrl, mItemImage);
////                            Picasso.with(getActivity())
////                                    .load(imageUrl)
////                                    .placeholder(R.drawable.ic_loading)
////                                    .error(R.drawable.ic_loading_error)
//////                                    .resize(DensityUtils.dip2px(getActivity(), 250), DensityUtils.dip2px(getActivity(), 250))
//////                                    .centerCrop()
////                                    .tag(getActivity())
////                                    .into(mItemImage);
//                        }
//                        mHouseName.setText(houseName);
//                        mDistance.setText(distance);
//
//                        mItemImage.setImageWidth(screenWidth / 3);
//                        mItemImage.setImageHeight(screenWidth / 3);
//                    }
//                };
//            }
//        });
//
//        mListView.setOnItemClickListener(this);
//        mListView.setOnLoadMoreListener(this);
//        mListViewAdapter.getDataList().addAll(mListData);
//        mListView.setAdapter(mListViewAdapter);
//
//        getData(mCurrentPage, distanceSort, priceSort);
//
//        mSwipeRefreshLayout.setColorSchemeColors(
//                getResources().getColor(R.color.gplus_color_1),
//                getResources().getColor(R.color.gplus_color_2),
//                getResources().getColor(R.color.gplus_color_3),
//                getResources().getColor(R.color.gplus_color_4));
//        mSwipeRefreshLayout.setOnRefreshListener(this);
//
//        mListView.setOnScrollListener(new PLAAbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(PLAAbsListView view, int scrollState) {
//                if (scrollState == PLAAbsListView.OnScrollListener.SCROLL_STATE_IDLE)
//                {
//                   // Picasso.with(getActivity()).resumeTag(getActivity());
//                    ImageLoader.getInstance().resume();
//                }
//                else
//                {
//                   // Picasso.with(getActivity()).pauseTag(getActivity());
//                    ImageLoader.getInstance().pause();
//                }
//            }
//
//            @Override
//            public void onScroll(PLAAbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//
//            }
//        });
//    }
//
//    private void getData(final int currentPage, String distanceRange, String sortType) {
//
//        String url = ServerApiConstants.Urls.GET_NEARBY_HOUSE_LIST_URLS;
//
//        SharedPreferences settings = getActivity().getSharedPreferences("secrecy", Activity.MODE_PRIVATE);
//        String latitude = settings.getString("latitude", "");
//        String longitude = settings.getString("longitude", "");
//
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("pageNum", String.valueOf(currentPage));
//        params.put("pageSize", "15");
//        params.put("longitude", longitude);
//        params.put("latitude", latitude);
//        params.put("distanceRange", distanceRange);
//        params.put("sortType", sortType);
//
//        if (NetUtils.isNetworkConnected(getActivity())) {
//            OkHttpUtils
//                    .get()
//                    .url(url)
//                    .params(params)
//                    .build()
//                    .execute(new NearByHouseListCallback() {
//                        @Override
//                        public void onError(Request request, Exception e) {
//                            TLog.error(e.toString());
//                            toggleShowLoading(false, null);
//                        }
//
//                        @Override
//                        public void onResponse(NearByHouseListResult response) {
//                            toggleShowLoading(false, null);
//                            if (response.getStatuscode().equals("1")) {
//                                int totalCnt = response.getData().getTotalCnt();
//                                int totalPage = response.getData().getTotalPage();
//                                if (totalCnt > 0) {
//                                    mListData = response.getData().getList();
//                                    mListViewAdapter.getDataList().addAll(mListData);
//                                    mListViewAdapter.notifyDataSetChanged();
//                                    if (totalCnt < currentPage * 15) {
//                                        mListView.setCanLoadMore(false);
//                                    }
//                                } else {
//                                    toggleShowEmpty(true, "没有查询到数据", new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View v) {
//                                            onRefresh();
//                                        }
//                                    });
//                                }
//                            } else {
//                                UIHelper.showToast(getActivity(), response.getMessage());
//                            }
//                        }
//                    });
//        } else {
//            toggleShowLoading(false, null);
//            toggleNetworkError(true, new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    onRefresh();
//                }
//            });
//        }
//
//    }
//
//
//    @Override
//    public void onItemClick(PLAAdapterView<?> parent, View view, int position, long id) {
//        if (null != mListViewAdapter) {
//            if (position >= 0 && position < mListViewAdapter.getDataList().size()) {
//                Intent intent = new Intent(getActivity(), HouseDetailActivity.class);
//                intent.putExtra("roomId", mListData.get(position).getRoomId());
//                getActivity().startActivity(intent);
//            }
//        }
//    }
//
//    @Override
//    public void onLoadMore() {
//        if (null != mListView) {
//            mListView.onLoadMoreComplete();
//        }
//
//        if (null != mListViewAdapter) {
//            mCurrentPage++;
//            getData(mCurrentPage, distanceSort, priceSort);
//        }
//
//        mListView.setCanLoadMore(true);
//    }
//
//    @Override
//    public void onRefresh() {
//        if (null != mSwipeRefreshLayout) {
//            mSwipeRefreshLayout.setRefreshing(false);
//        }
//        mListViewAdapter.getDataList().clear();
//
//        mCurrentPage = 1;
//        distanceSort = "";
//        priceSort = "0";
//        getData(mCurrentPage, distanceSort, priceSort);
//
//        mListView.setCanLoadMore(true);
//
//    }
//
//    /**
//     * 经纬度距离单位转换
//     */
//    private String trans(double distance) {
//        double dis = 0.0;
//        if (distance >= 1000) {
//            dis = Math.round(distance / 100d) / 10d;
//        } else {
//            dis = Math.round(distance / 100d) * 100d;
//            return String.valueOf((int) dis) + "m";
//        }
//        return Double.toString(dis) + "km";
//    }
//
//
//    /**
//     * toggle show loading
//     *
//     * @param toggle
//     */
//    protected void toggleShowLoading(boolean toggle, String msg) {
//        if (null == mVaryViewHelperController) {
//            throw new IllegalArgumentException("You must return a right target view for loading");
//        }
//
//        if (toggle) {
//            mVaryViewHelperController.showLoading(msg);
//        } else {
//            mVaryViewHelperController.restore();
//        }
//    }
//
//    /**
//     * toggle show empty
//     *
//     * @param toggle
//     */
//    protected void toggleShowEmpty(boolean toggle, String msg, View.OnClickListener onClickListener) {
//        if (null == mVaryViewHelperController) {
//            throw new IllegalArgumentException("You must return a right target view for loading");
//        }
//
//        if (toggle) {
//            mVaryViewHelperController.showEmpty(msg, onClickListener);
//        } else {
//            mVaryViewHelperController.restore();
//        }
//    }
//
//    /**
//     * toggle show error
//     *
//     * @param toggle
//     */
//    protected void toggleShowError(boolean toggle, String msg, View.OnClickListener onClickListener) {
//        if (null == mVaryViewHelperController) {
//            throw new IllegalArgumentException("You must return a right target view for loading");
//        }
//
//        if (toggle) {
//            mVaryViewHelperController.showError(msg, onClickListener);
//        } else {
//            mVaryViewHelperController.restore();
//        }
//    }
//
//    /**
//     * toggle show network error
//     *
//     * @param toggle
//     */
//    protected void toggleNetworkError(boolean toggle, View.OnClickListener onClickListener) {
//        if (null == mVaryViewHelperController) {
//            throw new IllegalArgumentException("You must return a right target view for loading");
//        }
//
//        if (toggle) {
//            mVaryViewHelperController.showNetworkError(onClickListener);
//        } else {
//            mVaryViewHelperController.restore();
//        }
//    }
//
//    @Override
//    @OnClick({R.id.distance_btn, R.id.price_btn})
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.distance_btn:
//                mSwipeRefreshLayout.setRefreshing(true);
//                mListViewAdapter.getDataList().clear();
//                mCurrentPage = 1;
//                if (distanceOrder == 1) {
//                    priceSortImg.setVisibility(View.GONE);
//                    disSortImg.setVisibility(View.VISIBLE);
//                    disSortImg.setImageDrawable(getResources().getDrawable(R.drawable.homepage_list_price));
//                    distanceOrder = 0;
//                    distanceSort = "";
//                    priceSort = "2";
//                    getData(mCurrentPage, distanceSort, priceSort);
//                } else {
//                    priceSortImg.setVisibility(View.GONE);
//                    disSortImg.setVisibility(View.VISIBLE);
//                    disSortImg.setImageDrawable(getResources().getDrawable(R.drawable.homepage_list_price_down));
//                    distanceOrder = 1;
//                    distanceSort = "";
//                    priceSort = "1";
//                    getData(mCurrentPage, distanceSort, priceSort);
//                }
//                mSwipeRefreshLayout.setRefreshing(false);
//                break;
//            case R.id.price_btn:
//                mSwipeRefreshLayout.setRefreshing(true);
//                mListViewAdapter.getDataList().clear();
//                mCurrentPage = 1;
//                if (order == 1) {
//                    priceSortImg.setVisibility(View.VISIBLE);
//                    disSortImg.setVisibility(View.GONE);
//                    priceSortImg.setImageDrawable(getResources().getDrawable(R.drawable.homepage_list_price));
//                    order = 0;
//                    distanceSort = "";
//                    priceSort = "4";
//                    getData(mCurrentPage, distanceSort, priceSort);
//                } else {
//                    priceSortImg.setVisibility(View.VISIBLE);
//                    disSortImg.setVisibility(View.GONE);
//                    priceSortImg.setImageDrawable(getResources().getDrawable(R.drawable.homepage_list_price_down));
//                    order = 1;
//                    distanceSort = "";
//                    priceSort = "3";
//                    getData(mCurrentPage, distanceSort, priceSort);
//                }
//                mSwipeRefreshLayout.setRefreshing(false);
//                break;
//            default:
//                break;
//        }
//    }
//}
