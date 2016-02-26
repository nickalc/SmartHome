package com.nick.smarthome.callback;

import com.android.okhttp.callback.Callback;
import com.google.gson.Gson;
import com.nick.smarthome.bean.MyLockListResult;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Author:  nick
 * Email:   nickdevp@gmail.com
 * Date:    15/12/9 14:13.
 * Description:
 */
public abstract class MyLockListCallback extends Callback<MyLockListResult> {

    @Override
    public MyLockListResult parseNetworkResponse(Response response) throws IOException
    {
        String string = response.body().string();
        MyLockListResult myLockListResult = new Gson().fromJson(string, MyLockListResult.class);
        return myLockListResult;
    }
}
