package com.nick.smarthome.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.okhttp.OkHttpUtils;
import com.github.obsessive.library.eventbus.EventCenter;
import com.github.obsessive.library.netstatus.NetUtils;
import com.github.obsessive.library.utils.CommonUtils;
import com.google.gson.Gson;
import com.nick.smarthome.R;
import com.nick.smarthome.api.ServerApiConstants;
import com.nick.smarthome.bean.CommResult;
import com.nick.smarthome.bean.ImagePathResult;
import com.nick.smarthome.bean.LockDetailInfoResult;
import com.nick.smarthome.callback.CommonCallback;
import com.nick.smarthome.callback.LockDetailInfoCallback;
import com.nick.smarthome.callback.UploadLockImgCallback;
import com.nick.smarthome.common.Constants;
import com.nick.smarthome.ui.adapter.BusinessTimePriceRecyclerAdapter;
import com.nick.smarthome.ui.adapter.LockTypeFragmentAdapter;
import com.nick.smarthome.ui.adapter.NoBusinessDateRecyclerAdapter;
import com.nick.smarthome.ui.adapter.PhotoAdapter;
import com.nick.smarthome.ui.base.BaseSwipeBackActivity;
import com.nick.smarthome.utils.DialogHelp;
import com.nick.smarthome.utils.UIHelper;
import com.nick.smarthome.widgets.MyLinearLayoutManager;
import com.nick.smarthome.widgets.wheelpicker.core.AbstractWheelPicker;
import com.nick.smarthome.widgets.wheelpicker.widget.curved.WheelDatePicker;
import com.nick.smarthome.widgets.wheelpicker.widget.curved.WheelMonthPicker;
import com.squareup.okhttp.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
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
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * Author:  nick
 * Email:   nickdevp@gmail.com
 * Date:    15/12/23 22:32.
 * Description: 锁的信息
 */
public class LockInfoActivity extends BaseSwipeBackActivity implements View.OnClickListener {

    private static final String TAG = "LockInfoActivity";

    @InjectView(R.id.ll_add_lock_pic)
    LinearLayout llAddLockPic;

    @InjectView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @InjectView(R.id.main_wheel_room)
    WheelMonthPicker wheelRoomPicker;

    @InjectView(R.id.set_no_business_date)
    WheelDatePicker wheelNoBusinessPicker;

    @InjectView(R.id.room_no)
    TextView tvRoomNo;

    @InjectView(R.id.rl_set_Business_date)
    RelativeLayout rlSetBusinessDate;

    @InjectView(R.id.rl_set_time_price)
    RelativeLayout rlSetTimePrice;

    @InjectView(R.id.layout_set_business_date)
    LinearLayout laySetBusinessDate;

    @InjectView(R.id.add_no_busi_date_btn)
    TextView addNoBusiDateBtn;

    @InjectView(R.id.btn_delete)
    TextView deleteBtn;

    @InjectView(R.id.btn_save)
    TextView saveBtn;

    @InjectView(R.id.add_time_price_btn)
    ImageView addTimePriceBtn;

    @InjectView(R.id.date_recyclerview)
    RecyclerView dateRecyclerview;

    @InjectView(R.id.time_recyclerview)
    RecyclerView timeRecyclerview;

    @InjectView(R.id.layout_set_time_price)
    LinearLayout layoutSetTimePrice;

    @InjectView(R.id.status_radioGroup)
    RadioGroup mRadioGroup;

    @InjectView(R.id.private_tag)
    RadioButton privateTag;

    @InjectView(R.id.public_tag)
    RadioButton publicTag;

    @InjectView(R.id.lock_type_radioGroup)
    RadioGroup lockTypeRadioGroup;

    @InjectView(R.id.house_tag)
    RadioButton houseTag;

    @InjectView(R.id.room_tag)
    RadioButton roomTag;

    @InjectView(R.id.img_date_arrow)
    ImageView imgDateArrow;

    @InjectView(R.id.img_time_arrow)
    ImageView imgTimeArrow;

    PhotoAdapter photoAdapter;

    NoBusinessDateRecyclerAdapter noBusinessDateRecyclerAdapter;

    BusinessTimePriceRecyclerAdapter businessTimePriceRecyclerAdapter;

    MyTimesAdapter myTimesAdapter;

    ProgressDialog waitDialog;

    ProgressDialog initDataDialog;

    private Dialog writePriceDialog;

