package com.ruiduoyi.skwmes.util;

import android.content.Context;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Chen on 2018/5/9.
 */

public class Util {
    public static void setSystemTime(final Context cxt, String datetimes) {
        // yyyyMMdd.HHmmss】
        /**
         * 可用busybox 修改时间
         */
        /*
         * String
         * cmd="busybox date  \""+bt_date1.getText().toString()+" "+bt_time1
         * .getText().toString()+"\""; String cmd2="busybox hwclock  -w";
         */
        try {
            Process process = Runtime.getRuntime().exec("su");
//          String datetime = "20131023.112800"; // 测试的设置的时间【时间格式
            String datetime = ""; // 测试的设置的时间【时间格式
            datetime = datetimes.toString(); // yyyyMMdd.HHmmss】
            DataOutputStream os = new DataOutputStream(
                    process.getOutputStream());
            os.writeBytes("setprop persist.sys.timezone GMT\n");
            os.writeBytes("/system/bin/date -s "+datetimes+"\n");
            os.writeBytes("clock -w\n");
            os.writeBytes("exit\n");
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
            //Toast.makeText(cxt, "请获取Root权限", Toast.LENGTH_SHORT).show();
        }
    }
}
