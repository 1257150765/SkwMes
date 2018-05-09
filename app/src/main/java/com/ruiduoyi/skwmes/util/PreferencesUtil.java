package com.ruiduoyi.skwmes.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.ruiduoyi.skwmes.bean.GzBean;
import com.ruiduoyi.skwmes.bean.SystemBean;
import com.ruiduoyi.skwmes.bean.XbBean;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Chen on 2018/4/24.
 */
public class PreferencesUtil {
    public static final String PWD = "pwd";
    public static final String SYB_NAME = "sybName";
    public static final String SYB_SERVER = "sybServer";
    public static final String SYB_DATABASE = "sybDataBase";
    public static final String SYB_UID = "sybUid";
    public static final String SYB_PWD = "sybPwd";
    public static final String XT_NAME = "xtName";
    public static final String XT_CODE = "xtCode";
    public static final String GZ_NAME_3 = "gzName3";
    public static final String GZ_NAME_4 = "gzName4";
    public static final String GZ_Code_3 = "gzCode3";
    public static final String GZ_Code_4 = "gzCode4";
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

    public void setSybXtGz(SystemBean.UcDataBean sybStr, XbBean.UcDataBean xbBean, GzBean.UcDataBean gzBean1, GzBean.UcDataBean gzBean2) {
        SharedPreferences preferences = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SYB_NAME, sybStr.getPrj_name());
        editor.putString(SYB_SERVER, sybStr.getPrj_server());
        editor.putString(SYB_DATABASE, sybStr.getPrj_database());
        editor.putString(SYB_UID, sybStr.getPrj_uid());
        editor.putString(SYB_PWD, sybStr.getPrj_pwd());

        editor.putString(XT_NAME, xbBean.getV_xbname());
        editor.putString(XT_CODE, xbBean.getV_xbdm());

        editor.putString(GZ_Code_3, gzBean1.getV_gzdm());
        editor.putString(GZ_NAME_3, gzBean1.getV_gzname());
        editor.putString(GZ_Code_4, gzBean2.getV_gzdm());
        editor.putString(GZ_NAME_4, gzBean2.getV_gzname());

        editor.commit();
    }
    public Map<String,String> getSybXtGz(){
        Map<String,String> map = new HashMap<String,String>();
        SharedPreferences preferences = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        map.put(SYB_NAME,preferences.getString(SYB_NAME, ""));
        map.put(SYB_SERVER,preferences.getString(SYB_SERVER, ""));
        map.put(SYB_DATABASE,preferences.getString(SYB_DATABASE, ""));
        map.put(SYB_UID,preferences.getString(SYB_UID, ""));
        map.put(SYB_PWD,preferences.getString(SYB_PWD, ""));
        map.put(XT_NAME,preferences.getString(XT_NAME, ""));
        map.put(XT_CODE,preferences.getString(XT_CODE, ""));

        map.put(GZ_Code_3,preferences.getString(GZ_Code_3, ""));
        map.put(GZ_NAME_3,preferences.getString(GZ_NAME_3, ""));
        map.put(GZ_Code_4,preferences.getString(GZ_Code_4, ""));
        map.put(GZ_NAME_4,preferences.getString(GZ_NAME_4, ""));
        if ("".equals(map.get(SYB_NAME))){
            map = null;
        }
        return map;
    }

    /**
     * 设置gpio3手动设置状态（手动开始，手动暂停）
     * 如果是手动开始了，当接收到服务器启动命令时，无论什么情况都要保持启动（包括重启）
     * 如果是手动暂停，无论接收到服务器是启动还是暂停命令，无论什么情况都要保持暂停（包括重启）
     */
    public void setGZMS3(String gzms){
        SharedPreferences preferences = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        preferences.edit().putString(SHOUDONG_3,gzms).commit();
    }
    public String getGZMS3(){
        SharedPreferences preferences = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        return preferences.getString(SHOUDONG_3,"");
    }
    /**
     * 设置gpio4设置状态（手动开始，手动暂停）
     * 如果是手动开始了，当接收到服务器启动命令时，无论什么情况都要保持启动（包括重启）
     * 如果是手动暂停，无论接收到服务器是启动还是暂停命令，无论什么情况都要保持暂停（包括重启）
     */
    public void setGZMS4(String shouDong){
        SharedPreferences preferences = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        preferences.edit().putString(SHOUDONG_4,shouDong).commit();
    }
    public String getGZMS4(){
        SharedPreferences preferences = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        return preferences.getString(SHOUDONG_4,"");
    }
}
