package com.ruiduoyi.skwmes;

/**
 * Created by Chen on 2018/4/24.
 */

public class Config {
    //public static final String BASE_URL = "http://192.168.142.1:8082/Ruiduoyi/";
    public static final String BASE_URL = "http://192.168.4.249:8888/RdyWebService.asmx/";
    public static final String IS_STOP_1 = "1";
    public static final String IS_STOP_0 = "0";
    public static final long REQUEST_TIME = 5000L;
    //初始密码
    public static final String PWD = "123456";
    //工作模式
    public static final String GZMS_SYSTEM_CONTROL = "系统监控";
    public static final String GZMS_SGFX = "手工放行";
    public static final String GZMS_ZTYX = "暂停运行";
    //一次gpio信号，高低电平的间隔时间
    public static final long TIME = 100L;
}
