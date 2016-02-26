package com.nick.smarthome.callback;

import com.android.okhttp.callback.Callback;
import com.google.gson.Gson;
import com.nick.smarthome.bean.TradeListResult;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Author:  nick
 * Email:   nickdevp@gmail.com
 * Date:    15/12/9 14:13.
 * Description:
 */
public abstract class OrderInfoListCallback extends Callback<TradeListResult> {

    @Override
    public TradeListResult parseNetworkResponse(Response response) throws IOException
    {
        String string = response.body().string();
        TradeListResult tradeListResult = new Gson().fromJson(string, TradeListResult.class);
        return tradeListResult;
    }
}
