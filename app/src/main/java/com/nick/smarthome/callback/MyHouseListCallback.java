package com.nick.smarthome.callback;

import com.android.okhttp.callback.Callback;
import com.google.gson.Gson;
import com.nick.smarthome.bean.MyHouseListResult;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Author:  nick
 * Email:   nickdevp@gmail.com
 * Date:    15/12/9 14:13.
 * Description:
 */
public abstract class MyHouseListCallback extends Callback<MyHouseListResult> {

    @Override
    public MyHouseListResult parseNetworkResponse(Response response) throws IOException
    {
        String string = response.body().string();
        MyHouseListResult myHouseListResult = new Gson().fromJson(string, MyHouseListResult.class);
        return myHouseListResult;
    }
}
