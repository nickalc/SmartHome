package com.android.okhttp.callback;

import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by nick on 15/12/14.
 */
public abstract class StringCallback extends Callback<String>
{
    @Override
    public String parseNetworkResponse(Response response) throws IOException
    {
        return response.body().string();
    }

}
