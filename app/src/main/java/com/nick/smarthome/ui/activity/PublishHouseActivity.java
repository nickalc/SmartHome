package com.nick.smarthome.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.android.okhttp.OkHttpUtils;
import com.github.obsessive.library.eventbus.EventCenter;
import com.github.obsessive.library.netstatus.NetUtils;
import com.github.obsessive.library.utils.CommonUtils;
import com.google.gson.Gson;
import com.nick.smarthome.R;
import com.nick.smarthome.api.ServerApiConstants;
import com.nick.smarthome.bean.CommResult;
import com.nick.smarthome.bean.MyLockEntity;
import com.nick.smarthome.callback.CommonCallback;
import com.nick.smarthome.ui.adapter.SelectedLockRecyclerAdapter;
import com.nick.smarthome.ui.base.BaseSwipeBackActivity;
import com.nick.smarthome.utils.DialogHelp;
import com.nick.smarthome.utils.TLog;
import com.nick.smarthome.utils.UIHelper;
import com.squareup.okhttp.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Author:  nick
 * Email:   nickdevp@gmail.com
 * Date:    15/12/12 20:39.
 * Description:发布房源
 */
public class PublishHouseActivity extends BaseSwipeBackActivity implements View.OnClickListener,
        AMapLocationListener {

    private static final String TAG = "PublishHouseActivity";

    @InjectView(R.id.rootview)
    LinearLayout mRootView;

    @InjectView(R.id.tv_title)
    EditText tvTitle;

    @InjectView(R.id.tv_addr)
    EditText tvAddr;

    @InjectView(R.id.ll_add_lock)
    LinearLayout llAddLock;

    @InjectView(R.id.btn_do_publish)
    TextView doPublishBtn;

    @InjectView(R.id.lock_list)
    RecyclerView lockRecyclerview;

    SelectedLockRecyclerAdapter selectedLockRecyclerAdapter;

    ArrayList<MyLockEntity> selectedLockList = new ArrayList<MyLockEntity>();

    private RecyclerView.LayoutManager mLayoutManager;

    private static final int REQUEST_CODE = 0x10;

    private LocationManagerProxy mLocationManagerProxy;

    private String latitude, longitude;

    private String mCustomerId;

    private String houseTitle, houseAdress;

    ProgressDialog mDialog;


    @Override
    protected int getActionBarTitle() {
        return R.string.publish_house;
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
        return R.layout.activity_publish_house;
    }

    @Override
    protected void onEventComming(EventCenter eventCenter) {

    }

    @Override
    protected View getLoadingTargetView() {
        return mRootView;
    }

    @Override
    protected void initViewsAndEvents() {
        mDialog = DialogHelp.getWaitDialog(this, "正在提交数据...");

        SharedPreferences settings = getSharedPreferences("secrecy", Activity.MODE_PRIVATE);
        mCustomerId = settings.getString("customerId", null);

        mLayoutManager = new LinearLayoutManager(this);

        lockRecyclerview.setLayoutManager(mLayoutManager);
        selectedLockRecyclerAdapter = new SelectedLockRecyclerAdapter(this, selectedLockList);

        lockRecyclerview.setAdapter(selectedLockRecyclerAdapter);

        getLng();

    }

    private void getLng() {
        // 初始化定位，只采用网络定位
        mLocationManagerProxy = LocationManagerProxy.getInstance(this);
        mLocationManagerProxy.setGpsEnable(true);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用removeUpdates()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用destroy()方法
        // 其中如果间隔时间为-1，则定位只定一次,
        //在单次定位情况下，定位无论成功与否，都无需调用removeUpdates()方法移除请求，定位sdk内部会移除
        mLocationManagerProxy.requestLocationData(
                LocationProviderProxy.AMapNetwork, -1, 15, this);
    }


    private void submitHouseInfo() {

        houseTitle = tvTitle.getText().toString();
        houseAdress = tvAddr.getText().toString();

        if (CommonUtils.isEmpty(houseTitle)) {
            UIHelper.showToast(mContext, "请先输入标题");
            return;
        }

        if (CommonUtils.isEmpty(houseAdress)) {
            UIHelper.showToast(mContext, "请先输入地址");
            return;
        }

        if (selectedLockList.size() <= 0) {
            UIHelper.showToast(mContext, "请先添加锁");
            return;
        }

        int houseNums = 0;
        int lockLen = selectedLockList.size();
        for (int i = 0; i < lockLen; i++) {
            MyLockEntity mylock = selectedLockList.get(i);
            if (mylock.getLockType().equals("HOUSE")) {
                houseNums++;
            }
        }
        if (houseNums < 1) {
            UIHelper.showToast(mContext,"请添加一个客厅");
            return;
        } else if (houseNums > 1) {
            UIHelper.showToast(mContext,"最多只能添加一个客厅");
            return;
        }

        mDialog.show();

        String url = ServerApiConstants.Urls.SUBMIT_HOUSE_INFO_URLS;

        JSONObject rnHouseInfoJson = new JSONObject();
        Gson gson = new Gson();
        String lockIdJsonStr = null;
        try {
            rnHouseInfoJson.put("longitude", longitude);
            rnHouseInfoJson.put("latitude", latitude);
            rnHouseInfoJson.put("houseAdress", houseAdress);
            rnHouseInfoJson.put("houseTitle", houseTitle);
            rnHouseInfoJson.put("customerId", mCustomerId);

            lockIdJsonStr = gson.toJson(selectedLockList);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Map<String, String> params = new HashMap<String, String>();
        params.put("rnHouseInfoJson", rnHouseInfoJson.toString());
        params.put("lockIdJson", lockIdJsonStr);

        OkHttpUtils.
                post()
                .url(url)
                .params(params)
                .build()
                .execute(new CommonCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        UIHelper.showToast(mContext, "提交信息出错");
                        mDialog.hide();
                    }

                    @Override
                    public void onResponse(CommResult response) {
                        mDialog.hide();
                        if (response.statuscode.equals("1")) {
                            Bundle bundle = new Bundle();
                            bundle.putString("houseTitle", houseTitle);
                            bundle.putString("houseAddr", houseAdress);
                            readyGo(PublishHouseSuccessActivity.class, bundle);
                            finish();
                            setResult(Activity.RESULT_OK);
                        } else {
                            UIHelper.showToast(mContext, response.message);
                        }
                    }
                });
    }


    @Override
    @OnClick({R.id.ll_add_lock, R.id.btn_do_publish})
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ll_add_lock:
                readyGoForResult(AddLockListActicity.class, REQUEST_CODE);
                break;
            case R.id.btn_do_publish:

                submitHouseInfo();

                break;
            default:
                break;
        }
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
     * @param requestCode
     * @param resultCode
     * @param data
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == REQUEST_CODE) {
                selectedLockList = (ArrayList<MyLockEntity>) data.getSerializableExtra("lockList");
                TLog.error(selectedLockList.toString());
                selectedLockRecyclerAdapter = new SelectedLockRecyclerAdapter(this, selectedLockList);

                lockRecyclerview.setAdapter(selectedLockRecyclerAdapter);
                selectedLockRecyclerAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null && amapLocation.getAMapException().getErrorCode() == 0) {
            latitude = String.valueOf(amapLocation.getLatitude());
            longitude = String.valueOf(amapLocation.getLongitude());

            String addr = amapLocation.getAddress();
            addr = addr.replaceAll(amapLocation.getProvince(), "");
//            addr = addr.replaceAll(amapLocation.getCity(), "");
            tvAddr.setText(addr);


            SharedPreferences sharedPreferences = getSharedPreferences(
                    "secrecy", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("latitude", latitude);
            editor.putString("longitude", longitude);
            editor.commit();
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
