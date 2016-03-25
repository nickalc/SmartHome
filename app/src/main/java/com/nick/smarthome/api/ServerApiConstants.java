package com.nick.smarthome.api;

import android.os.Environment;

/**
 * Author:  nick
 * Email:   nickdevp@gmail.com
 * Date:    15/12/8 00:15.
 * Description:
 */
public class ServerApiConstants {

    /**
     *
     */
    public static final class Urls {

      
    }

    public static final class Paths {
        public static final String BASE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
        public static final String IMAGE_LOADER_CACHE_PATH = "/SmartHome/Images/";
    }
}