    private RecyclerView.LayoutManager mLayoutManager;

    private RecyclerView.LayoutManager timeLayoutManager;

    private MyLinearLayoutManager myDateLinearLayoutManager;

    private MyLinearLayoutManager myTimeLinearLayoutManager;

    private LockTypeFragmentAdapter mAdapter;

    ArrayList<String> selectedPhotos = new ArrayList<>();

    ArrayList<Map<String, Object>> noBusiDateList = new ArrayList<Map<String, Object>>();

    ArrayList<Map<String, Object>> timePriceList = new ArrayList<Map<String, Object>>();

    public final static int REQUEST_CODE = 1;

    private static final int REQUEST_IMAGE = 2;
    private ArrayList<String> mSelectPath;

    private int padding;
    private int textSize;
    private int itemSpace;
    private String roomNo, noBusiDateStr;
    private int firstNoDate = 1;
    private int firstTime = 1;
    private int isPublic = 0;
    private String lockType = "HOUSE";
    private String rnLockId, isBound;

    private String privilege = "0";
    private String startPrice = "";
    private String stepPrice = "";
    private String mCustomerId, lockCode;
    private List<String> imagePathList;
    private boolean isModifyImg = false;
    private JSONObject lockJson = new JSONObject();
    private JSONArray imgJsonArr = new JSONArray();

    @Override
    protected int getActionBarTitle() {
        return R.string.lock_info;
    }

    @Override
    protected boolean isApplyKitKatTranslucency() {
        return true;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

        SharedPreferences settings = getSharedPreferences("secrecy", Activity.MODE_PRIVATE);
        mCustomerId = settings.getString("customerId", null);

        lockCode = extras.getString("lockCode");
        rnLockId = extras.getString("lockId");
        isBound = extras.getString("isBound");

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_lock_info;
    }

    @Override
    protected void onEventComming(EventCenter eventCenter) {

        UIHelper.showToast(LockInfoActivity.this, eventCenter.getData().toString());
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void initViewsAndEvents() {

        if ("0".equals(isBound)) {
            deleteBtn.setVisibility(View.VISIBLE);
        } else {
            deleteBtn.setVisibility(View.GONE);
            // saveBtn.setVisibility(View.GONE);
            lockTypeRadioGroup.setEnabled(false);
            houseTag.setEnabled(false);
            roomTag.setEnabled(false);
            wheelRoomPicker.setVisibility(View.GONE);
            tvRoomNo.setVisibility(View.VISIBLE);
        }

        initDataDialog = DialogHelp.getWaitDialog(this, "正在加载数据...");
        initDataDialog.show();

        padding = getResources().getDimensionPixelSize(R.dimen.WheelPadding);
        textSize = getResources().getDimensionPixelSize(R.dimen.TextSizeLarge);
        itemSpace = getResources().getDimensionPixelSize(R.dimen.ItemSpaceLarge);

        WindowManager wm = this.getWindowManager();

        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();

        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) mRecyclerView.getLayoutParams(); // 取控件mGrid当前的布局参数
        linearParams.height = width / 3;// 当控件的高强制设成50象素
        mRecyclerView.setLayoutParams(linearParams); // 使设置好的布局参数应用到控件myGrid

        // mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        photoAdapter = new PhotoAdapter(this, selectedPhotos);

        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, OrientationHelper.VERTICAL));
        mRecyclerView.setAdapter(photoAdapter);

        wheelRoomPicker.setPadding(padding, padding, padding, padding);
        wheelRoomPicker.setTextSize(textSize);
        wheelRoomPicker.setItemSpace(itemSpace);
        wheelRoomPicker.setItemCount(11);
        wheelRoomPicker.setCurrentMonth(1);
        wheelRoomPicker.setOrientation(LinearLayout.HORIZONTAL);
        wheelRoomPicker.setOnWheelChangeListener(new AbstractWheelPicker.SimpleWheelChangeListener() {
            @Override
            public void onWheelScrollStateChanged(int state) {
            }

            @Override
            public void onWheelSelected(int index, String data) {
                roomNo = data;
            }
        });

        wheelNoBusinessPicker.setPadding(padding, 2, padding, 2);
        wheelNoBusinessPicker.setTextSize(60);
        wheelNoBusinessPicker.setItemSpace(itemSpace);
        wheelNoBusinessPicker.setTextColor(getResources().getColor(R.color.main_toolbar_color));
        wheelNoBusinessPicker.setCurrentTextColor(getResources().getColor(R.color.main_toolbar_color));
        wheelNoBusinessPicker.setLabelColor(getResources().getColor(R.color.main_toolbar_color));
        wheelNoBusinessPicker.setOnWheelChangeListener(new AbstractWheelPicker.SimpleWheelChangeListener() {
            @Override
            public void onWheelScrollStateChanged(int state) {
            }

            @Override
            public void onWheelSelected(int index, String data) {
                noBusiDateStr = data;
            }
        });

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.private_tag:
                        isPublic = 0;
                        break;
                    case R.id.public_tag:
                        isPublic = 1;
                        break;
                }
            }
        });

        lockTypeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.house_tag:
                        lockType = "HOUSE";
                        break;
                    case R.id.room_tag:
                        lockType = "ROOM";
                        break;
                }
            }
        });


        myDateLinearLayoutManager = new MyLinearLayoutManager(this);

        myTimeLinearLayoutManager = new MyLinearLayoutManager(this);

        mLayoutManager = new LinearLayoutManager(this);

        dateRecyclerview.setLayoutManager(myDateLinearLayoutManager);//mLayoutManager);
        noBusinessDateRecyclerAdapter = new NoBusinessDateRecyclerAdapter(this, noBusiDateList);

        dateRecyclerview.setAdapter(noBusinessDateRecyclerAdapter);

        timeLayoutManager = new LinearLayoutManager(this);

        getTimePriceDate();


