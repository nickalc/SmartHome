package com.nick.smarthome.ui.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.okhttp.OkHttpUtils;
import com.android.okhttp.callback.StringCallback;
import com.github.obsessive.library.eventbus.EventCenter;
import com.github.obsessive.library.netstatus.NetUtils;
import com.github.obsessive.library.utils.CommonUtils;
import com.nick.smarthome.R;
import com.nick.smarthome.api.ServerApiConstants;
import com.nick.smarthome.ui.base.BaseSwipeBackActivity;
import com.nick.smarthome.utils.UIHelper;
import com.nick.smarthome.widgets.ClearEditText;
import com.squareup.okhttp.Request;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Author:  nick
 * Email:   nickdevp@gmail.com
 * Date:    15/12/10 17:53.
 * Description:
 */
public class ForgotPasswordActivity extends BaseSwipeBackActivity implements View.OnClickListener{

    private static final String TAG = "RegisterActivity";

    @InjectView(R.id.phone_ly)
    LinearLayout mPhoneLy;

    @InjectView(R.id.btn_do_register)
    Button mDoRegister;

    @InjectView(R.id.btn_send_code)
    TextView sendCodeBtn;

    @InjectView(R.id.edt_phone)
    ClearEditText edtPhone;

    @InjectView(R.id.edt_code)
    ClearEditText edtCode;

    @InjectView(R.id.edt_password)
    ClearEditText edtPassword;

    @InjectView(R.id.edt_password_again)
    ClearEditText edtPasswordAgain;

    private TimeCount timeCount;

    private String phoneNumber,validCode,password,aPassword;

    @Override
    protected int getActionBarTitle() {
        return R.string.find_password;
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
        return R.layout.activity_forgot_password;
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

    }

    private void getValidCode(){

        phoneNumber = edtPhone.getText().toString();


        if (CommonUtils.isEmpty(phoneNumber)){
            UIHelper.showToast(ForgotPasswordActivity.this,"手机号不能为空");
            return;
        }

        String url = ServerApiConstants.Urls.GET_FORGOT_VALID_CODE_URLS;
        OkHttpUtils
                .get()
                .url(url)
                .addParams("phoneNumber",phoneNumber)
                .build()
                .execute(new MyStringCallback());

    }

    public class MyStringCallback extends StringCallback
    {
        @Override
        public void onBefore(Request request)
        {
            super.onBefore(request);
        }

        @Override
        public void onAfter()
        {
            super.onAfter();
        }

        @Override
        public void onError(Request request, Exception e)
        {
            UIHelper.showToast(ForgotPasswordActivity.this, "获取验证码失败");
        }

        @Override
        public void onResponse(String response)
        {
            try {
                JSONObject retJson = new JSONObject(response);
                String statuscode = retJson.optString("statuscode");
                if (statuscode.equals("1")) {
                    timeCount = new TimeCount(60 * 1000, 1000);
                    timeCount.start();
                }
                String message = retJson.optString("message");
                UIHelper.showToast(ForgotPasswordActivity.this, message);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void inProgress(float progress)
        {
            Log.e(TAG, "inProgress:" + progress);
          //  mProgressBar.setProgress((int) (100 * progress));
        }
    }


    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
//            btn_code.setTextColor(getResources().getColor(R.color.orange));
        }

        @Override
        public void onFinish() {
            sendCodeBtn.setEnabled(true);
            sendCodeBtn.setBackgroundColor(getResources().getColor(R.color.light_orange));
            sendCodeBtn.setText("重新获取");
        }

        @Override
        public void onTick(long millisUntilFinished) {
            sendCodeBtn.setEnabled(false);
            sendCodeBtn.setBackgroundColor(getResources().getColor(R.color.light_gray));
            sendCodeBtn.setText(millisUntilFinished / 1000 +"s");
        }

    }

    private void doResetPsd(){

        validCode = edtCode.getText().toString();
        phoneNumber = edtPhone.getText().toString();
        password = edtPassword.getText().toString();
        aPassword = edtPasswordAgain.getText().toString();

        if (CommonUtils.isEmpty(password)){
            UIHelper.showToast(ForgotPasswordActivity.this,"密码不能为空");
            return;
        }

        if (CommonUtils.isEmpty(aPassword)){
            UIHelper.showToast(ForgotPasswordActivity.this,"请输入确认密码");
            return;
        }

        String url = ServerApiConstants.Urls.SUBMIT_RESET_PSD_URLS;

        OkHttpUtils.
                post()
                .url(url)
                .addParams("phoneNumber",phoneNumber)
                .addParams("password",password)
                .addParams("repassword",aPassword)
                .addParams("vCode",validCode)
                .build()
                .execute(new DoResetCallback());

    }


    public class DoResetCallback extends StringCallback
    {
        @Override
        public void onBefore(Request request){
            super.onBefore(request);
        }

        @Override
        public void onAfter() {
            super.onAfter();
        }

        @Override
        public void onError(Request request, Exception e){
            UIHelper.showToast(ForgotPasswordActivity.this,"重置密码失败");
        }

        @Override
        public void onResponse(String response){

            try {
                JSONObject jsonObject = new JSONObject(response);

                String statusCode = jsonObject.optString("statuscode");
                String message = jsonObject.optString("message");
                if (statusCode.equals("1")) {
                    UIHelper.showToast(ForgotPasswordActivity.this, message);
                    timeCount.cancel();
                    finish();
                }else {
                    UIHelper.showToast(ForgotPasswordActivity.this,message);
                }
            } catch (JSONException e) {
                e.printStackTrace();

            }

        }

        @Override
        public void inProgress(float progress){
            Log.e(TAG, "inProgress:" + progress);
        }
    }

    @Override
    @OnClick({R.id.btn_do_register,R.id.btn_send_code})
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btn_do_register:
                doResetPsd();
                break;
            case R.id.btn_send_code:
                getValidCode();
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


}
