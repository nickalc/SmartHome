package com.nick.smarthome.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.obsessive.library.eventbus.EventCenter;
import com.github.obsessive.library.netstatus.NetUtils;
import com.nick.smarthome.R;
import com.nick.smarthome.ui.base.BaseSwipeBackActivity;
import com.nick.smarthome.ui.dialog.ShareDialog;
import com.nick.smarthome.utils.DialogHelp;
import com.nick.smarthome.utils.FileUtil;
import com.nick.smarthome.utils.MethodsCompat;
import com.nick.smarthome.utils.UIHelper;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

import java.io.File;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Author:  nick
 * Email:   nickdevp@gmail.com
 * Date:    15/12/10 00:09.
 * Description:
 */
public class SettingActivity extends BaseSwipeBackActivity implements View.OnClickListener{

    @InjectView(R.id.rl_clean_cache)
    RelativeLayout mRlCleanCache;

    @InjectView(R.id.rl_check_update)
    RelativeLayout mRlCheckUpdate;

    @InjectView(R.id.rl_about)
    RelativeLayout mRlAbout;

    @InjectView(R.id.rl_share)
    RelativeLayout rlShare;

    @InjectView(R.id.tv_cache_size)
    TextView tvCacheSize;

    @InjectView(R.id.rl_exit)
    RelativeLayout mRlExit;

    ProgressDialog checkDialog;

    @Override
    protected boolean isApplyKitKatTranslucency() {
        return true;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_setting;
    }

    @Override
    protected void onEventComming(EventCenter eventCenter) {

    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.my_setting;
    }

    @Override
    protected void initViewsAndEvents() {
        checkDialog = DialogHelp.getWaitDialog(this, "正在检测更新...");

        caculateCacheSize();
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
    @OnClick({R.id.rl_clean_cache,R.id.rl_check_update,R.id.rl_share,R.id.rl_about,R.id.rl_exit})
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.rl_clean_cache:
                onClickCleanCache();
                break;
            case R.id.rl_check_update:
                checkUpdate();
                break;
            case R.id.rl_share:
                shareDial();
                break;
            case R.id.rl_about:
                readyGo(AboutActivity.class);
                break;
            case R.id.rl_exit:
                onClickExit();
                break;
            default:
                break;
        }
    }


    private void shareDial(){

        final ShareDialog dialog = new ShareDialog(mContext);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setTitle(R.string.share_to);
        dialog.setShareInfo("分享自手源", "分享自手源："+"http://www.pgyer.com/smarthomeapp", "http://www.pgyer.com/smarthomeapp");
        dialog.show();

    }

    private void checkUpdate(){
        checkDialog.show();
        UmengUpdateAgent.setUpdateAutoPopup(false);
        UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
            @Override
            public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
                switch (updateStatus) {
                    case UpdateStatus.Yes: // has update
                        UmengUpdateAgent.showUpdateDialog(mContext, updateInfo);
                        break;
                    case UpdateStatus.No: // has no update
                        Toast.makeText(mContext, "已经是最新版本", Toast.LENGTH_SHORT).show();
                        break;
                    case UpdateStatus.NoneWifi: // none wifi
                        Toast.makeText(mContext, "没有wifi连接， 只在wifi下更新", Toast.LENGTH_SHORT).show();
                        break;
                    case UpdateStatus.Timeout: // time out
                        Toast.makeText(mContext, "超时", Toast.LENGTH_SHORT).show();
                        break;
                }
                checkDialog.hide();
            }
        });
        UmengUpdateAgent.update(this);
       // UmengUpdateAgent.forceUpdate(mContext);
    }


    /**
     * 计算缓存的大小
     */
    private void caculateCacheSize() {
        long fileSize = 0;
        String cacheSize = "0KB";
        File filesDir = getFilesDir();
        File cacheDir = getCacheDir();

        fileSize += FileUtil.getDirSize(filesDir);
        fileSize += FileUtil.getDirSize(cacheDir);
        // 2.2版本才有将应用缓存转移到sd卡的功能
        if (UIHelper.isMethodsCompat(android.os.Build.VERSION_CODES.FROYO)) {
            File externalCacheDir = MethodsCompat
                    .getExternalCacheDir(this);
            fileSize += FileUtil.getDirSize(externalCacheDir);
        }
        if (fileSize > 0)
            cacheSize = FileUtil.formatFileSize(fileSize);
        //tvCacheSize.setText(cacheSize);
    }

    private void onClickCleanCache() {
        DialogHelp.getConfirmDialog(this, "是否清空缓存?", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
             //   UIHelper.clearAppCache(getActivity());
              //  tvCacheSize.setText("0KB");
                UIHelper.showToast(mContext,"已清空缓存");
            }
        }).show();
    }

    /**退出应用*/
    private void onClickExit() {


        DialogHelp.getConfirmDialog(this, "确定退出?", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                //如果没有选择自动登陆 则去掉密码保存
                SharedPreferences sharedPreferences = getSharedPreferences(
                        "secrecy", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();

                getBaseApplication().exitApp();
                finish();
            }
        }).show();

    }
}
