package com.nick.smarthome.callback;

import com.android.okhttp.callback.Callback;
import com.google.gson.Gson;
import com.nick.smarthome.bean.ImagePathResult;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Author:  nick
 * Email:   nickdevp@gmail.com
 * Date:    15/12/9 14:13.
 * Description:
 */
public abstract class UploadLockImgCallback extends Callback<ImagePathResult> {

    @Override
    public ImagePathResult parseNetworkResponse(Response response) throws IOException
    {
        String string = response.body().string();
        ImagePathResult imagePathResult = new Gson().fromJson(string, ImagePathResult.class);
        return imagePathResult;
    }
}
