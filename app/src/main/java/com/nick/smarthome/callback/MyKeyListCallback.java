package com.nick.smarthome.callback;

import com.android.okhttp.callback.Callback;
import com.google.gson.Gson;
import com.nick.smarthome.bean.MyKeyListResult;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Author:  nick
 * Email:   nickdevp@gmail.com
 * Date:    15/12/9 14:13.
 * Description:
 */
public abstract class MyKeyListCallback extends Callback<MyKeyListResult> {

    @Override
    public MyKeyListResult parseNetworkResponse(Response response) throws IOException
    {
        String string = response.body().string();
        MyKeyListResult myKeyListResult = new Gson().fromJson(string, MyKeyListResult.class);
        return myKeyListResult;
    }
}
