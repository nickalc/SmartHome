package com.nick.smarthome.callback;

import com.android.okhttp.callback.Callback;
import com.google.gson.Gson;
import com.nick.smarthome.bean.MyHouseInfoResult;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Author:  nick
 * Email:   nickdevp@gmail.com
 * Date:    16/01/9 14:13.
 * Description:
 */
public abstract class MyHouseInfoCallback extends Callback<MyHouseInfoResult> {

    @Override
    public MyHouseInfoResult parseNetworkResponse(Response response) throws IOException
    {
        String string = response.body().string();
        MyHouseInfoResult myHouseInfoResult = new Gson().fromJson(string, MyHouseInfoResult.class);
        return myHouseInfoResult;
    }
}
