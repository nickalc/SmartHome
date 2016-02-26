package com.nick.smarthome.callback;

import com.android.okhttp.callback.Callback;
import com.google.gson.Gson;
import com.nick.smarthome.bean.CommResult;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Author:  nick
 * Email:   nickdevp@gmail.com
 * Date:    15/12/9 14:13.
 * Description:
 */
public abstract class CommonCallback extends Callback<CommResult> {

    @Override
    public CommResult parseNetworkResponse(Response response) throws IOException
    {
        String string = response.body().string();
        CommResult commResult = new Gson().fromJson(string, CommResult.class);
        return commResult;
    }
}
