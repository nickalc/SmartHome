package com.nick.smarthome.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.okhttp.OkHttpUtils;
import com.android.okhttp.callback.StringCallback;
import com.github.obsessive.library.eventbus.EventCenter;
import com.github.obsessive.library.netstatus.NetUtils;
import com.github.obsessive.library.utils.CommonUtils;
import com.nick.smarthome.R;
import com.nick.smarthome.api.ServerApiConstants;
import com.nick.smarthome.bean.HouseResourceDeatailResult;
import com.nick.smarthome.bean.OrderDateEntity;
import com.nick.smarthome.bean.OrderTimeEntity;
import com.nick.smarthome.callback.HouseResourceDetailCallback;
import com.nick.smarthome.ui.base.BaseSwipeBackActivity;
import com.nick.smarthome.utils.DialogHelp;
import com.nick.smarthome.utils.TLog;
import com.nick.smarthome.utils.UIHelper;
import com.nick.smarthome.widgets.FullyLinearLayoutManager;
import com.nick.smarthome.widgets.MyLinearLayoutManager;
import com.nick.smarthome.widgets.MyScrollView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.okhttp.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Author:  nick
 * Email:   nickdevp@gmail.com
 * Date:    15/12/29 22:36.
 * Description:
 */
public class HouseDetailActivity extends BaseSwipeBackActivity implements View.OnClickListener {

    @InjectView(R.id.scrollView)
    MyScrollView scrollView;

    @InjectView(R.id.tv_house_title)
    TextView tvHouseTitle;

    @InjectView(R.id.tv_house_address)
    TextView tvHouseAddr;

    @InjectView(R.id.tv_house_price)
    TextView tvHousePrice;

    @InjectView(R.id.room_type)
    TextView roomType;

    @InjectView(R.id.tv_room_no)
    TextView tvRoomNo;

    @InjectView(R.id.date_recyclerview)
    RecyclerView dateRecyclerview;

    @InjectView(R.id.time_recyclerview)
    RecyclerView timeRecyclerview;

    @InjectView(R.id.btn_do_order)
    TextView doOrderBtn;

    @InjectView(R.id.viewPager)
    ViewPager viewPager;

    @InjectView(R.id.indicator_tv)
    TextView indicatorTV;

    private List<View> mDefaultViews;
    private int roomId;

    ProgressDialog waitDialog;

    ProgressDialog mDialog;

    // private DateRecyclerAdapter dateRecyclerAdapter;

    private MyDateAdapter myDateAdapter;

    private MyTimeAdapter myTimeAdapter;

    // private TimeRecyclerAdapter timeRecyclerAdapter;

    private RecyclerView.LayoutManager mDateLayoutManager;

    private RecyclerView.LayoutManager mTimeLayoutManager;

    private FullyLinearLayoutManager fullyLinearLayoutManager;

    private MyLinearLayoutManager myLinearLayoutManager;

    ArrayList<OrderDateEntity> initialDateList = new ArrayList<OrderDateEntity>();

    ArrayList<OrderTimeEntity> initialTimeList = new ArrayList<OrderTimeEntity>();

    ArrayList<ArrayList<OrderTimeEntity>> allTimeItems = new ArrayList<ArrayList<OrderTimeEntity>>();

    HouseResourceDeatailResult.DataEntity.RnRoomInfoMapEntity rnRoomInfoMapEntity;

    ArrayList<HouseResourceDeatailResult.DataEntity.RoomTimePriceListEntity> roomTimePriceList;

    ArrayList<HouseResourceDeatailResult.DataEntity.RoomImgListEntity> roomImgList;

    ArrayList<HouseResourceDeatailResult.DataEntity.HaveBusinessDateSgementListEntity> sellOutTimeList;

    ArrayList<HouseResourceDeatailResult.DataEntity.NoBusinessDateSgementListEntity> NoBusinessDateList;

    private List<HashMap<String, Object>> optionalDateList = new ArrayList<HashMap<String, Object>>();

    private String mCustomerId;

    private int totalPrice = 0;

    private int[] A;

    private LayoutInflater inflater;
    private View item;
    private ImageView image;
    private MyAdapter adapter;// viewpager适配器

//    protected int getActionBarTitle() {
//        return R.string.detail;
//    }

