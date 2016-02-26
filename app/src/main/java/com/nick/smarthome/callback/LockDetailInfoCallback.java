package com.nick.smarthome.callback;

import com.android.okhttp.callback.Callback;
import com.google.gson.Gson;
import com.nick.smarthome.bean.LockDetailInfoResult;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Author:  nick
 * Email:   nickdevp@gmail.com
 * Date:    15/12/9 14:13.
 * Description:
 */
public abstract class LockDetailInfoCallback extends Callback<LockDetailInfoResult> {

    @Override
    public LockDetailInfoResult parseNetworkResponse(Response response) throws IOException
    {
        String string = response.body().string();
        LockDetailInfoResult lockDetailInfoResult = new Gson().fromJson(string, LockDetailInfoResult.class);
        return lockDetailInfoResult;
    }
}
