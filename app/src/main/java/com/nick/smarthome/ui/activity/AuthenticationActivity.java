package com.nick.smarthome.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.okhttp.OkHttpUtils;
import com.github.obsessive.library.eventbus.EventCenter;
import com.github.obsessive.library.netstatus.NetUtils;
import com.github.obsessive.library.utils.CommonUtils;
import com.nick.smarthome.R;
import com.nick.smarthome.api.ServerApiConstants;
import com.nick.smarthome.bean.CommResult;
import com.nick.smarthome.callback.CommonCallback;
import com.nick.smarthome.ui.base.BaseSwipeBackActivity;
import com.nick.smarthome.utils.DialogHelp;
import com.nick.smarthome.utils.FileUtil;
import com.nick.smarthome.utils.ImageUtils;
import com.nick.smarthome.utils.UIHelper;
import com.squareup.okhttp.Request;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.InjectView;
import butterknife.OnClick;

//import com.ta.utdid2.android.utils.StringUtils;

/**
 * Author:  nick
 * Email:   nickdevp@gmail.com
 * Date:    15/12/22 18:01.
 * Description:用户身份验证
 */
public class AuthenticationActivity extends BaseSwipeBackActivity implements View.OnClickListener{

    private static final String TAG = "AuthenticationActivity";

    @InjectView(R.id.root_layout)
    LinearLayout rootLayout;

    @InjectView(R.id.iv_add_authentic)
    ImageView ivAddAuthentic;

    @InjectView(R.id.rl_img)
    RelativeLayout rlImg;

    @InjectView(R.id.iv_img)
    ImageView ivImg;

    @InjectView(R.id.iv_clear_img)
    ImageView ivClearImg;

    @InjectView(R.id.btn_upload_auth)
    TextView uploadAuthBtn;

    @InjectView(R.id.skip_btn)
    TextView skipBtn;

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
    private String theLarge,fileName,cropFileName;

    private ProgressDialog waitDialog ;

    @Override
    protected int getActionBarTitle() {
        return R.string.customer_auth;
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
        return R.layout.activity_authentication;
    }

    @Override
    protected void onEventComming(EventCenter eventCenter) {

    }

    @Override
    protected View getLoadingTargetView() {
        return rootLayout;
    }

    @Override
    protected void initViewsAndEvents() {

    }


    @Override
    @OnClick({R.id.iv_add_authentic,R.id.iv_clear_img,R.id.btn_upload_auth,R.id.skip_btn})
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.iv_add_authentic:
                handleSelectPicture();
                break;
            case R.id.iv_clear_img:
                rlImg.setVisibility(View.GONE);
                protraitFile.delete();
                protraitBitmap = null;
                break;
            case R.id.btn_upload_auth:
                uploadNewPhoto();
                break;
            case R.id.skip_btn:
                finish();
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
        fileName = "osc_" + timeStamp + ".jpg";// 照片命名
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
        cropFileName = "smart_crop_" + timeStamp + "." + ext;
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
     * 显示新照片
     */
    private void showNewPhoto() {

        // 获取头像缩略图
        if (!CommonUtils.isEmpty(protraitPath) && protraitFile.exists()) {
            protraitBitmap = ImageUtils
                    .loadImgThumbnail(protraitPath, 200, 200);
        } else {
            showToast("图像不存在，上传失败");
        }
        if (protraitBitmap != null) {

            ivImg.setImageBitmap(protraitBitmap);
            rlImg.setVisibility(View.VISIBLE);
//            Map<String, String> params = new HashMap<>();
//            params.put("customerId", mCustomerId);
//
//            String url = ServerApiConstants.Urls.SUBMIT_CUSTOMER_PIC_URLS;
//            OkHttpUtils
//                    .post()
//                    .addFile("mFile", "messenger_01.png", protraitFile)
//                    .url(url)
//                    .params(params)
//                    .build()
//                    .execute(new MyStringCallback());
        }
    }

    private void uploadNewPhoto(){
        waitDialog =  DialogHelp.getWaitDialog(this, "正在上传图片...");

        SharedPreferences settings = getSharedPreferences("secrecy", Activity.MODE_PRIVATE);
        String mCustomerId = settings.getString("customerId", "");

        // 获取头像缩略图
        if (!CommonUtils.isEmpty(protraitPath) && protraitFile.exists()) {
            protraitBitmap = ImageUtils
                    .loadImgThumbnail(protraitPath, 200, 200);
        } else {
            showToast("图像不存在，上传失败");
        }
        if (protraitBitmap != null) {

            waitDialog.show();

            Map<String, String> params = new HashMap<>();
            params.put("customerId", mCustomerId);

            String url = ServerApiConstants.Urls.UPLOAD_CUSTOMER_CARD_PIC_URLS;
            OkHttpUtils
                    .post()
                    .addFile("idCardImg", cropFileName, protraitFile)
                    .url(url)
                    .params(params)
                    .build()
                    .execute(new CommonCallback() {
                        @Override
                        public void onError(Request request, Exception e) {
                            waitDialog.hide();
                            UIHelper.showToast(AuthenticationActivity.this, "上传身份验证图片失败");
                        }

                        @Override
                        public void onResponse(CommResult response) {
                            UIHelper.showToast(AuthenticationActivity.this, response.message);
                            if (response.statuscode.equals("1")) {
                                finish();
                            }
                            waitDialog.hide();
                        }
                    });
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
                showNewPhoto();
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