    @Override
    protected boolean isApplyKitKatTranslucency() {
        return true;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {
        roomId = extras.getInt("roomId");

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_house_detail;
    }

    @Override
    protected void onEventComming(EventCenter eventCenter) {

    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void initViewsAndEvents() {

        waitDialog = DialogHelp.getWaitDialog(this, "正在加载数据...");
        mDialog = DialogHelp.getWaitDialog(this, "正在提交数据...");

        //设置布局管理器
        mDateLayoutManager = new LinearLayoutManager(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        dateRecyclerview.setLayoutManager(linearLayoutManager);

        mTimeLayoutManager = new LinearLayoutManager(this);

        fullyLinearLayoutManager = new FullyLinearLayoutManager(this);
        fullyLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        fullyLinearLayoutManager.setSmoothScrollbarEnabled(true);

        myLinearLayoutManager = new MyLinearLayoutManager(this);

        initDatas();

        //    getDate();

        //   getTimePriceDate();

        timeRecyclerview.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                int action = e.getAction();
                switch (action) {
                    case MotionEvent.ACTION_MOVE:
                        rv.getParent().requestDisallowInterceptTouchEvent(true);
                        break;
                    case MotionEvent.ACTION_UP:
                        rv.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
    }

    /**
     * 初始化可下单日期
     */
    private void getDate() {

        for (int i = 0; i < 7; i++) {
            OrderDateEntity orderDateEntity = new OrderDateEntity();
            orderDateEntity.setDateId(i);
            orderDateEntity.setShowDate(getNowOrNextDay(i).get("showDate"));
            orderDateEntity.setDate(getNowOrNextDay(i).get("fullDate"));
            orderDateEntity.setIsSelect(false);
            initialDateList.add(orderDateEntity);
        }

        myDateAdapter = new MyDateAdapter(initialDateList);
        dateRecyclerview.setAdapter(myDateAdapter);
        myDateAdapter.notifyDataSetChanged();
        myDateAdapter.setOnItemClickListener(new MyDateAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, Object data) {
                final OrderDateEntity orderDateEntity = (OrderDateEntity) data;

                int dateId = orderDateEntity.getDateId();

                int len = initialDateList.size();
                for (int i = 0; i < len; i++) {
                    OrderDateEntity dateEntity = initialDateList.get(i);
                    if (i == dateId) {
                        dateEntity.setIsSelect(true);
                    } else {
                        dateEntity.setIsSelect(false);
                    }
                }

                myTimeAdapter = new MyTimeAdapter(allTimeItems.get(dateId));
                timeRecyclerview.setAdapter(myTimeAdapter);
                myTimeAdapter.setOnItemClickListener(new MyTimeAdapter.OnRecyclerViewItemClickListener() {
                    @Override
                    public void onItemClick(View view, Object data) {
                        OrderTimeEntity ordertimeEntity = (OrderTimeEntity) data;

                        int dateId = ordertimeEntity.getDateId();
                        String selectedDate = initialDateList.get(dateId).getSelectedDate();
                        if (!CommonUtils.isEmpty(selectedDate) && ordertimeEntity.isSelect() && !"7".equals(ordertimeEntity.getTimeId())) {

                            int currSelectTimes = Integer.valueOf(initialDateList.get(dateId).getSelectedDate());
                            initialDateList.get(dateId).setSelectedDate(String.valueOf(currSelectTimes + 2));
                            initialDateList.get(dateId).setIsSelctNight(false);

                        } else if (!CommonUtils.isEmpty(selectedDate) && !ordertimeEntity.isSelect() && !"7".equals(ordertimeEntity.getTimeId())) {

                            int currSelectTimes = Integer.valueOf(initialDateList.get(dateId).getSelectedDate());
                            initialDateList.get(dateId).setSelectedDate(String.valueOf(currSelectTimes - 2));
                            initialDateList.get(dateId).setIsSelctNight(false);

                        } else if (!CommonUtils.isEmpty(selectedDate) && ordertimeEntity.isSelect() && "7".equals(ordertimeEntity.getTimeId())) {

                            int currSelectTimes = Integer.valueOf(initialDateList.get(dateId).getSelectedDate());
                            initialDateList.get(dateId).setSelectedDate(String.valueOf(currSelectTimes + 12));
                            initialDateList.get(dateId).setIsSelctNight(true);

                        } else if (!CommonUtils.isEmpty(selectedDate) && !ordertimeEntity.isSelect() && "7".equals(ordertimeEntity.getTimeId())) {

                            int currSelectTimes = Integer.valueOf(initialDateList.get(dateId).getSelectedDate());
                            initialDateList.get(dateId).setSelectedDate(String.valueOf(currSelectTimes - 12));
                            initialDateList.get(dateId).setIsSelctNight(false);

                        } else if (CommonUtils.isEmpty(selectedDate) && !"7".equals(ordertimeEntity.getTimeId())) {

                            initialDateList.get(dateId).setSelectedDate("2");
                            initialDateList.get(dateId).setIsSelctNight(false);

                        } else if (CommonUtils.isEmpty(selectedDate) && "7".equals(ordertimeEntity.getTimeId())) {

                            initialDateList.get(dateId).setSelectedDate("12");
                            initialDateList.get(dateId).setIsSelctNight(true);

                        } else {
                            initialDateList.get(dateId).setSelectedDate("2");
                            initialDateList.get(dateId).setIsSelctNight(false);
                        }

                        //calculateTotalPrice();
                        calculateNewTotalPrice();

                        myDateAdapter.notifyDataSetChanged();
                    }
                });
                myTimeAdapter.notifyDataSetChanged();
            }
        });
    }


    /**
     * 初始化可下单日期
     */
    private void getDate(List<String> list) {

        // List<HashMap<String, Object>> optionalDateList = new ArrayList<HashMap<String, Object>>();
        for (int m = 0; m < 7 + list.size(); m++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("showDate", getNowOrNextDay(m).get("showDate"));
            map.put("fullDate", getNowOrNextDay(m).get("fullDate"));
            optionalDateList.add(map);
        }

        if (list.size() > 0) {
            // for (int n = 0; n < optionalDateList.size(); n++) {
            for (int n = optionalDateList.size() - 1; n >= 0; n--) {
                HashMap<String, Object> map = optionalDateList.get(n);
                if (list.contains(map.get("fullDate"))) {
                    optionalDateList.remove(map);
                }
            }
        }

        for (int i = 0; i < 7; i++) {
            OrderDateEntity orderDateEntity = new OrderDateEntity();
            orderDateEntity.setDateId(i);
            orderDateEntity.setShowDate(optionalDateList.get(i).get("showDate").toString());
            orderDateEntity.setDate(optionalDateList.get(i).get("fullDate").toString());
            orderDateEntity.setIsSelect(false);
            initialDateList.add(orderDateEntity);
        }

        myDateAdapter = new MyDateAdapter(initialDateList);
        dateRecyclerview.setAdapter(myDateAdapter);
        myDateAdapter.notifyDataSetChanged();
        myDateAdapter.setOnItemClickListener(new MyDateAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, Object data) {
                final OrderDateEntity orderDateEntity = (OrderDateEntity) data;

                int dateId = orderDateEntity.getDateId();

                int len = initialDateList.size();
                for (int i = 0; i < len; i++) {
                    OrderDateEntity dateEntity = initialDateList.get(i);
                    if (i == dateId) {
                        dateEntity.setIsSelect(true);
                    } else {
                        dateEntity.setIsSelect(false);
                    }
                }

                myTimeAdapter = new MyTimeAdapter(allTimeItems.get(dateId));
                timeRecyclerview.setAdapter(myTimeAdapter);
                myTimeAdapter.setOnItemClickListener(new MyTimeAdapter.OnRecyclerViewItemClickListener() {
                    @Override
                    public void onItemClick(View view, Object data) {
                        OrderTimeEntity ordertimeEntity = (OrderTimeEntity) data;

                        int dateId = ordertimeEntity.getDateId();
                        String selectedDate = initialDateList.get(dateId).getSelectedDate();
                        if (!CommonUtils.isEmpty(selectedDate) && ordertimeEntity.isSelect() && !"7".equals(ordertimeEntity.getTimeId())) {

                            int currSelectTimes = Integer.valueOf(initialDateList.get(dateId).getSelectedDate());
                            initialDateList.get(dateId).setSelectedDate(String.valueOf(currSelectTimes + 2));
                            initialDateList.get(dateId).setIsSelctNight(false);

                        } else if (!CommonUtils.isEmpty(selectedDate) && !ordertimeEntity.isSelect() && !"7".equals(ordertimeEntity.getTimeId())) {

                            int currSelectTimes = Integer.valueOf(initialDateList.get(dateId).getSelectedDate());
                            initialDateList.get(dateId).setSelectedDate(String.valueOf(currSelectTimes - 2));
                            initialDateList.get(dateId).setIsSelctNight(false);

                        } else if (!CommonUtils.isEmpty(selectedDate) && ordertimeEntity.isSelect() && "7".equals(ordertimeEntity.getTimeId())) {

                            int currSelectTimes = Integer.valueOf(initialDateList.get(dateId).getSelectedDate());
                            initialDateList.get(dateId).setSelectedDate(String.valueOf(currSelectTimes + 12));
                            initialDateList.get(dateId).setIsSelctNight(true);

                        } else if (!CommonUtils.isEmpty(selectedDate) && !ordertimeEntity.isSelect() && "7".equals(ordertimeEntity.getTimeId())) {

                            int currSelectTimes = Integer.valueOf(initialDateList.get(dateId).getSelectedDate());
                            initialDateList.get(dateId).setSelectedDate(String.valueOf(currSelectTimes - 12));
                            initialDateList.get(dateId).setIsSelctNight(false);

                        } else if (CommonUtils.isEmpty(selectedDate) && !"7".equals(ordertimeEntity.getTimeId())) {

                            initialDateList.get(dateId).setSelectedDate("2");
                            initialDateList.get(dateId).setIsSelctNight(false);

                        } else if (CommonUtils.isEmpty(selectedDate) && "7".equals(ordertimeEntity.getTimeId())) {

                            initialDateList.get(dateId).setSelectedDate("12");
                            initialDateList.get(dateId).setIsSelctNight(true);

                        } else {
                            initialDateList.get(dateId).setSelectedDate("2");
                            initialDateList.get(dateId).setIsSelctNight(false);
                        }

                        //calculateTotalPrice();
                        calculateNewTotalPrice();

                        myDateAdapter.notifyDataSetChanged();
                    }
                });
                myTimeAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * 初始化下单时间段
     */
    private void getTimePriceDate() {

        String[] arrays = getResources().getStringArray(R.array.orderTimeBucket);

        Date date = new Date();
        int len = arrays.length;

        for (int i = 0; i < 7; i++) {
            ArrayList<OrderTimeEntity> timeList = new ArrayList<OrderTimeEntity>();
            for (int j = 0; j < len; j++) {
                OrderTimeEntity orderTimeEntity = new OrderTimeEntity();
                orderTimeEntity.setDate(optionalDateList.get(i).get("fullDate").toString());
                orderTimeEntity.setDateId(i);
                orderTimeEntity.setTimeId(String.valueOf(j + 1));
                orderTimeEntity.setTime(arrays[j]);
                orderTimeEntity.setIsSelect(false);
                initialTimeList.add(orderTimeEntity);
                timeList.add(orderTimeEntity);
            }
            allTimeItems.add(timeList);
        }

        timeRecyclerview.setHasFixedSize(true);
        timeRecyclerview.setLayoutManager(myLinearLayoutManager);//fullyLinearLayoutManager);//mTimeLayoutManager);
        myTimeAdapter = new MyTimeAdapter(allTimeItems.get(0));
        timeRecyclerview.setAdapter(myTimeAdapter);
        myTimeAdapter.setOnItemClickListener(new MyTimeAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, Object data) {
                OrderTimeEntity ordertimeEntity = (OrderTimeEntity) data;

                int dateId = ordertimeEntity.getDateId();
                String selectedDate = initialDateList.get(dateId).getSelectedDate();
                if (!CommonUtils.isEmpty(selectedDate) && ordertimeEntity.isSelect() && !"7".equals(ordertimeEntity.getTimeId())) {

                    int currSelectTimes = Integer.valueOf(initialDateList.get(dateId).getSelectedDate());
                    initialDateList.get(dateId).setSelectedDate(String.valueOf(currSelectTimes + 2));
                    initialDateList.get(dateId).setIsSelctNight(false);

                } else if (!CommonUtils.isEmpty(selectedDate) && !ordertimeEntity.isSelect() && !"7".equals(ordertimeEntity.getTimeId())) {

                    int currSelectTimes = Integer.valueOf(initialDateList.get(dateId).getSelectedDate());
                    initialDateList.get(dateId).setSelectedDate(String.valueOf(currSelectTimes - 2));
                    initialDateList.get(dateId).setIsSelctNight(false);

                } else if (!CommonUtils.isEmpty(selectedDate) && ordertimeEntity.isSelect() && "7".equals(ordertimeEntity.getTimeId())) {

                    int currSelectTimes = Integer.valueOf(initialDateList.get(dateId).getSelectedDate());
                    initialDateList.get(dateId).setSelectedDate(String.valueOf(currSelectTimes + 12));
                    initialDateList.get(dateId).setIsSelctNight(true);

                } else if (!CommonUtils.isEmpty(selectedDate) && !ordertimeEntity.isSelect() && "7".equals(ordertimeEntity.getTimeId())) {

                    int currSelectTimes = Integer.valueOf(initialDateList.get(dateId).getSelectedDate());
                    initialDateList.get(dateId).setSelectedDate(String.valueOf(currSelectTimes - 12));
                    initialDateList.get(dateId).setIsSelctNight(false);

                } else if (CommonUtils.isEmpty(selectedDate) && !"7".equals(ordertimeEntity.getTimeId())) {

                    initialDateList.get(dateId).setSelectedDate("2");
                    initialDateList.get(dateId).setIsSelctNight(false);

                } else if (CommonUtils.isEmpty(selectedDate) && "7".equals(ordertimeEntity.getTimeId())) {

                    initialDateList.get(dateId).setSelectedDate("12");
                    initialDateList.get(dateId).setIsSelctNight(true);

                } else {
                    initialDateList.get(dateId).setSelectedDate("2");
                    initialDateList.get(dateId).setIsSelctNight(false);
                }

                //calculateTotalPrice();
                calculateNewTotalPrice();

                myDateAdapter.notifyDataSetChanged();
            }
        });
    }

    public int daysBetween(String smdate) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date today = sdf.parse(sdf.format(new Date()));

        Date sdate = sdf.parse(smdate);

        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        long time1 = cal.getTimeInMillis();
        cal.setTime(sdate);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between_days));
    }


    /**
     * 计算订房订单价格
     */
    private void calculateTotalPrice() {

        int housePrice = 0;

        int len = initialDateList.size();

        for (int i = 0; i < len; i++) {
            OrderDateEntity dateEntity = initialDateList.get(i);
            String selectedTimes = dateEntity.getSelectedDate();
            boolean isSelectNight = dateEntity.isSelctNight();
            if (!CommonUtils.isEmpty(selectedTimes)) {
                if (!isSelectNight) {
                    if (selectedTimes.equals("2")) {
                        housePrice = housePrice + roomTimePriceList.get(0).getSalePrice();
                    } else if (selectedTimes.equals("4")) {
                        housePrice = housePrice + roomTimePriceList.get(1).getSalePrice();
                    } else if (selectedTimes.equals("6")) {
                        housePrice = housePrice + roomTimePriceList.get(2).getSalePrice();
                    } else if (selectedTimes.equals("8")) {
                        housePrice = housePrice + roomTimePriceList.get(3).getSalePrice();
                    } else if (selectedTimes.equals("10")) {
                        housePrice = housePrice + roomTimePriceList.get(4).getSalePrice();
                    } else if (selectedTimes.equals("12")) {
                        housePrice = housePrice + roomTimePriceList.get(5).getSalePrice();
                    } else if (selectedTimes.equals("14")) {
                        housePrice = housePrice + roomTimePriceList.get(0).getSalePrice() + roomTimePriceList.get(6).getSalePrice();
                    } else if (selectedTimes.equals("16")) {
                        housePrice = housePrice + roomTimePriceList.get(1).getSalePrice() + roomTimePriceList.get(6).getSalePrice();
                    } else if (selectedTimes.equals("18")) {
                        housePrice = housePrice + roomTimePriceList.get(2).getSalePrice() + roomTimePriceList.get(6).getSalePrice();
                    } else if (selectedTimes.equals("20")) {
                        housePrice = housePrice + roomTimePriceList.get(3).getSalePrice() + roomTimePriceList.get(6).getSalePrice();
                    } else if (selectedTimes.equals("22")) {
                        housePrice = housePrice + roomTimePriceList.get(4).getSalePrice() + roomTimePriceList.get(6).getSalePrice();
                    } else if (selectedTimes.equals("24")) {
                        housePrice = housePrice + roomTimePriceList.get(5).getSalePrice() + roomTimePriceList.get(6).getSalePrice();
                    }
                } else {
                    if (selectedTimes.equals("14")) {
                        housePrice = housePrice + roomTimePriceList.get(0).getSalePrice() + roomTimePriceList.get(6).getSalePrice();
                    } else if (selectedTimes.equals("16")) {
                        housePrice = housePrice + roomTimePriceList.get(1).getSalePrice() + roomTimePriceList.get(6).getSalePrice();
                    } else if (selectedTimes.equals("18")) {
                        housePrice = housePrice + roomTimePriceList.get(2).getSalePrice() + roomTimePriceList.get(6).getSalePrice();
                    } else if (selectedTimes.equals("20")) {
                        housePrice = housePrice + roomTimePriceList.get(3).getSalePrice() + roomTimePriceList.get(6).getSalePrice();
                    } else if (selectedTimes.equals("22")) {
                        housePrice = housePrice + roomTimePriceList.get(4).getSalePrice() + roomTimePriceList.get(6).getSalePrice();
                    } else if (selectedTimes.equals("24")) {
                        housePrice = housePrice + roomTimePriceList.get(5).getSalePrice() + roomTimePriceList.get(6).getSalePrice();
                    } else {
                        housePrice = housePrice + roomTimePriceList.get(6).getSalePrice();
                    }

                }
            }
        }
        totalPrice = housePrice;
        tvHousePrice.setText(housePrice + "元");
    }


    /**
     * A是价格，X是定的时段,计算最后的总价Psum
     * int[] X = { 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0 };//3天，共3*7+2=23个。实际上可以订7天，对应7*7+2=51个
     * int[] A = { 0, 10, 15, 19, 23, 26, 28, 50 };//价格，2小时、4小时、6小时...价格分别为10，15，19...夜间价格50，左侧补一个0表示0小时价格
     */
    private void calculateNewTotalPrice() {

        int timesLen = allTimeItems.size();
        int[] X = new int[51];
        X[0] = 0;
        X[50] = 0;
        for (int i = 0; i < timesLen; i++) {

            ArrayList<OrderTimeEntity> orderTimeList = allTimeItems.get(i);

            for (int j = 0; j < orderTimeList.size(); j++) {

                OrderTimeEntity orderTimeEntity = orderTimeList.get(j);

                if (orderTimeEntity.isSelect()) {
                    X[i * 7 + j + 1] = 1;
                } else {
                    X[i * 7 + j + 1] = 0;
                }
            }
        }

        int[] S = new int[X.length - 1];
        int[] E = new int[X.length - 1];
        List<Integer> TS = new ArrayList<Integer>();
        List<Integer> TE = new ArrayList<Integer>();
        for (int i = 0; i < S.length; i++) {
            if (X[i] == 0 && X[i + 1] == 1) {
                S[i] = 1;
                TS.add(i);
            }
        }
        for (int i = 0; i < E.length; i++) {
            if (X[i] == 1 && X[i + 1] == 0) {
                E[i] = 1;
                TE.add(i);
            }
        }
        int[] TA = new int[TS.size()];
        int[] TB = new int[TS.size()];
        for (int i = 0; i < TS.size(); i++) {
            TB[i] = (TE.get(i) - TS.get(i)) / 7;
            if (TE.get(i) % 7 < TS.get(i) % 7)
                TB[i]++;
            TA[i] = TE.get(i) - TS.get(i) - TB[i];
        }

        int[] P = new int[TS.size()];
        for (int i = 0; i < TS.size(); i++)
            // P[i] = A[TA[i] % 7] + (A[1] + A[2] + A[3] + A[4] + A[5] + A[6]) * (TA[i] / 7) + A[7] * TB[i];
            P[i] = A[TA[i] % 6] + A[6] * (TA[i] / 6) + A[7] * TB[i];
        int Psum = 0;

        for (int i = 0; i < P.length; i++)
            Psum += P[i];

        totalPrice = Psum;
        tvHousePrice.setText(Psum + "元");

    }

    /**
     * 获取当前下单日期
     *
     * @param index
     * @return
     */
    public Map<String, String> getNowOrNextDay(int index) {

        Map<String, String> map = new HashMap<String, String>();

        String temp_str = "";
        String LongDateStr = "";
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, index);
        date = calendar.getTime();

        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
        temp_str = sdf.format(date);

        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        LongDateStr = sdf1.format(date);

        map.put("showDate", temp_str);
        map.put("fullDate", LongDateStr);

        return map;
    }

    /**
     * 初始化详情信息
     */
    private void initDatas() {

        waitDialog.show();

        SharedPreferences settings = getSharedPreferences("secrecy", Activity.MODE_PRIVATE);
        String latitude = settings.getString("latitude", "");
        String longitude = settings.getString("longitude", "");

        String url = ServerApiConstants.Urls.GET_HOUSE_RESOURCE_DETAIL_INFO_URLS;

        OkHttpUtils
                .get()
                .url(url)
                .addParams("roomId", String.valueOf(roomId))
                .addParams("longitude", longitude)
                .addParams("latitude", latitude)
                .tag(this)
                .build()
                .execute(new HouseResourceDetailCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        waitDialog.hide();
                    }

                    @Override
                    public void onResponse(HouseResourceDeatailResult response) {

                        if ("1".equals(response.getStatuscode())) {
                            roomImgList = (ArrayList<HouseResourceDeatailResult.DataEntity.RoomImgListEntity>) response.getData().getRoomImgList();
                            //initRoomImgBanner(roomImgList);
                            loadingImgData();
                            rnRoomInfoMapEntity = response.getData().getRnRoomInfoMap();
                            tvHouseTitle.setText(rnRoomInfoMapEntity.getHouseTitle());
                            tvHouseAddr.setText(rnRoomInfoMapEntity.getHouseAdress());
                            tvRoomNo.setText(rnRoomInfoMapEntity.getDoorNo());
                            if ("ROOM".equals(rnRoomInfoMapEntity.getLockType())) {
                                roomType.setText("房间");
                            }else{
                                roomType.setText("客厅");
                            }

                            getSupportActionBar().setTitle(rnRoomInfoMapEntity.getHouseTitle());
                            getSupportActionBar().setSubtitle(trans(rnRoomInfoMapEntity.getHouseDistance()));//(rnRoomInfoMapEntity.getHouseDistance() + "m");

                            roomTimePriceList = (ArrayList<HouseResourceDeatailResult.DataEntity.RoomTimePriceListEntity>) response.getData().getRoomTimePriceList();

                            NoBusinessDateList = (ArrayList<HouseResourceDeatailResult.DataEntity.NoBusinessDateSgementListEntity>) response.getData().getNoBusinessDateSgementList();

                            int dateLen = NoBusinessDateList.size();

                            List<String> noDateList = new ArrayList<String>();

                            for (int a = 0; a < dateLen; a++) {
                                HouseResourceDeatailResult.DataEntity.NoBusinessDateSgementListEntity noBusiDate = NoBusinessDateList.get(a);
                                try {
                                    int l = daysBetween(noBusiDate.getNoBusinessDate());
                                    if (l <= 6) {
                                        noDateList.add(noBusiDate.getNoBusinessDate());
                                    }

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }

                            getDate(noDateList);

                            getTimePriceDate();

                            sellOutTimeList = (ArrayList<HouseResourceDeatailResult.DataEntity.HaveBusinessDateSgementListEntity>) response.getData().getHaveBusinessDateSgementList();

                            for (int i = 0; i < sellOutTimeList.size(); i++) {

                                HouseResourceDeatailResult.DataEntity.HaveBusinessDateSgementListEntity sellOutEntity = sellOutTimeList.get(i);

                                try {
                                    int a = daysBetween(sellOutEntity.getCheckInDate());
                                    int sellTimeId = sellOutEntity.getTimeId();

                                    int beforeSellDateLen = compareNowBetweenSellDate(sellOutEntity.getCheckInDate());


                                    int len = allTimeItems.get(a - beforeSellDateLen).size();
                                    for (int j = 0; j < len; j++) {
                                        OrderTimeEntity timeEntity = allTimeItems.get(a - beforeSellDateLen).get(j);
                                        if (Integer.valueOf(timeEntity.getTimeId()) == sellTimeId) {
                                            timeEntity.setIsSellOut(true);
                                        }
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                            }
                            myTimeAdapter.notifyDataSetChanged();


                            int len = roomTimePriceList.size();
                            A = new int[len + 1];
                            for (int i = 0; i < A.length; i++) {
                                if (i == 0) {
                                    A[i] = 0;
                                } else {
                                    A[i] = roomTimePriceList.get(i - 1).getSalePrice();
                                }
                            }
                        } else {
                            UIHelper.showToast(mContext, response.getMessage());
                        }
                        waitDialog.hide();
                    }
                });
    }

    private int compareNowBetweenSellDate(String date) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date today = sdf.parse(sdf.format(new Date()));

        Date sellDate = sdf.parse(date);

        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        long time1 = cal.getTimeInMillis();
        cal.setTime(sellDate);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);
        int between = Integer.parseInt(String.valueOf(between_days));
        List<String> mDateList = new ArrayList<String>();
        if (between > 0) {
            for (int i = 0; i < between; i++) {
                mDateList.add(getNowOrNextDay(i).get("fullDate"));
            }

            int beforeSellDateLen = 0;
            for (int j = 0; j < NoBusinessDateList.size(); j++) {
                String noBusiDate = NoBusinessDateList.get(j).getNoBusinessDate();
                if (mDateList.contains(noBusiDate)) {
                    beforeSellDateLen++;
                }
            }
            return beforeSellDateLen;

        } else {
            return 0;
        }

    }


    private void loadingImgData() {

        final List<View> list = new ArrayList<View>();
        inflater = LayoutInflater.from(this);
        /**
         * 创建多个item （每一条viewPager都是一个item） 从服务器获取完数据（图片url地址） 后，再设置适配器
         */
        for (int i = 0; i < roomImgList.size(); i++) {
            item = inflater.inflate(R.layout.item_product_viewpager, null);
            list.add(item);
        }
        // 创建适配器， 把组装完的组件传递进去
        adapter = new MyAdapter(list);
        TLog.error(viewPager.toString());
        viewPager.setAdapter(adapter);
        indicatorTV.setVisibility(View.VISIBLE);
        indicatorTV.setText("1" + "/" + list.size());
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // TODO Auto-generated method stub
                indicatorTV.setText(position + 1 + "/" + list.size());
                TLog.error("" + position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }
        });


    }


    /**
     * 适配器，负责装配 、销毁 数据 和 组件 。
     */
    private class MyAdapter extends PagerAdapter {
        private List<View> mList;

        public MyAdapter(List<View> list) {
            mList = list;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mList.size();
        }

        /**
         * Remove a page for the given position. 滑动过后就销毁 ，销毁当前页的前一个的前一个的页！
         * instantiateItem(View container, int position) This method was
         * deprecated in API level . Use instantiateItem(ViewGroup, int)
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // TODO Auto-generated method stub
            container.removeView(mList.get(position));
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            // TODO Auto-generated method stub
            return arg0 == arg1;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            // TODO Auto-generated method stub

            View view = mList.get(position);
            image = ((ImageView) view.findViewById(R.id.image));
            ImageLoader.getInstance().displayImage(roomImgList.get(position).getImgPath(), image);
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            container.removeView(mList.get(position));
            container.addView(mList.get(position));
            return mList.get(position);
        }

    }


    private void submitOrder() {

        mDialog.show();

        SharedPreferences settings = getSharedPreferences("secrecy", Activity.MODE_PRIVATE);
        mCustomerId = settings.getString("customerId", null);

        String url = ServerApiConstants.Urls.ADD_CUST_ORDER_URLS;

        JSONObject custOrderInfoJson = new JSONObject();

        JSONArray businessDateSegmentJsonArr = new JSONArray();

        try {
            custOrderInfoJson.put("houseTitle", rnRoomInfoMapEntity.getHouseTitle());
            custOrderInfoJson.put("customerId", mCustomerId);
            custOrderInfoJson.put("doorNo", rnRoomInfoMapEntity.getDoorNo());
            custOrderInfoJson.put("roomId", rnRoomInfoMapEntity.getRoomId());
            custOrderInfoJson.put("houseAdress", rnRoomInfoMapEntity.getHouseAdress());
            custOrderInfoJson.put("totalPrice", totalPrice);

            int len = allTimeItems.size();

            for (int j = 0; j < len; j++) {

                ArrayList<OrderTimeEntity> timeItemList = allTimeItems.get(j);

                int timeItemLen = timeItemList.size();

                for (int m = 0; m < timeItemLen; m++) {
                    OrderTimeEntity orderTimeEntity = timeItemList.get(m);
                    if (orderTimeEntity.isSelect()) {
                        JSONObject json = new JSONObject();
                        json.put("checkInDate", orderTimeEntity.getDate());
                        json.put("timeId", orderTimeEntity.getTimeId());
                        businessDateSegmentJsonArr.put(json);
                    }
                }
            }

            if (businessDateSegmentJsonArr.length() == 0) {
                UIHelper.showToast(mContext, "请至少选择一个入住时间下单");
                return;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Map<String, String> params = new HashMap<String, String>();
        params.put("custOrderInfoJson", custOrderInfoJson.toString());
        params.put("businessDateSegmentJson", businessDateSegmentJsonArr.toString());

        OkHttpUtils.
                post()
                .url(url)
                .params(params)
                .build()
                .execute(new OrderCallback());
    }

    public class OrderCallback extends StringCallback {
        @Override
        public void onBefore(Request request) {
            super.onBefore(request);
        }

        @Override
        public void onAfter() {
            super.onAfter();
        }

        @Override
        public void onError(Request request, Exception e) {
            UIHelper.showToast(mContext, "提交信息出错");
            mDialog.hide();
        }

        @Override
        public void onResponse(String response) {

            mDialog.hide();

            try {
                JSONObject jsonObject = new JSONObject(response);

                String statusCode = jsonObject.optString("statuscode");
                String message = jsonObject.optString("message");
                JSONObject data = jsonObject.getJSONObject("data");
                if (statusCode.equals("1")) {
                    UIHelper.showToast(mContext, "房源已预定，请在15分钟内完成支付");//message);
                    Bundle bundle = new Bundle();
                    bundle.putString("custOrderId", data.optString("custOrderId"));
                    bundle.putString("title", "支付");
                    readyGo(PayActivity.class, bundle);
                    finish();
                } else {
                    UIHelper.showToast(mContext, message);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void inProgress(float progress) {
            Log.e(TAG_LOG, "inProgress:" + progress);
        }
    }


    /**
     * 经纬度距离单位转换
     */
    private String trans(double distance) {
        double dis = 0.0;
        if (distance >= 1000) {
            dis = Math.round(distance / 100d) / 10d;
        } else {
            dis = Math.round(distance / 100d) * 100d;
            return String.valueOf((int) dis) + "m";
        }
        return Double.toString(dis) + "km";
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
    @OnClick({R.id.btn_do_order})
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_do_order:

                if (!UIHelper.isLogin(mContext)) {
                    UIHelper.showLogin(mContext);
                } else {
                    submitOrder();
                }
                break;
            default:
                break;
        }
    }


    public static class MyDateAdapter extends RecyclerView.Adapter<MyDateAdapter.ViewHolder> {
        // private  String[]  datas;
        List<OrderDateEntity> list;
        private int selectedItem = 0;

        public MyDateAdapter(List<OrderDateEntity> list) {
            super();
            this.list = list;
        }

        private OnRecyclerViewItemClickListener mOnItemClickListener = null;

        //define interface
        public interface OnRecyclerViewItemClickListener {
            void onItemClick(View view, Object data);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_order_date_recyclerview, viewGroup, false);
            ViewHolder vh = new ViewHolder(view);
            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {

            OrderDateEntity orderDateEntity = list.get(position);
            viewHolder.tvDate.setText(list.get(position).getShowDate());
            //将数据保存在itemView的Tag中，以便点击时进行获取
            viewHolder.itemView.setTag(list.get(position));
            viewHolder.itemView.setSelected(selectedItem == position);
            // viewHolder.tvSelectedTime.setText(list.get(position).getSelectedDate() == null || list.get(position).getSelectedDate() == "" || list.get(position).getSelectedDate() == "0" ? "" : "已选" + list.get(position).getSelectedDate() + "小时");
            if (CommonUtils.isEmpty(orderDateEntity.getSelectedDate()) || "0".equals(orderDateEntity.getSelectedDate())) {
                viewHolder.tvSelectedTime.setText("");
            } else {
                viewHolder.tvSelectedTime.setText("已选" + orderDateEntity.getSelectedDate() + "小时");
            }

            if (selectedItem == position) {
                viewHolder.rlDateItem.setBackgroundResource(R.color.main_toolbar_color);
            } else {
                viewHolder.rlDateItem.setBackgroundResource(R.color.light_gray);
            }
        }

        public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
            this.mOnItemClickListener = listener;
        }

        //获取数据的数量
        @Override
        public int getItemCount() {
            return list.size();
        }

        //自定义的ViewHolder，持有每个Item的的所有界面元素
        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView tvDate;
            public TextView tvSelectedTime;
            public RelativeLayout rlDateItem;

            public ViewHolder(View view) {
                super(view);
                tvDate = (TextView) view.findViewById(R.id.tv_date);
                tvSelectedTime = (TextView) view.findViewById(R.id.tv_selected_time);
                rlDateItem = (RelativeLayout) view.findViewById(R.id.item_date_list_layout);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Redraw the old selection and the new
                        notifyItemChanged(selectedItem);
                        selectedItem = getPosition();
                        OrderDateEntity dateEntity = (OrderDateEntity) v.getTag();

                        dateEntity.setIsSelect(true);

                        mOnItemClickListener.onItemClick(v, dateEntity);
                        notifyItemChanged(selectedItem);
                    }
                });
            }

        }
    }

