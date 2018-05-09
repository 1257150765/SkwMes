package com.ruiduoyi.skwmes.util;

import android.util.Log;

/**
 * Created by Chen on 2018/4/23.
 */

public class LogWraper {
    private static boolean isDebug = true;
    public static void d(String tag, String msg){
        if (isDebug) {
            Log.d(tag, msg);
        }
    }
    public static void e(String tag, String msg){
        if (isDebug) {
            Log.e(tag, msg);
        }
    }
    public static void i(String tag, String msg){
        if (isDebug) {
            Log.i(tag, msg);
        }
    }
}
