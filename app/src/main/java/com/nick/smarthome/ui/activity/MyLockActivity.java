package com.nick.smarthome.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.github.obsessive.library.eventbus.EventCenter;
import com.github.obsessive.library.netstatus.NetUtils;
import com.github.obsessive.library.smartlayout.SmartTabLayout;
import com.nick.smarthome.R;
import com.nick.smarthome.ui.activity.qrcode.CaptureActivity;
import com.nick.smarthome.ui.adapter.MyLockViewPagerAdapter;
import com.nick.smarthome.ui.base.BaseSwipeBackActivity;

import butterknife.InjectView;

/**
 * Author:  nick
 * Email:   nickdevp@gmail.com
 * Date:    15/12/12 20:36.
 * Description:
 */
public class MyLockActivity extends BaseSwipeBackActivity{

    private String titles[] = new String[]{"未绑定", "已绑定"};

    private static final int REQUEST_CODE_ADD_LOCK = 0x10;

    @InjectView(R.id.viewpagertab)
    SmartTabLayout viewPagerTab;

    @InjectView(R.id.viewpager)
    ViewPager pager;

    @Override
    protected int getActionBarTitle() {
        return R.string.my_lock;
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
        return R.layout.activity_my_lock;
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

        pager.setAdapter(new MyLockViewPagerAdapter(getSupportFragmentManager(), titles));

        viewPagerTab.setViewPager(pager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_my_lock, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            case R.id.action_capture:
                readyGoForResult(CaptureActivity.class,REQUEST_CODE_ADD_LOCK);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK)
            return;

        switch (requestCode) {
            case REQUEST_CODE_ADD_LOCK:
              //  UIHelper.showToast(mContext,"回调");
                break;
            default:
                break;
        }
    }
}