//        timeRecyclerview.setLayoutManager(timeLayoutManager);
//        myTimesAdapter =  new MyTimesAdapter(timePriceList);
//        timeRecyclerview.setAdapter(myTimesAdapter);

        initDatas();

        dateRecyclerview.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
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
     * 初始化锁信息
     */
    private void initDatas() {

        String url = ServerApiConstants.Urls.GET_LOCK_DETAIL_INFO_URLS;

        OkHttpUtils
                .get()
                .url(url)
                .addParams("rnLockId", rnLockId)
                .tag(this)
                .build()
                .execute(new LockDetailInfoCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        UIHelper.showToast(mContext, "接口调用失败");
                        initDataDialog.hide();
                    }

                    @Override
                    public void onResponse(LockDetailInfoResult response) {

                        if ("1".equals(response.getStatuscode())) {

                            // imagePathList = response.getData().getLockFileJson();
                            try {
                                lockJson = new JSONObject(response.getData().getRnlockInfoJson());
                                String isHaveModify = lockJson.optString("isHaveModify");
                                if ("1".equals(isHaveModify)) {
                                    JSONArray timePriceArr = new JSONArray(response.getData().getLockTimeSegmentRelaJson());
                                    int len = timePriceArr.length();
                                    for (int i = 0; i < len; i++) {
                                        JSONObject json = timePriceArr.getJSONObject(i);
                                        Map<String, Object> map = timePriceList.get(i);
                                        map.put("salePrice", json.optString("salePrice"));
                                    }
                                    myTimesAdapter.notifyDataSetChanged();

                                    imgJsonArr = new JSONArray(response.getData().getLockFileJson());
                                    int imgLen = imgJsonArr.length();
                                    ArrayList<String> imgPathList = new ArrayList<String>();
                                    for (int j = 0; j < imgLen; j++) {
                                        JSONObject imgJson = imgJsonArr.getJSONObject(j);
                                        String imgPath = imgJson.optString("imgPath");
                                        imgPathList.add(imgPath);
                                    }
                                    selectedPhotos.clear();

                                    if (imgPathList != null) {
                                        selectedPhotos.addAll(imgPathList);
                                    }
                                    photoAdapter.notifyDataSetChanged();


                                    isPublic = lockJson.optInt("isPublic");
                                    lockType = lockJson.optString("lockType");
                                    roomNo = lockJson.optString("doorNo");
                                    stepPrice = lockJson.optString("everyTwoHourPrice");
                                    startPrice = lockJson.optString("startPrice");
                                    privilege = String.valueOf(Double.valueOf(lockJson.optString("discountRate")) / 100);

                                    wheelRoomPicker.setCurrentMonth(Integer.valueOf(roomNo));
                                    tvRoomNo.setText(roomNo);

                                    if (isPublic == 1) {
                                        publicTag.setChecked(true);
                                    } else {
                                        privateTag.setChecked(true);
                                    }

                                    if ("HOUSE".equals(lockType)) {
                                        houseTag.setChecked(true);
                                    } else {
                                        roomTag.setChecked(true);
                                    }
                                    //  calculatePrice();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        } else {
                            UIHelper.showToast(mContext, "初始化锁信息失败");
                        }

                        initDataDialog.hide();
                    }
                });


    }

    /**
     * 初始化营业时间段
     */
    private void getTimePriceDate() {


        String[] arrays = getResources().getStringArray(R.array.lockTimeBucket);
        int len = arrays.length;

        for (int i = 0; i < len; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("timeId", i + 1);
            map.put("date", arrays[i]);
            map.put("salePrice", "");
            timePriceList.add(map);
        }

        timeRecyclerview.setLayoutManager(myTimeLinearLayoutManager);//timeLayoutManager);
        myTimesAdapter = new MyTimesAdapter(timePriceList);
        timeRecyclerview.setAdapter(myTimesAdapter);
        myTimesAdapter.notifyDataSetChanged();
    }


    private void MyDialog(Context context) {
        writePriceDialog = new Dialog(context, R.style.MyDialog);
        writePriceDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mPopView = inflater.inflate(R.layout.dialog_write_time_price, null);
        TextView confirmBtn = (TextView) mPopView.findViewById(R.id.btn_confirm);
        final EditText startPriceTv = (EditText) mPopView.findViewById(R.id.edt_start_price);
        final EditText everyTwoHourPrice = (EditText) mPopView.findViewById(R.id.edt_every_twohour_price);
        final SeekBar couponValue = (SeekBar) mPopView.findViewById(R.id.seekbar);
        couponValue.incrementProgressBy(1);

        couponValue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //progress = progress / 10;
                // UIHelper.showToast(mContext, String.valueOf(progress));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPrice = startPriceTv.getText().toString();
                stepPrice = everyTwoHourPrice.getText().toString();
                privilege = String.valueOf((double) couponValue.getProgress() / 10);

                if (CommonUtils.isEmpty(startPrice)) {
                    UIHelper.showToast(mContext, "起始价格不能为空");
                    return;
                }

                if (CommonUtils.isEmpty(stepPrice)) {
                    UIHelper.showToast(mContext, "每2小时价格不能为空");
                    return;
                }

                writePriceDialog.hide();

                calculatePrice();
            }
        });

        writePriceDialog.setContentView(mPopView);
        Window window = writePriceDialog.getWindow();
        // 设置显示动画
        window.setWindowAnimations(R.style.main_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = ((Activity) context).getWindowManager().getDefaultDisplay().getHeight();
        // 设置显示位置
        writePriceDialog.onWindowAttributesChanged(wl);
        // 设置点击外围解散
        writePriceDialog.setCanceledOnTouchOutside(true);
        writePriceDialog.show();
    }

    private void selectPhoto() {
        int selectedMode = MultiImageSelectorActivity.MODE_MULTI;

        boolean showCamera = true;

        int maxNum = 3;

        Intent intent = new Intent(LockInfoActivity.this, MultiImageSelectorActivity.class);
        // 是否显示拍摄图片
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, showCamera);
        // 最大可选择图片数量
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, maxNum);
        // 选择模式
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, selectedMode);
        // 默认选择
        if (mSelectPath != null && mSelectPath.size() > 0) {
            intent.putExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, mSelectPath);
        }
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void saveLockInfo() {

        if (selectedPhotos == null || selectedPhotos.size() <= 0) {
            UIHelper.showToast(LockInfoActivity.this, "请至少选择一张图片上传");
            return;
        }

        if (isModifyImg) {
            uploadPic();
        } else {
            submitLockInfo();
        }

    }

    private void deleteLockInfo() {

        DialogHelp.getConfirmDialog(this, "确定删除锁?", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                List rnLockIdList = new ArrayList();
                rnLockIdList.add(rnLockId);

                Gson gson = new Gson();
                String rnLockIdListStr = gson.toJson(rnLockIdList);

                String url = ServerApiConstants.Urls.DELETE_LOCK_URLS;
                Map<String, String> params = new HashMap<String, String>();
                params.put("rnLockIdList", rnLockIdListStr);

                OkHttpUtils.
                        post()
                        .url(url)
                        .params(params)
                        .build()
                        .execute(new CommonCallback() {
                            @Override
                            public void onError(Request request, Exception e) {
                                UIHelper.showToast(mContext, "接口出错");
                            }

                            @Override
                            public void onResponse(CommResult response) {
                                if (response.statuscode.equals("1")) {
                                    UIHelper.showToast(mContext, response.message);
                                    finish();
                                    sendBroadcast(new Intent(Constants.INTENT_ACTION_LOCK_BOUGHT));
                                } else {
                                    UIHelper.showToast(mContext, response.message);
                                }
                            }
                        });
            }
        }).show();

    }

    /**
     * 添加不营业时间
     */
    private void addNoBusiDate() {

        try {
            int a = daysBetween(noBusiDateStr);
            if (a < 0) {
                UIHelper.showToast(mContext, "请选择正确的日期");
                return;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        for (int i = 0; i <noBusiDateList.size(); i++) {
            Map<String, Object> map = noBusiDateList.get(i);
            if(map.get("noBusinessDate").equals(noBusiDateStr)){
               UIHelper.showToast(mContext,"当前日期已经添加");
                return;
            }
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("noBusinessDate", noBusiDateStr);
        noBusiDateList.add(map);

        noBusinessDateRecyclerAdapter = new NoBusinessDateRecyclerAdapter(this, noBusiDateList);

        dateRecyclerview.setAdapter(noBusinessDateRecyclerAdapter);
        noBusinessDateRecyclerAdapter.notifyDataSetChanged();
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


    private void uploadPic() {


        waitDialog = DialogHelp.getWaitDialog(this, "正在上传图片...");

        String url = ServerApiConstants.Urls.UPLOAD_MULTI_PIC_URLS;

        if (mSelectPath == null || mSelectPath.size() <= 0) {
            UIHelper.showToast(LockInfoActivity.this, "请至少选择一张图片上传");
            return;
        }

        if (CommonUtils.isEmpty(startPrice) || CommonUtils.isEmpty(stepPrice)) {
            UIHelper.showToast(mContext, "请先设置时段价格");
            return;
        }

        waitDialog.show();

        if (mSelectPath.size() == 1) {

            OkHttpUtils.post()
                    .addFile("mFile", "messenger_01.png", new File(mSelectPath.get(0)))
                    .url(url)
                    .build()
                    .execute(new UploadLockImgCallback() {
                        @Override
                        public void onError(Request request, Exception e) {
                            UIHelper.showToast(LockInfoActivity.this, "上传图片出错");
                            waitDialog.hide();
                        }

                        @Override
                        public void onResponse(ImagePathResult response) {
                            waitDialog.hide();
                            if (response.getStatuscode().equals("1")) {
                                UIHelper.showToast(LockInfoActivity.this, response.getMessage());
                                imagePathList = response.getData().getImgPath();
                                submitLockInfo();
                            } else {
                                UIHelper.showToast(LockInfoActivity.this, response.getMessage());
                            }
                        }
                    });
        } else if (mSelectPath.size() == 2) {
            OkHttpUtils.post()
                    .addFile("mFile", "messenger_01.png", new File(mSelectPath.get(0)))
                    .addFile("mFile1", "messenger_02.png", new File(mSelectPath.get(1)))
                    .url(url)
                    .build()
                    .execute(new UploadLockImgCallback() {
                        @Override
                        public void onError(Request request, Exception e) {
                            UIHelper.showToast(LockInfoActivity.this, "上传图片出错");
                            waitDialog.hide();
                        }

                        @Override
                        public void onResponse(ImagePathResult response) {
                            waitDialog.hide();
                            if (response.getStatuscode().equals("1")) {
                                UIHelper.showToast(LockInfoActivity.this, response.getMessage());
                                imagePathList = response.getData().getImgPath();
                                submitLockInfo();
                            } else {
                                UIHelper.showToast(LockInfoActivity.this, response.getMessage());
                            }
                        }
                    });
        } else if (mSelectPath.size() == 3) {
            OkHttpUtils.post()
                    .addFile("mFile", "messenger_01.png", new File(mSelectPath.get(0)))
                    .addFile("mFile1", "messenger_02.png", new File(mSelectPath.get(1)))
                    .addFile("mFile2", "messenger_03.png", new File(mSelectPath.get(2)))
                    .url(url)
                    .build()
                    .execute(new UploadLockImgCallback() {
                        @Override
                        public void onError(Request request, Exception e) {
                            UIHelper.showToast(LockInfoActivity.this, "上传图片出错");
                            waitDialog.hide();
                        }

                        @Override
                        public void onResponse(ImagePathResult response) {
                            waitDialog.hide();
                            if (response.getStatuscode().equals("1")) {
                                UIHelper.showToast(LockInfoActivity.this, response.getMessage());
                                imagePathList = response.getData().getImgPath();
                                submitLockInfo();
                            } else {
                                UIHelper.showToast(LockInfoActivity.this, response.getMessage());
                            }
                        }
                    });
        }
    }


    private void submitLockInfo() {

        String url = ServerApiConstants.Urls.UPDATE_LOCK_INFO_URLS;

        JSONObject rnlockInfoJson = new JSONObject();
        JSONArray imagePathJson = new JSONArray();
        Gson gson = new Gson();
        String timePriceJsonStr = null;
        String noBusiDateJsonStr = null;
        try {
            rnlockInfoJson.put("rnLockId", rnLockId);
            rnlockInfoJson.put("customerId", mCustomerId);
            rnlockInfoJson.put("lockType", lockType);
            rnlockInfoJson.put("doorNo", roomNo);
            rnlockInfoJson.put("lockCode", lockCode);
            rnlockInfoJson.put("isPublic", isPublic);
            rnlockInfoJson.put("discountRate", Double.valueOf(privilege) * 100);
            rnlockInfoJson.put("everyTwoHourPrice", stepPrice);
            rnlockInfoJson.put("startPrice", startPrice);

            timePriceJsonStr = gson.toJson(timePriceList);
            noBusiDateJsonStr = gson.toJson(noBusiDateList);

            if (isModifyImg) {
                int imgLen = imagePathList.size();
                for (int i = 0; i < imgLen; i++) {
                    String imagePath = imagePathList.get(i);
                    JSONObject json = new JSONObject();
                    json.put("imgPath", imagePath);
                    imagePathJson.put(json);
                }
            } else {
                imagePathJson = imgJsonArr;
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        Map<String, String> params = new HashMap<String, String>();
        params.put("rnlockInfoJson", rnlockInfoJson.toString());
        params.put("lockTimeSegmentRelaJson", timePriceJsonStr);
        params.put("noBusinessDateJson", noBusiDateJsonStr);
        params.put("lockFileJson", imagePathJson.toString());

        // return;

        OkHttpUtils.
                post()
                .url(url)
                .params(params)
                .build()
                .execute(new CommonCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        UIHelper.showToast(mContext, "提交信息出错");
                    }

                    @Override
                    public void onResponse(CommResult response) {
                        if (response.statuscode.equals("1")) {
                            UIHelper.showToast(mContext, response.message);
                            sendBroadcast(new Intent(Constants.INTENT_ACTION_LOCK_BOUGHT));
                            finish();
                        } else {
                            UIHelper.showToast(mContext, response.message);
                        }

                    }
                });

    }


    private void calculatePrice() {

        int len = timePriceList.size();

        for (int i = 0; i < len; i++) {
            Map<String, Object> map = timePriceList.get(i);
            map.put("salePrice", calculateStepPrice(i + 1));
        }

        myTimesAdapter.notifyDataSetChanged();
    }


    /**
     * @param timeStep
     * @return
     */
    private String calculateStepPrice(int timeStep) {

        String doorPrice = "";

        if (privilege == "0.0") {

            doorPrice = String.valueOf(Integer.valueOf(startPrice) + Integer.valueOf(stepPrice) * (timeStep - 1));

        } else {

            doorPrice = String.valueOf(Math.rint(Integer.valueOf(startPrice) +
                    Integer.valueOf(stepPrice) / Double.valueOf(privilege) * Math.log(1 + Double.valueOf(privilege) * (timeStep - 1))));
        }
        if (doorPrice.indexOf(".") > 0) {
            //正则表达
            doorPrice = doorPrice.replaceAll("0+?$", "");//去掉后面无用的零
            doorPrice = doorPrice.replaceAll("[.]$", "");//如小数点后面全是零则去掉小数点
        }

        return doorPrice;
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

    /**
     * @param v
     */
    @Override
    @OnClick({R.id.ll_add_lock_pic, R.id.rl_set_time_price,
            R.id.rl_set_Business_date, R.id.btn_save, R.id.btn_delete, R.id.add_no_busi_date_btn, R.id.add_time_price_btn})
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ll_add_lock_pic:
                selectPhoto();
                break;
            case R.id.rl_set_Business_date:

                if (firstNoDate % 2 == 0) {
                    laySetBusinessDate.setVisibility(View.GONE);
                    imgDateArrow.setImageDrawable(getResources().getDrawable(R.drawable.icon_down_arrow));
                } else {
                    laySetBusinessDate.setVisibility(View.VISIBLE);
                    imgDateArrow.setImageDrawable(getResources().getDrawable(R.drawable.icon_up_arrow));
                }
                firstNoDate++;
                break;
            case R.id.rl_set_time_price:
                if (firstTime % 2 == 0) {
                    layoutSetTimePrice.setVisibility(View.GONE);
                    imgTimeArrow.setImageDrawable(getResources().getDrawable(R.drawable.icon_down_arrow));
                } else {
                    layoutSetTimePrice.setVisibility(View.VISIBLE);
                    imgTimeArrow.setImageDrawable(getResources().getDrawable(R.drawable.icon_up_arrow));
                }
                firstTime++;
                break;
            case R.id.add_no_busi_date_btn:
                addNoBusiDate();
                break;
            case R.id.add_time_price_btn:
                MyDialog(mContext);
                break;
            case R.id.btn_save:
                saveLockInfo();
                break;
            case R.id.btn_delete:
                deleteLockInfo();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                mSelectPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                if (data != null) {
                    mSelectPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                }
                selectedPhotos.clear();

                if (mSelectPath != null) {
                    isModifyImg = true;
                    selectedPhotos.addAll(mSelectPath);
                }
                photoAdapter.notifyDataSetChanged();
            }
        }
    }

//    //此时MainActivity作为订阅者
//    public void onEvent(MyEvent event) {
//        Log.d("", "onEvent >> callback main>>>" + event.getMsg());
//
//        UIHelper.showToast(LockInfoActivity.this, event.getMsg());
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkHttpUtils.getInstance().cancelTag(this);
    }


    public class MyTimesAdapter extends RecyclerView.Adapter<MyTimesAdapter.ViewHolder> {

        private ArrayList<Map<String, Object>> datas;

        public MyTimesAdapter(ArrayList<Map<String, Object>> data) {
            datas = timePriceList;
        }

        @Override
        public MyTimesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_business_time_price_recyclerview, parent, false);
            // pass MyCustomEditTextListener to viewholder in onCreateViewHolder
            // so that we don't have to do this expensive allocation in onBindViewHolder
            ViewHolder vh = new ViewHolder(v, new MyCustomEditTextListener());

            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            // update MyCustomEditTextListener every time we bind a new item
            // so that it knows what item in mDataset to update
            final Map<String, Object> item = (Map<String, Object>) timePriceList.get(position);

            holder.myCustomEditTextListener.updatePosition(position);
            holder.seqTimes.setText(item.get("date").toString());
            if (position == 6) {
                holder.mSort.setImageDrawable(getResources().getDrawable(R.drawable.icon_moon));
            } else {
                holder.mSort.setImageDrawable(getResources().getDrawable(R.drawable.icon_sun));
            }
            if (!CommonUtils.isEmpty(item.get("salePrice").toString())) {
                holder.mEditText.setText(item.get("salePrice").toString());
            }
        }

        @Override
        public int getItemCount() {
            return timePriceList.size();
        }


        public class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            public EditText mEditText;
            public ImageView mSort;
            public TextView seqTimes;
            public MyCustomEditTextListener myCustomEditTextListener;

            public ViewHolder(View v, MyCustomEditTextListener myCustomEditTextListener) {
                super(v);

                this.mEditText = (EditText) v.findViewById(R.id.tv_time_price);
                this.mSort = (ImageView) v.findViewById(R.id.sort);
                this.seqTimes = (TextView) v.findViewById(R.id.no_business_date);
                this.myCustomEditTextListener = myCustomEditTextListener;
                this.mEditText.addTextChangedListener(myCustomEditTextListener);
            }
        }

        // we make TextWatcher to be aware of the position it currently works with
        // this way, once a new item is attached in onBindViewHolder, it will
        // update current position MyCustomEditTextListener, reference to which is kept by ViewHolder
        private class MyCustomEditTextListener implements TextWatcher {
            private int position;

            public void updatePosition(int position) {
                this.position = position;
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                // no op
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                timePriceList.get(position).put("salePrice", charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // no op
            }
        }
    }

}