//

    public static class MyTimeAdapter extends RecyclerView.Adapter<MyTimeAdapter.ViewHolder> {

        List<OrderTimeEntity> list;

        public MyTimeAdapter(List<OrderTimeEntity> list) {
            super();
            this.list = list;
        }

        private OnRecyclerViewItemClickListener mOnItemClickListener = null;

        //define interface
        public interface OnRecyclerViewItemClickListener {
            void onItemClick(View view, Object data);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_order_time_recyclerview, viewGroup, false);
            ViewHolder vh = new ViewHolder(view);
            return vh;
        }

        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

            final OrderTimeEntity item = (OrderTimeEntity) list.get(position);

            viewHolder.tvDate.setText(item.getTime());
            //将数据保存在itemView的Tag中，以便点击时进行获取
            viewHolder.itemView.setTag(item);
            if (item.isSelect()) {
                viewHolder.selectedTime.setChecked(true);
            } else {
                viewHolder.selectedTime.setChecked(false);
            }

            if (!item.isSellOut()) {

                int timeId = Integer.valueOf(item.getTimeId());
                String[] arrays = {"10:00:00", "12:00:00", "14:00:00", "16:00:00", "18:00:00", "20:00:00", "24:00:00"};
                SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
                String itemTimeStr = item.getDate() + " " + arrays[timeId - 1];
                try {
                    Date nowTime = sdf.parse(sdf.format(new Date()));
                    Date itemTime = sdf.parse(itemTimeStr);
                    itemTime.getTime();
                    if (nowTime.getTime() >= itemTime.getTime()) {
                        item.setIsSellOut(true);
                    } else {
                        item.setIsSellOut(false);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            if (item.isSellOut()) {
                viewHolder.rlTimeItem.setBackgroundColor(Color.parseColor("#1a000000"));
                viewHolder.selectedTime.setEnabled(false);
            } else {
                viewHolder.rlTimeItem.setBackgroundColor(Color.WHITE);
                viewHolder.selectedTime.setEnabled(true);
            }

            viewHolder.selectedTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!item.isSellOut()) {
                        if (item.isSelect()) {
                            viewHolder.selectedTime.setChecked(false);
                            item.setIsSelect(false);
                        } else {
                            viewHolder.selectedTime.setChecked(true);
                            item.setIsSelect(true);
                        }
                        mOnItemClickListener.onItemClick(v, item);
                    }
                }
            });

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!item.isSellOut()) {
                        if (item.isSelect()) {
                            viewHolder.selectedTime.setChecked(false);
                            item.setIsSelect(false);
                        } else {
                            viewHolder.selectedTime.setChecked(true);
                            item.setIsSelect(true);
                        }
                        mOnItemClickListener.onItemClick(v, item);
                    }
                }
            });

        }

        public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
            this.mOnItemClickListener = listener;
        }


        //获取数据的数量
        @Override
        public int getItemCount() {
            return list.size();
        }

        //自定义的ViewHolder，持有每个Item的的所有界面元素
        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView tvDate;
            public CheckBox selectedTime;
            public LinearLayout rlTimeItem;

            public ViewHolder(View view) {
                super(view);
                tvDate = (TextView) view.findViewById(R.id.tv_time);
                rlTimeItem = (LinearLayout) view.findViewById(R.id.item_time_list_layout);
                selectedTime = (CheckBox) view.findViewById(R.id.ckeckbox_time);
            }
        }
    }


