package com.nick.smarthome.ui.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.obsessive.library.eventbus.EventCenter;
import com.github.obsessive.library.netstatus.NetUtils;
import com.github.obsessive.library.utils.CommonUtils;
import com.nick.smarthome.R;
import com.nick.smarthome.ui.base.BaseSwipeBackActivity;
import com.nick.smarthome.utils.TDevice;

import butterknife.InjectView;
import butterknife.OnClick;

//import com.umeng.fb.FeedbackAgent;
//import com.umeng.fb.fragment.FeedbackFragment;

/**
 * Author:  nick
 * Email:   nickdevp@gmail.com
 * Date:    15/12/10 14:35.
 * Description:
 */
public class AboutActivity extends BaseSwipeBackActivity implements View.OnClickListener{

    @InjectView(R.id.tv_version_name)
    TextView mVersionName;
    @InjectView(R.id.rl_feedback)
    RelativeLayout mRlFeedback;
    @InjectView(R.id.rl_grade)
    RelativeLayout mRlGrade;

   // private FeedbackAgent mFeedbackAgent = null;

    @Override
    protected  int getActionBarTitle(){
        return R.string.about_title;
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
        return R.layout.activity_about;
    }

    @Override
    protected void onEventComming(EventCenter eventCenter) {

    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void initViewsAndEvents()  {

//        mFeedbackAgent = new FeedbackAgent(this);
//        mFeedbackAgent.sync();
//        mFeedbackAgent.closeFeedbackPush();
//        mFeedbackAgent.closeAudioFeedback();
//        mFeedbackAgent.setWelcomeInfo(getResources().getString(R.string.feedback_welcome_info));

        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_ACTIVITIES);
            String version = String.format(getResources().getString(R.string.splash_version), packageInfo.versionName);
            if (!CommonUtils.isEmpty(version)) {
                mVersionName.setText(version);
            }
        } catch (PackageManager.NameNotFoundException e) {
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

    @Override
    @OnClick({R.id.rl_feedback,R.id.rl_grade})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_feedback:
                Bundle extras = new Bundle();
              //  extras.putString(FeedbackFragment.BUNDLE_KEY_CONVERSATION_ID, mFeedbackAgent.getDefaultConversation().getId());
                readyGo(FeedBackActivity.class, extras);
                break;
            case R.id.rl_grade:
                TDevice.openAppInMarket(this);
                break;
            default:
                break;
        }

    }
}
