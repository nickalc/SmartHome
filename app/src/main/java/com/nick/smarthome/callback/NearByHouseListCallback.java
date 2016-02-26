package com.nick.smarthome.callback;

import com.android.okhttp.callback.Callback;
import com.google.gson.Gson;
import com.nick.smarthome.bean.NearByHouseListResult;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Author:  nick
 * Email:   nickdevp@gmail.com
 * Date:    15/12/9 14:13.
 * Description:
 */
public abstract class NearByHouseListCallback extends Callback<NearByHouseListResult> {

    @Override
    public NearByHouseListResult parseNetworkResponse(Response response) throws IOException
    {
        String string = response.body().string();
        NearByHouseListResult nearByHouseListResult = new Gson().fromJson(string, NearByHouseListResult.class);
        return nearByHouseListResult;
    }
}
