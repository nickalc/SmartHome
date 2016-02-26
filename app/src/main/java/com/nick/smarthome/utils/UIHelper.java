package com.nick.smarthome.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.Toast;

import com.github.obsessive.library.utils.CommonUtils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.nick.smarthome.SmartHomeApplication;
import com.nick.smarthome.ui.activity.LoginActivity;

import java.util.Hashtable;

/**
 * Author:  nick
 * Email:   nickdevp@gmail.com
 * Date:    15/12/14 17:27.
 * Description:
 */
public class UIHelper {


    /**
     * show toast
     *
     * @param msg
     */
    public static void showToast(Context context,String msg) {
        if (null != msg && !CommonUtils.isEmpty(msg)) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 判断用户是否已登录
     * @param context
     * @return
     */
    public static boolean isLogin(Context context){
        SharedPreferences settings = context.getSharedPreferences("secrecy", Activity.MODE_PRIVATE);
        String mCustomerId = settings.getString("customerId", null);

        if (CommonUtils.isEmpty(mCustomerId)) {
            return false;
        }else{
            return true;
        }
    }


    /**
     * 判断当前版本是否兼容目标版本的方法
     *
     * @param VersionCode
     * @return
     */
    public static boolean isMethodsCompat(int VersionCode) {
        int currentVersion = android.os.Build.VERSION.SDK_INT;
        return currentVersion >= VersionCode;
    }

    /**
     * 显示登录
     * @param context
     */
    public static void showLogin(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    /**
     * 打开系统中的浏览器
     *
     * @param context
     * @param url
     */
    public static void openSysBrowser(Context context, String url) {
        try {
            Uri uri = Uri.parse(url);
            Intent it = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(it);
        } catch (Exception e) {
            e.printStackTrace();
            SmartHomeApplication.showToastShort("无法浏览此网页");
        }
    }


    public static Bitmap createQRImage(String str) {
        Bitmap  bitmap = null;
        try {
            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            // 图像数据转换，使用了矩阵转换
            BitMatrix bitMatrix = new QRCodeWriter().encode(str,
                    BarcodeFormat.QR_CODE, 700, 700, hints);

            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();

            int[] pixels = new int[width * height];
            // 下面这里按照二维码的算法，逐个生成二维码的图片，
            // 两个for循环是图片横列扫描的结果
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * width + x] = 0xff000000;
                    } else {
                        pixels[y * width + x] = 0xffffffff;
                    }
                }
            }
            // 生成二维码图片的格式，使用ARGB_8888
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            // 先判断是否已经回收

            if(bitmap != null && !bitmap.isRecycled()){
                // 回收并且置为null
                bitmap.recycle();
                bitmap = null;
            }
            System.gc();
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return bitmap;
    }


    /**
     * 调用系统安装了的应用分享
     *
     * @param context
     * @param title
     * @param url
     */
    public static void showSystemShareOption(Activity context,
                                             final String title, final String url) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "分享：" + title);
        intent.putExtra(Intent.EXTRA_TEXT, title + " " + url);
        context.startActivity(Intent.createChooser(intent, "选择分享"));
    }

}
