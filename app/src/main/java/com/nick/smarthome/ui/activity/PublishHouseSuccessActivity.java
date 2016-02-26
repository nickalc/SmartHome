package com.nick.smarthome.ui.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.obsessive.library.eventbus.EventCenter;
import com.github.obsessive.library.netstatus.NetUtils;
import com.google.zxing.WriterException;
import com.nick.smarthome.R;
import com.nick.smarthome.ui.base.BaseSwipeBackActivity;
import com.nick.smarthome.utils.QrCodeUtils;

import butterknife.InjectView;

/**
 * Author:  nick
 * Email:   nickdevp@gmail.com
 * Date:    16/1/6 21:55.
 * Description:
 */
public class PublishHouseSuccessActivity extends BaseSwipeBackActivity {

    @InjectView(R.id.tv_house_title)
    TextView tvHouseTitle;

    @InjectView(R.id.iv_house_qrcode)
    ImageView ivHouseQrcode;

    @InjectView(R.id.tv_house_addr)
    TextView tvHouseAddr;

    private String houseTitle,houseAdress;
    private Bitmap qrBitmap;

    protected int getActionBarTitle() {
        return R.string.publish_success;
    }

    @Override
    protected boolean isApplyKitKatTranslucency() {
        return true;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

        houseTitle = extras.getString("houseTitle");
        houseAdress = extras.getString("houseAddr");
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_publish_house_success;
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
        tvHouseAddr.setText(houseAdress);
        try {
            ivHouseQrcode.setImageBitmap(QrCodeUtils.Create2DCode("1212323"));
        } catch (WriterException e) {
            e.printStackTrace();
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



}
