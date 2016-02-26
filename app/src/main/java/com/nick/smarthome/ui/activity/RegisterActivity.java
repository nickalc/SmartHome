package com.nick.smarthome.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.okhttp.OkHttpUtils;
import com.android.okhttp.callback.StringCallback;
import com.github.obsessive.library.base.BaseWebActivity;
import com.github.obsessive.library.eventbus.EventCenter;
import com.github.obsessive.library.netstatus.NetUtils;
import com.github.obsessive.library.utils.CommonUtils;
import com.nick.smarthome.R;
import com.nick.smarthome.api.ServerApiConstants;
import com.nick.smarthome.common.Constants;
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
public class RegisterActivity extends BaseSwipeBackActivity implements View.OnClickListener{

    private static final String TAG = "RegisterActivity";

    @InjectView(R.id.phone_ly)
    LinearLayout mPhoneLy;

    @InjectView(R.id.password_ly)
    LinearLayout mPasswordLy;

    @InjectView(R.id.btn_do_register)
    Button mDoRegister;

    @InjectView(R.id.btn_send_code)
    TextView sendCodeBtn;

    @InjectView(R.id.service_terms)
    TextView serviceTerms;

    @InjectView(R.id.edt_phone)
    ClearEditText edtPhone;

    @InjectView(R.id.edt_code)
    ClearEditText edtCode;

    @InjectView(R.id.edt_invite_code)
    ClearEditText edtInviteCode;

    @InjectView(R.id.edt_password)
    ClearEditText edtPassword;

    @InjectView(R.id.edt_password_again)
    ClearEditText edtPasswordAgain;

    @InjectView(R.id.ll_service_terms)
    LinearLayout llServiceTerms;

    private boolean isValid = false;

    private String phoneNumber,validCode,inviteCode,password,aPassword;

    @Override
    protected int getActionBarTitle() {
        return R.string.register;
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
        return R.layout.activity_register;
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

        serviceTerms.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG ); //下划线
        serviceTerms.getPaint().setAntiAlias(true);//抗锯齿

    }

    private void getValidCode(){

        phoneNumber = edtPhone.getText().toString();


        if (CommonUtils.isEmpty(phoneNumber)){
            UIHelper.showToast(RegisterActivity.this,"手机号不能为空");
            return;
        }

        String url = ServerApiConstants.Urls.GET_VALID_CODE_URLS;
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
            UIHelper.showToast(RegisterActivity.this, "获取验证码失败");
        }

        @Override
        public void onResponse(String response)
        {
            try {
                JSONObject retJson = new JSONObject(response);
                String statuscode = retJson.optString("statuscode");
                String message = retJson.optString("message");
                UIHelper.showToast(RegisterActivity.this, message);
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

    private void doRegister(){

        password = edtPassword.getText().toString();
        aPassword = edtPasswordAgain.getText().toString();

        if (CommonUtils.isEmpty(password)){
            UIHelper.showToast(RegisterActivity.this,"密码不能为空");
            return;
        }

        if (CommonUtils.isEmpty(aPassword)){
            UIHelper.showToast(RegisterActivity.this,"请输入确认密码");
            return;
        }

        String url = ServerApiConstants.Urls.SUBMIT_REGISTER_URLS;

        OkHttpUtils.
                post()
                .url(url)
                .addParams("phoneNumber",phoneNumber)
                .addParams("password",password)
                .addParams("repassword",aPassword)
                .addParams("invitationCode",inviteCode)
                .build()
                .execute(new DoRegisterCallback());

    }

    private void doValidCode(){

        validCode = edtCode.getText().toString();
        phoneNumber = edtPhone.getText().toString();
        inviteCode = edtInviteCode.getText().toString();

        if (CommonUtils.isEmpty(phoneNumber)){
            UIHelper.showToast(RegisterActivity.this,"手机号不能为空");
            return;
        }

        if (CommonUtils.isEmpty(validCode)){
            UIHelper.showToast(RegisterActivity.this,"验证码不能为空");
            return;
        }

        String url = ServerApiConstants.Urls.VALIDATION_PHONE_CODE_URLS;
        OkHttpUtils
                .get()
                .url(url)
                .addParams("phoneNumber",phoneNumber)
                .addParams("vCode",validCode)
                .build()
                .execute(new ValidCodeCallback());

    }

    public class ValidCodeCallback extends StringCallback
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
            UIHelper.showToast(RegisterActivity.this, "验证码验证失败");
        }

        @Override
        public void onResponse(String response){

            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(response);
                String statusCode = jsonObject.optString("statuscode");
                String message = jsonObject.optString("message");
                if (statusCode.equals("1")) {
                    UIHelper.showToast(RegisterActivity.this, message);
                    isValid = true;
                    mPhoneLy.setVisibility(View.GONE);
                    mPasswordLy.setVisibility(View.VISIBLE);
                    llServiceTerms.setVisibility(View.VISIBLE);
                    mDoRegister.setText("完成注册");
                }else {
                    UIHelper.showToast(RegisterActivity.this, message);
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

    public class DoRegisterCallback extends StringCallback
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
            UIHelper.showToast(RegisterActivity.this,"注册失败");
        }

        @Override
        public void onResponse(String response){

            try {
                JSONObject jsonObject = new JSONObject(response);

                String statusCode = jsonObject.optString("statuscode");
                String message = jsonObject.optString("message");
                JSONObject data = jsonObject.getJSONObject("data");
                if (statusCode.equals("1")) {
                    //保存账号密码到缓存sharedpreferences
                    SharedPreferences sharedPreferences = getSharedPreferences(
                            "secrecy", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("customerId", data.optString("customerId"));
                    editor.putString("phoneNumber", phoneNumber);
                    editor.commit();

                    UIHelper.showToast(RegisterActivity.this, message);
                    finish();
                    setResult(Activity.RESULT_OK);
                    sendBroadcast(new Intent(Constants.INTENT_ACTION_USER_CHANGE));
                    readyGo(AuthenticationActivity.class);//跳转用户身份验证
                }else {
                    UIHelper.showToast(RegisterActivity.this,message);
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
    @OnClick({R.id.btn_do_register,R.id.btn_send_code,R.id.service_terms})
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btn_do_register:
                if (!isValid) {
                    doValidCode();
                }else {
                    doRegister();
                }
                break;
            case R.id.btn_send_code:
                getValidCode();
                break;
            case R.id.service_terms:
                Bundle bundle = new Bundle();
                bundle.putString("BUNDLE_KEY_URL","http://xuan.3g.cn");
                bundle.putString("BUNDLE_KEY_TITLE","用户服务条款");
                readyGo(BaseWebActivity.class,bundle);
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
