package com.nick.smarthome.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.android.okhttp.OkHttpUtils;
import com.android.okhttp.callback.StringCallback;
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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Author:  nick
 * Email:   nickdevp@gmail.com
 * Date:    15/12/10 16:06.
 * Description:
 */
public class LoginActivity extends BaseSwipeBackActivity implements View.OnClickListener{

    private static final String TAG = "LoginActivity";

    @InjectView(R.id.btn_register)
    Button registerBtn;

    @InjectView(R.id.btn_login)
    Button loginBtn;

    @InjectView(R.id.tv_forgot_psw)
    TextView tvForgotPsw;

    @InjectView(R.id.edt_user)
    ClearEditText edtUser;

    @InjectView(R.id.edt_password)
    ClearEditText edtPassword;

    private String userName,password;
    private static final int REQUEST_CODE = 0x10;


    @Override
    protected boolean isApplyKitKatTranslucency() {
        return true;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_login;
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

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    //   | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

            // window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            //window.setNavigationBarColor(getResources().getColor(R.color.));
        }

    }

    private void doLogin(){
        userName = edtUser.getText().toString();
        password = edtPassword.getText().toString();

        if (CommonUtils.isEmpty(userName)){
            UIHelper.showToast(LoginActivity.this, "用户名不能为空");
            return;
        }

        if (CommonUtils.isEmpty(password)){
            UIHelper.showToast(LoginActivity.this,"密码不能为空");
            return;
        }

        String url = ServerApiConstants.Urls.LOGIN_IN_URLS;
        OkHttpUtils
                .post()
                .url(url)
               // .content(new Gson().toJson(new User(userName, password)))
                .addParams("phoneNumber",userName)
                .addParams("password",password)
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
            UIHelper.showToast(LoginActivity.this, "登陆失败");
        }

        @Override
        public void onResponse(String response) {

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
                    editor.putString("phoneNumber", userName);
                    editor.putString("customerImg",data.optString("headUrl"));
                    editor.commit();

                    UIHelper.showToast(LoginActivity.this, message);
                    finish();
                    setResult(Activity.RESULT_OK);
                    sendBroadcast(new Intent(Constants.INTENT_ACTION_USER_CHANGE));
                }else {
                    UIHelper.showToast(LoginActivity.this,message);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void inProgress(float progress)
        {
            Log.e(TAG, "inProgress:" + progress);
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
    @OnClick({R.id.btn_login,R.id.tv_forgot_psw,R.id.btn_register})
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btn_register:
               // readyGo(RegisterActivity.class);
                readyGoForResult(RegisterActivity.class,REQUEST_CODE);
                break;
            case R.id.btn_login:
                doLogin();
                break;
            case R.id.tv_forgot_psw:
                readyGo(ForgotPasswordActivity.class);
                break;
            default:
                break;

        }
    }

    protected void onActivityResult( int requestCode, int resultCode, Intent data) {

        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (resultCode != Activity.RESULT_OK)
                return;

            if(requestCode ==REQUEST_CODE) {
                finish();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }


    public  String sHA1(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);

            byte[] cert = info.signatures[0].toByteArray();

            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(cert);
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < publicKey.length; i++) {
                String appendString = Integer.toHexString(0xFF & publicKey[i])
                        .toUpperCase(Locale.US);
                if (appendString.length() == 1)
                    hexString.append("0");
                hexString.append(appendString);
                hexString.append(":");
            }
            showToast(hexString.toString());
            return hexString.toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
