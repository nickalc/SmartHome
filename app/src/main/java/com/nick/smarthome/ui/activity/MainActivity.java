package com.nick.smarthome.ui.activity;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

import com.github.obsessive.library.eventbus.EventCenter;
import com.github.obsessive.library.netstatus.NetUtils;
import com.nick.smarthome.R;
import com.nick.smarthome.interf.OnTabReselectListener;
import com.nick.smarthome.ui.base.BaseActivity;
import com.nick.smarthome.ui.base.MainTab;
import com.nick.smarthome.utils.UIHelper;
import com.nick.smarthome.view.base.BaseView;
import com.nick.smarthome.widgets.MyFragmentTabHost;
import com.umeng.update.UmengUpdateAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

public class MainActivity extends BaseActivity implements BaseView, View.OnClickListener,
        OnTabChangeListener, OnTouchListener {

    private static long DOUBLE_CLICK_TIME = 0L;
    private String tabName = "附近";

    @InjectView(android.R.id.tabhost)
    public MyFragmentTabHost mTabHost;


    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_main;
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
        UmengUpdateAgent.update(this);

        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        setTitle(R.string.app_name);

        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        if (android.os.Build.VERSION.SDK_INT > 10) {
            mTabHost.getTabWidget().setShowDividers(0);
        }

        initTabs();


        mTabHost.setCurrentTab(0);
        mTabHost.setOnTabChangedListener(this);
    }


    private void initTabs() {
        MainTab[] tabs = MainTab.values();
        final int size = tabs.length;
        for (int i = 0; i < size; i++) {
            MainTab mainTab = tabs[i];
            TabHost.TabSpec tab = mTabHost.newTabSpec(getString(mainTab.getResName()));
            View indicator = LayoutInflater.from(getApplicationContext())
                    .inflate(R.layout.tab_indicator, null);
            TextView title = (TextView) indicator.findViewById(R.id.tab_title);
            Drawable drawable = this.getResources().getDrawable(
                    mainTab.getResIcon());
            title.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null,
                    null);
//            if (i == 2) {
//                indicator.setVisibility(View.INVISIBLE);
//                mTabHost.setNoTabChangedTag(getString(mainTab.getResName()));
//            }
            title.setText(getString(mainTab.getResName()));
            tab.setIndicator(indicator);
            tab.setContent(new TabHost.TabContentFactory() {

                @Override
                public View createTabContent(String tag) {
                    return new View(MainActivity.this);
                }
            });
            mTabHost.addTab(tab, mainTab.getClz(), null);

            if (mainTab.equals(MainTab.ME)) {
//                View cn = indicator.findViewById(R.id.tab_mes);
//                mBvNotice = new BadgeView(MainActivity.this, cn);
//                mBvNotice.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
//                mBvNotice.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
//                mBvNotice.setBackgroundResource(R.drawable.notification_bg);
//                mBvNotice.setGravity(Gravity.CENTER);
            }
            mTabHost.getTabWidget().getChildAt(i).setOnTouchListener(this);
        }
    }


    @Override
    public void onTabChanged(String tabId) {
        final int size = mTabHost.getTabWidget().getTabCount();
        for (int i = 0; i < size; i++) {
            View v = mTabHost.getTabWidget().getChildAt(i);
            if (i == mTabHost.getCurrentTab()) {
                v.setSelected(true);
            } else {
                v.setSelected(false);
            }
        }
        if (tabId.equals(getString(MainTab.ME.getResName()))) {
//            mBvNotice.setText("");
//            mBvNotice.hide();
        }
        tabName = tabId;
        supportInvalidateOptionsMenu();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {

            default:
                break;
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        super.onTouchEvent(event);
        boolean consumed = false;
        // use getTabHost().getCurrentTabView to decide if the current tab is
        // touched again
        if (event.getAction() == MotionEvent.ACTION_DOWN
                && v.equals(mTabHost.getCurrentTabView())) {
            // use getTabHost().getCurrentView() to get a handle to the view
            // which is displayed in the tab - and to get this views context
            Fragment currentFragment = getCurrentFragment();
            if (currentFragment != null
                    && currentFragment instanceof OnTabReselectListener) {
                OnTabReselectListener listener = (OnTabReselectListener) currentFragment;
                listener.onTabReselect();
                consumed = true;
            }
        }
        return consumed;
    }

    private Fragment getCurrentFragment() {
        return getSupportFragmentManager().findFragmentByTag(
                mTabHost.getCurrentTabTag());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        MenuItem actionSettings = menu.findItem(R.id.action_search);
        if (tabName.equals("附近")) {
            actionSettings.setVisible(true);
        } else {
            actionSettings.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            case R.id.action_search:
//                if (!UIHelper.isLogin(mContext)) {
//                    UIHelper.showLogin(mContext);
//                } else {
//                    // readyGo(PublishHouseActivity.class);
//                }
                mTabHost.setCurrentTab(1);
                //culaPrice();
                break;
            case R.id.action_repeat:
                if (!UIHelper.isLogin(mContext)) {
                    UIHelper.showLogin(mContext);
                } else {
                    readyGo(AuthenticationActivity.class);
                }
                break;
            case R.id.action_my_info:
                if (!UIHelper.isLogin(mContext)) {
                    UIHelper.showLogin(mContext);
                } else {
                    readyGo(PersonalInformationActivity.class);
                }
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
    protected boolean isApplyKitKatTranslucency() {
        return true;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_MENU) {

            return true;
        } else if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {

            if ((System.currentTimeMillis() - DOUBLE_CLICK_TIME) > 2000) {
                //showToast(getString(R.string.double_click_exit));
                UIHelper.showToast(mContext, getString(R.string.double_click_exit));
                DOUBLE_CLICK_TIME = System.currentTimeMillis();
            } else {
                getBaseApplication().exitApp();
            }

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    private void culaPrice(){
         int[] A = { 0, 10, 15, 19, 23, 26, 28, 50 };//价格，2小时、4小时、6小时...价格分别为10，15，19...夜间价格50，左侧补一个0表示0小时价格
        //int[] X = { 0, 1, 1, 0, 1, 0, 1, 1, 0 };//1天
        //int[] X = { 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0 };//3天，共3*7+2=23个。实际上可以订7天，对应7*7+2=51个
        // int[] X = { 0, 1, 1, 0, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 0, 1, 1, 1, 1, 0, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 0, 1, 1, 1, 1, 0, 1, 0, 1, 1, 0 };//7天
        int[] X = { 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0 };//7天

        int[] S = new int[X.length - 1];
        int[] E = new int[X.length - 1];
        List<Integer> TS = new ArrayList<Integer>();
        List<Integer> TE = new ArrayList<Integer>();
        for (int i = 0; i < S.length; i++)
        {
            if (X[i] == 0 && X[i + 1] == 1)
            {
                S[i] = 1;
                TS.add(i);
            }
        }
        for (int i = 0; i < E.length; i++)
        {
            if (X[i] == 1 && X[i + 1] == 0)
            {
                E[i] = 1;
                TE.add(i);
            }
        }
        int[] TA = new int[TS.size()];
        int[] TB = new int[TS.size()];
        for (int i = 0; i < TS.size(); i++)
        {
            TB[i] = (TE.get(i) - TS.get(i)) / 7;
            if (TE.get(i) % 7 < TS.get(i) % 7)
                TB[i]++;
            TA[i] = TE.get(i) - TS.get(i) - TB[i];
        }

        int[] P = new int[TS.size()];
        for (int i = 0; i < TS.size(); i++)
            P[i] = A[TA[i] % 7] + (A[1] + A[2] + A[3] + A[4] + A[5] + A[6]) * (TA[i] / 7) + A[7] * TB[i];

        int Psum = 0;

        for (int i=0; i<P.length; i++)
            Psum += P[i];


        UIHelper.showToast(mContext,String.valueOf(Psum));

    }
}
