package com.nick.smarthome.callback;

import com.android.okhttp.callback.Callback;
import com.google.gson.Gson;
import com.nick.smarthome.bean.HouseResourceDeatailResult;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Author:  nick
 * Email:   nickdevp@gmail.com
 * Date:    15/12/9 14:13.
 * Description:
 */
public abstract class HouseResourceDetailCallback extends Callback<HouseResourceDeatailResult> {

    @Override
    public HouseResourceDeatailResult parseNetworkResponse(Response response) throws IOException
    {
        String string = response.body().string();
        HouseResourceDeatailResult houseResourceDeatailResult = new Gson().fromJson(string, HouseResourceDeatailResult.class);
        return houseResourceDeatailResult;
    }
}