//    public class DateRecyclerAdapter extends AutoRVAdapter {
//
//
//        private Context context;
//
//        /**
//         * @param context
//         * @param list
//         */
//        public DateRecyclerAdapter(Context context, List<OrderDateEntity> list) {
//            super(context, list);
//            this.context = context;
//            this.list = initialDateList;
//        }
//
//        @Override
//        public int onCreateViewLayoutID(int viewType) {
//            return R.layout.item_order_date_recyclerview;
//        }
//
//        @Override
//        public void onBindViewHolder(final ViewHolder holder, final int position) {
//
//            final OrderDateEntity item = (OrderDateEntity) initialDateList.get(position);
//            holder.getTextView(R.id.tv_date).setText(item.getDate());
//            holder.getTextView(R.id.tv_selected_time).setText("");
//
//            if (position == 0) {
//                holder.getConvertView().findViewById(R.id.item_date_list_layout).setBackgroundColor(getResources().getColor(R.color.main_toolbar_color));
//            }
//
//            holder.getConvertView().findViewById(R.id.item_date_list_layout).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                }
//            });
//        }
//
//        public Object getItems() {
//            return list;
//        }
//    }
//
//    public static class TimeRecyclerAdapter extends AutoRVAdapter {
//
//
//        private Context context;
//
//        /**
//         * @param context
//         * @param list
//         */
//        public TimeRecyclerAdapter(Context context, List<OrderTimeEntity> list) {
//            super(context, list);
//            this.context = context;
//            this.list = list;
//        }
//
//        private OnRecyclerViewItemClickListener mOnItemClickListener = null;
//
//        //define interface
//        public interface OnRecyclerViewItemClickListener {
//            void onItemClick(View view , Object data);
//        }
//
//        public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
//            this.mOnItemClickListener = listener;
//        }
//
//        @Override
//        public int onCreateViewLayoutID(int viewType) {
//            return R.layout.item_order_time_recyclerview;
//        }
//
//        @Override
//        public void onBindViewHolder(final ViewHolder holder, final int position) {
//
//            final OrderTimeEntity item = (OrderTimeEntity) list.get(position);
//            holder.getTextView(R.id.tv_time).setText(item.getTime());
//
//
//            if (item.isSelect()) {
//                holder.getCheckBox(R.id.ckeckbox_time).setChecked(true);
//            }else{
//                holder.getCheckBox(R.id.ckeckbox_time).setChecked(false);
//            }
//
//            holder.getCheckBox(R.id.ckeckbox_time).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (item.isSelect()) {
//                        holder.getCheckBox(R.id.ckeckbox_time).setChecked(false);
//                        item.setIsSelect(false);
//                    }else{
//                        holder.getCheckBox(R.id.ckeckbox_time).setChecked(true);
//                        item.setIsSelect(true);
//                    }
//                    mOnItemClickListener.onItemClick(v,item);
//                }
//            });
//
//            holder.getConvertView().findViewById(R.id.item_time_list_layout).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (item.isSelect()) {
//                        holder.getCheckBox(R.id.ckeckbox_time).setChecked(false);
//                        item.setIsSelect(false);
//                    }else{
//                        holder.getCheckBox(R.id.ckeckbox_time).setChecked(true);
//                        item.setIsSelect(true);
//                    }
//                    mOnItemClickListener.onItemClick(v,item);
//                }
//            });
//        }
//
//        public Object getItems() {
//            return list;
//        }
//    }
}
