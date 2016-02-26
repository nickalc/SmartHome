package com.nick.smarthome.bean;

/**
 * Author:  nick
 * Email:   nickdevp@gmail.com
 * Date:    15/12/22 13:20.
 * Description:
 */
public class CommResult {
    public String statuscode ;
    public String message  ;

    public CommResult() {
        // TODO Auto-generated constructor stub
    }

    public CommResult(String statuscode, String message) {
        this.statuscode = statuscode;
        this.message = message;
    }

    @Override
    public String toString()
    {
        return "CommResult{" +
                "statuscode='" + statuscode + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
