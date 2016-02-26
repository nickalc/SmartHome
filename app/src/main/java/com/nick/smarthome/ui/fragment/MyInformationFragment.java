package com.nick.smarthome.ui.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.obsessive.library.eventbus.EventCenter;
import com.github.obsessive.library.utils.CommonUtils;
import com.nick.smarthome.R;
import com.nick.smarthome.common.Constants;
import com.nick.smarthome.ui.activity.MyHouseActicity;
import com.nick.smarthome.ui.activity.MyKeyActivity;
import com.nick.smarthome.ui.activity.MyLockActivity;
import com.nick.smarthome.ui.activity.PersonalInformationActivity;
import com.nick.smarthome.ui.activity.SettingActivity;
import com.nick.smarthome.ui.activity.TradeInformationActivity;
import com.nick.smarthome.ui.base.BaseFragment;
import com.nick.smarthome.utils.UIHelper;
import com.nick.smarthome.widgets.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Author:  nick
 * Email:   nickdevp@gmail.com
 * Date:    15/12/9 14:13.
 * Description: 我的
 */
public class MyInformationFragment extends BaseFragment implements View.OnClickListener{

    @InjectView(R.id.rootview)
    LinearLayout mRootview;

    @InjectView(R.id.rl_key)
    RelativeLayout rlKey;

    @InjectView(R.id.rl_house)
    RelativeLayout rlHouse;

    @InjectView(R.id.rl_lock)
    RelativeLayout rlLock;

    @InjectView(R.id.rl_trade)
    RelativeLayout rlTrade;

    @InjectView(R.id.rl_setting)
    RelativeLayout rlSetting;

    @InjectView(R.id.iv_user_img)
    CircleImageView userImg;

    @InjectView(R.id.tv_username)
    TextView tvUserName;

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Constants.INTENT_ACTION_LOGOUT)) {

            } else if (action.equals(Constants.INTENT_ACTION_USER_CHANGE)) {
                steupUser();
            } else if (action.equals(Constants.INTENT_ACTION_NOTICE)) {

            }
        }
    };

    @Override
    protected void onFirstUserVisible() {

    }

    @Override
    protected void onUserVisible() {

    }

    @Override
    protected void onUserInvisible() {

       // rlSetting.setOnClickListener(this);
    }

    @Override
    protected View getLoadingTargetView() {
        return mRootview;
    }

    @Override
    protected void initViewsAndEvents() {

        IntentFilter filter = new IntentFilter(Constants.INTENT_ACTION_LOGOUT);
        filter.addAction(Constants.INTENT_ACTION_USER_CHANGE);
        getActivity().registerReceiver(mReceiver, filter);

        steupUser();

    }

    private void steupUser() {
        SharedPreferences settings = getActivity().getSharedPreferences("secrecy", Activity.MODE_PRIVATE);
        String phoneNumber = settings.getString("phoneNumber", "未登录");
        String customerImg = settings.getString("customerImg","");
        String custName = settings.getString("custName","");
        String nickname = settings.getString("nickname","");
        if (!CommonUtils.isEmpty(customerImg)) {
            ImageLoader.getInstance().displayImage(customerImg,userImg);
        }

        if (!CommonUtils.isEmpty(custName)){
            tvUserName.setText(custName);
        }else if(!CommonUtils.isEmpty(nickname)){
            tvUserName.setText(nickname);
        }else {
            tvUserName.setText(phoneNumber);
        }


    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_my_information;
    }

    @Override
    protected void onEventComming(EventCenter eventCenter) {

    }

    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }


    @Override
    @OnClick({R.id.rl_setting,R.id.rl_lock,R.id.rl_key,R.id.rl_house,R.id.rl_trade,R.id.iv_user_img})
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.rl_key:// 我的钥匙
                if (!UIHelper.isLogin(mContext)) {
                    UIHelper.showLogin(mContext);
                }else {
                    readyGo(MyKeyActivity.class);
                }
                break;
            case R.id.rl_trade:// 交易信息
                if (!UIHelper.isLogin(mContext)) {
                    UIHelper.showLogin(mContext);
                }else {
                    readyGo(TradeInformationActivity.class);
                }
                break;
            case R.id.rl_house:// 我的房源
                if (!UIHelper.isLogin(mContext)) {
                    UIHelper.showLogin(mContext);
                }else {
                    readyGo(MyHouseActicity.class);
                }
                break;
            case R.id.rl_lock:// 我的锁
                if (!UIHelper.isLogin(mContext)) {
                    UIHelper.showLogin(mContext);
                }else {
                    readyGo(MyLockActivity.class);
                }
                break;
            case R.id.rl_setting:  // 设置
                readyGo(SettingActivity.class);
                break;
            case R.id.iv_user_img:
                if (!UIHelper.isLogin(mContext)) {
                    UIHelper.showLogin(mContext);
                }else {
                    readyGo(PersonalInformationActivity.class);
                }
                break;
            default:
                break;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mReceiver);
    }
}
