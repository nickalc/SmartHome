package com.nick.smarthome.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.github.obsessive.library.eventbus.EventCenter;
import com.github.obsessive.library.netstatus.NetUtils;
import com.nick.smarthome.R;
import com.nick.smarthome.ui.base.BaseSwipeBackActivity;

import butterknife.InjectView;

/**
 * Author:  nick
 * Email:   nickdevp@gmail.com
 * Date:    15/12/12 20:40.
 * Description:
 */
public class PayFinishActivity extends BaseSwipeBackActivity {


    @InjectView(R.id.tv_house_title)
    TextView tvHouseTitle;

    @InjectView(R.id.tv_house_addr)
    TextView tvHouseAddr;

    @InjectView(R.id.tv_room_no)
    TextView tvRoomNo;

    @InjectView(R.id.tv_room_type)
    TextView tvRoomType;

    @InjectView(R.id.tv_order_code)
    TextView tvOrderCode;

    @InjectView(R.id.tv_order_price)
    TextView tvOrderPrice;

    private String houseTitle, houseAddr, housePrice, orderCode, roomNo, roomType;

    protected int getActionBarTitle() {
        return R.string.pay_finish;
    }

    @Override
    protected boolean isApplyKitKatTranslucency() {
        return true;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

        houseTitle = extras.getString("houseTitle");
        houseAddr = extras.getString("houseAddr");
        housePrice = extras.getString("housePrice");
        orderCode = extras.getString("orderCode");
        roomNo = extras.getString("roomNo");
        roomType = extras.getString("roomType");
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_pay_finish;
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

        tvHouseTitle.setText(houseTitle);
        tvHouseAddr.setText(houseAddr);
        tvRoomNo.setText(roomNo);
        if (roomType.equals("ROOM")) {
            tvRoomType.setText("房间");
        } else {
            tvRoomType.setText("客厅");
        }
        tvOrderCode.setText(orderCode);
        tvOrderPrice.setText(housePrice + "元");
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
}
