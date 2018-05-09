package com.ruiduoyi.skwmes.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Chen on 2018/4/24.
 */
public class PreferencesUtil {
    public static final String PWD = "pwd";
    public static final String SYB = "syb";
    public static final String XT = "xt";
    public static final String GZ = "gz";
    private static final String SHOUDONG_3 = "shoudong3";
    private static final String SHOUDONG_4 = "shoudong4";
    private Context context;
    public PreferencesUtil(Context context) {
        this.context = context;
    }

    public void savePwd(String pwd) {
        SharedPreferences preferences = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PWD, pwd);
        editor.commit();
    }

    public String getPwd(){
        SharedPreferences preferences = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        return preferences.getString(PWD, "");
    }

    public void setSybXtGz(String sybStr, String xtStr, String gzStr) {
        SharedPreferences preferences = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SYB, sybStr);
        editor.putString(XT, xtStr);
        editor.putString(GZ, gzStr);
        editor.commit();
    }
    public Map<String,String> getSybXtGz(){
        Map<String,String> map = new HashMap<String,String>();
        SharedPreferences preferences = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        map.put(SYB,preferences.getString(SYB, ""));
        map.put(XT,preferences.getString(XT, ""));
        map.put(GZ,preferences.getString(GZ, ""));
        if ("".equals(map.get(SYB))){
            map = null;
        }
        return map;
    }

    /**
     * 设置gpio3手动设置状态（手动开始，手动暂停）
     * 如果是手动开始了，当接收到服务器启动命令时，无论什么情况都要保持启动（包括重启）
     * 如果是手动暂停，无论接收到服务器是启动还是暂停命令，无论什么情况都要保持暂停（包括重启）
     */
    public void setShouDong3(boolean shouDong){
        SharedPreferences preferences = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        preferences.edit().putBoolean(SHOUDONG_3,shouDong).commit();
    }
    public boolean getShouDong3(){
        SharedPreferences preferences = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        return preferences.getBoolean(SHOUDONG_3,false);
    }
    /**
     * 设置gpio4手动设置状态（手动开始，手动暂停）
     * 如果是手动开始了，当接收到服务器启动命令时，无论什么情况都要保持启动（包括重启）
     * 如果是手动暂停，无论接收到服务器是启动还是暂停命令，无论什么情况都要保持暂停（包括重启）
     */
    public void setShouDong4(boolean shouDong){
        SharedPreferences preferences = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        preferences.edit().putBoolean(SHOUDONG_4,shouDong).commit();
    }
    public boolean getShouDong4(){
        SharedPreferences preferences = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        return preferences.getBoolean(SHOUDONG_4,false);
    }
}
