package com.nick.smarthome.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.okhttp.OkHttpUtils;
import com.android.okhttp.callback.StringCallback;
import com.github.obsessive.library.eventbus.EventCenter;
import com.github.obsessive.library.netstatus.NetUtils;
import com.github.obsessive.library.utils.CommonUtils;
import com.nick.smarthome.R;
import com.nick.smarthome.api.ServerApiConstants;
import com.nick.smarthome.bean.CommResult;
import com.nick.smarthome.callback.CommonCallback;
import com.nick.smarthome.common.Constants;
import com.nick.smarthome.ui.base.BaseSwipeBackActivity;
import com.nick.smarthome.utils.DialogHelp;
import com.nick.smarthome.utils.FileUtil;
import com.nick.smarthome.utils.ImageUtils;
import com.nick.smarthome.utils.UIHelper;
import com.nick.smarthome.widgets.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.okhttp.Request;
//import com.ta.utdid2.android.utils.StringUtils;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Author:  nick
 * Email:   nickdevp@gmail.com
 * Date:    15/12/10 23:27.
 * Description:
 */
public class PersonalInformationActivity extends BaseSwipeBackActivity implements View.OnClickListener,
        DatePickerDialog.OnDateSetListener{

    private static final String TAG = "PersonalInformationActivity";
    public static final int ACTION_TYPE_ALBUM = 0;
    public static final int ACTION_TYPE_PHOTO = 1;

    private final static int CROP = 500;

    private final static String FILE_SAVEPATH = Environment
            .getExternalStorageDirectory().getAbsolutePath()
            + "/SmartHome/Portrait/";
    private Uri origUri;
    private Uri cropUri;
    private File protraitFile;
    private Bitmap protraitBitmap;
    private String protraitPath;
    private String theLarge;

    private ProgressDialog waitDialog ;

    private AlertDialog alertDialog;

    @InjectView(R.id.iv_avatar)
    CircleImageView ivAvatar;

    @InjectView(R.id.rl_sex)
    RelativeLayout rlSex;

    @InjectView(R.id.tv_nickname)
    EditText tvNickname;

    @InjectView(R.id.tv_sex)
    TextView tvSex;

    @InjectView(R.id.tv_name)
    TextView userName;

    @InjectView(R.id.rl_birthday)
    RelativeLayout rlBirthday;

    @InjectView(R.id.tv_birthday)
    TextView tvBirthday;

    @InjectView(R.id.rl_addr)
    LinearLayout rlAddr;

    @InjectView(R.id.tv_addr)
    EditText tvAddr;

    @InjectView(R.id.tv_interest)
    EditText tvInterst;

    @InjectView(R.id.btn_update_userinfo)
    Button updateUserBtn;

    private String mUsername,mCustomerId;

    @Override
    protected int getActionBarTitle(){
        return R.string.personal_info;
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
        return R.layout.activity_personal_information;
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

        SharedPreferences settings = getSharedPreferences("secrecy", Activity.MODE_PRIVATE);
        mUsername = settings.getString("phoneNumber", null);
        mCustomerId = settings.getString("customerId",null);

        userName.setText(mUsername);
        tvSex.setText("男");

        getUserInfo();
    }

    private void getUserInfo(){
        String url = ServerApiConstants.Urls.GET_CUSTOMER_INFO_URLS;
        OkHttpUtils
                .get()
                .url(url)
                .addParams("customerId",mCustomerId)
                .build()
                .execute(new UserCallback());

    }

    public class UserCallback extends StringCallback
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
            UIHelper.showToast(PersonalInformationActivity.this, "获取用户信息失败");
        }

        @Override
        public void onResponse(String response){

            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(response);
                String statusCode = jsonObject.optString("statuscode");
                String message = jsonObject.optString("message");
                JSONObject data = jsonObject.getJSONObject("data");
                if (statusCode.equals("1")) {
                    String sex = data.optString("sex");
                    String headUrl = data.optString("HEAD_URL");
                    if (sex.equals("0")) {
                        tvSex.setText("女");
                    }else {
                        tvSex.setText("男");
                    }
                    if (CommonUtils.isEmpty(data.optString("customer_name"))) {
                        userName.setText(mUsername);
                    }else {
                        userName.setText(data.optString("customer_name"));
                    }

                    tvNickname.setText(data.optString("nick_name") == "null"?"":data.optString("nick_name"));
                    tvBirthday.setText(data.optString("birth_date")== "null"?"":data.optString("birth_date"));
                    tvAddr.setText(data.optString("address")== "null"?"":data.optString("address"));
                    tvInterst.setText(data.optString("interset")== "null"?"":data.optString("interset"));
                    if (!CommonUtils.isEmpty(headUrl)) {
                        ImageLoader.getInstance().displayImage(headUrl,ivAvatar);
                    }

                    //保存账号密码到缓存sharedpreferences
                    SharedPreferences sharedPreferences = getSharedPreferences(
                            "secrecy", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("custName", data.optString("customer_name") == "null"?"":data.optString("customer_name"));
                    editor.putString("nickname", data.optString("nick_name") == "null"?"":data.optString("nick_name"));
                    editor.putString("birthData",data.optString("birth_date")== "null"?"":data.optString("birth_date"));
                    editor.putString("customerImg",headUrl);
                    editor.putString("sex",sex =="0"?"女":"男");
                    editor.commit();

                    sendBroadcast(new Intent(Constants.INTENT_ACTION_USER_CHANGE));

                }else {
                    UIHelper.showToast(PersonalInformationActivity.this, message);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void inProgress(float progress){
            Log.e(TAG,"inProgress:" + progress);
        }
    }

    private void updateUserInfo(){

        Map<String, String> params = new HashMap<String, String>();
        params.put("nick_name", tvNickname.getText().toString());
        params.put("customer_name", tvNickname.getText().toString());
        params.put("birth_date", tvBirthday.getText().toString());
        params.put("address", tvAddr.getText().toString());
        params.put("sex", tvSex.getText().toString() =="女"?"0":"1");
        params.put("interset", tvInterst.getText().toString());
        params.put("customerId", mCustomerId);

        String url = ServerApiConstants.Urls.ADD_CUSTOMER_INFO_URLS;

        OkHttpUtils
                .get()
                .url(url)
                .params(params)
                .build()
                .execute(new CommonCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        UIHelper.showToast(PersonalInformationActivity.this, "客户信息修改失败");
                    }

                    @Override
                    public void onResponse(CommResult response) {

                        if (response.statuscode.equals("1")) {
                            getUserInfo();
                            UIHelper.showToast(PersonalInformationActivity.this, response.message);

                        } else {
                            UIHelper.showToast(PersonalInformationActivity.this, response.message);
                        }

                    }
                });

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
    @OnClick({R.id.iv_avatar,R.id.rl_sex,R.id.rl_addr,R.id.rl_birthday,R.id.btn_update_userinfo})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_avatar:
                handleSelectPicture();
                break;
            case R.id.rl_sex:
                handleSelectSex();
                break;
            case R.id.rl_addr:
                break;
            case R.id.rl_birthday:
                showChooseDate();
                break;
            case R.id.btn_update_userinfo:
                updateUserInfo();
                break;
            default:
                break;
        }
    }

    private void handleSelectPicture() {
        DialogHelp.getSelectDialog(this, "选择图片", getResources().getStringArray(R.array.choose_picture), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                goToSelectPicture(i);
            }
        }).show();
    }

    private void handleSelectSex() {
         alertDialog =  DialogHelp.getSingleChoiceDialog(this, "选择性别", getResources().getStringArray(R.array.choose_sex), 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                goToSelectSex(i);
                alertDialog.hide();
            }
        }).show();
    }

    private void showChooseDate(){
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                PersonalInformationActivity.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setThemeDark(false);
        dpd.vibrate(false);
        dpd.dismissOnPause(false);
        dpd.showYearPickerFirst(true);
        dpd.setAccentColor(Color.parseColor("#54ab99"));
        dpd.setTitle("请选择生日日期");

        dpd.show(getFragmentManager(), "Datepickerdialog");
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = year+"-"+(++monthOfYear)+"-"+dayOfMonth;
        tvBirthday.setText(date);
    }

    private void goToSelectPicture(int position) {
        switch (position) {
            case ACTION_TYPE_ALBUM:
                startImagePick();
                break;
            case ACTION_TYPE_PHOTO:
                startTakePhoto();
                break;
            default:
                break;
        }
    }

    private void goToSelectSex(int position) {
        switch (position) {
            case ACTION_TYPE_ALBUM:
                tvSex.setText("男");
                break;
            case ACTION_TYPE_PHOTO:
                tvSex.setText("女");
                break;
            default:
                break;
        }
    }

    /**
     * 选择图片裁剪
     */
    private void startImagePick() {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "选择图片"),
                    ImageUtils.REQUEST_CODE_GETIMAGE_BYCROP);
        } else {
            intent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "选择图片"),
                    ImageUtils.REQUEST_CODE_GETIMAGE_BYCROP);
        }
    }

    private void startTakePhoto() {
        Intent intent;
        // 判断是否挂载了SD卡
        String savePath = "";
        String storageState = Environment.getExternalStorageState();
        if (storageState.equals(Environment.MEDIA_MOUNTED)) {
            savePath = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/oschina/Camera/";
            File savedir = new File(savePath);
            if (!savedir.exists()) {
                savedir.mkdirs();
            }
        }

        // 没有挂载SD卡，无法保存文件
        if (CommonUtils.isEmpty(savePath)) {
            showToast("无法保存照片，请检查SD卡是否挂载");
            return;
        }

        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss")
                .format(new Date());
        String fileName = "osc_" + timeStamp + ".jpg";// 照片命名
        File out = new File(savePath, fileName);
        Uri uri = Uri.fromFile(out);
        origUri = uri;

        theLarge = savePath + fileName;// 该照片的绝对路径

        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent,
                ImageUtils.REQUEST_CODE_GETIMAGE_BYCAMERA);
    }

    // 裁剪头像的绝对路径
    private Uri getUploadTempFile(Uri uri) {
        String storageState = Environment.getExternalStorageState();
        if (storageState.equals(Environment.MEDIA_MOUNTED)) {
            File savedir = new File(FILE_SAVEPATH);
            if (!savedir.exists()) {
                savedir.mkdirs();
            }
        } else {
            showToast("无法保存上传的头像，请检查SD卡是否挂载");
            return null;
        }
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss")
                .format(new Date());
        String thePath = ImageUtils.getAbsolutePathFromNoStandardUri(uri);

        // 如果是标准Uri
        if (CommonUtils.isEmpty(thePath)) {
            thePath = ImageUtils.getAbsoluteImagePath(this, uri);
        }
        String ext = FileUtil.getFileFormat(thePath);
        ext = CommonUtils.isEmpty(ext) ? "jpg" : ext;
        // 照片命名
        String cropFileName = "smart_crop_" + timeStamp + "." + ext;
        // 裁剪头像的绝对路径
        protraitPath = FILE_SAVEPATH + cropFileName;
        protraitFile = new File(protraitPath);

        cropUri = Uri.fromFile(protraitFile);
        return this.cropUri;
    }

    /**
     * 拍照后裁剪
     *
     * @param data 原始图片
     */
    private void startActionCrop(Uri data) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(data, "image/*");
        intent.putExtra("output", this.getUploadTempFile(data));
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);// 裁剪框比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", CROP);// 输出图片大小
        intent.putExtra("outputY", CROP);
        intent.putExtra("scale", true);// 去黑边
        intent.putExtra("scaleUpIfNeeded", true);// 去黑边
        startActivityForResult(intent,
                ImageUtils.REQUEST_CODE_GETIMAGE_BYSDCARD);
    }


    /**
     * 上传新照片
     */
    private void uploadNewPhoto() {
        waitDialog =  DialogHelp.getWaitDialog(this,"正在上传头像...");

        // 获取头像缩略图
        if (!CommonUtils.isEmpty(protraitPath) && protraitFile.exists()) {
            protraitBitmap = ImageUtils
                    .loadImgThumbnail(protraitPath, 200, 200);
        } else {
            showToast("图像不存在，上传失败");
        }
        if (protraitBitmap != null) {

            Map<String, String> params = new HashMap<>();
            params.put("customerId", mCustomerId);

            String url = ServerApiConstants.Urls.SUBMIT_CUSTOMER_PIC_URLS;
            OkHttpUtils
                    .post()
                    .addFile("mFile", "messenger_01.png", protraitFile)
                    .url(url)
                    .params(params)
                    .build()
                    .execute(new MyStringCallback());
        }
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
            waitDialog.hide();
            UIHelper.showToast(PersonalInformationActivity.this, "更换头像失败");
        }

        @Override
        public void onResponse(String response) {

            try {
                waitDialog.hide();
                JSONObject jsonObject = new JSONObject(response);

                String statusCode = jsonObject.optString("statuscode");
                String message = jsonObject.optString("message");
                JSONObject data = jsonObject.getJSONObject("data");
                if (statusCode.equals("1")) {
                    ivAvatar.setImageBitmap(protraitBitmap);
                    //保存账号密码到缓存sharedpreferences
                    SharedPreferences sharedPreferences = getSharedPreferences(
                            "secrecy", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("customerImg", data.optString("photoUrl"));
                    editor.commit();

                    UIHelper.showToast(PersonalInformationActivity.this, message);
                    sendBroadcast(new Intent(Constants.INTENT_ACTION_USER_CHANGE));
                }else {
                    UIHelper.showToast(PersonalInformationActivity.this,message);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void inProgress(float progress)
        {
            waitDialog.show();
            Log.e(TAG, "inProgress:" + progress);
        }
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode,
                                 final Intent imageReturnIntent) {
        if (resultCode != Activity.RESULT_OK)
            return;

        switch (requestCode) {
            case ImageUtils.REQUEST_CODE_GETIMAGE_BYCAMERA:
                startActionCrop(origUri);// 拍照后裁剪
                break;
            case ImageUtils.REQUEST_CODE_GETIMAGE_BYCROP:
                startActionCrop(imageReturnIntent.getData());// 选图后裁剪
                break;
            case ImageUtils.REQUEST_CODE_GETIMAGE_BYSDCARD:
                uploadNewPhoto();
                break;
        }
    }

}
