package com.nick.smarthome.eventbus;

/**
 * Author:  nick
 * Email:   nickdevp@gmail.com
 * Date:    15/12/25 09:52.
 * Description:
 */
public class MyEvent {

    private String mMsg;
    public MyEvent(String msg) {
        // TODO Auto-generated constructor stub
        mMsg = msg;
    }
    public String getMsg(){
        return mMsg;
    }
}
