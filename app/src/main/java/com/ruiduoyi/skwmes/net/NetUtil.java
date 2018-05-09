package com.ruiduoyi.skwmes.net;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.ruiduoyi.skwmes.util.LogWraper;

import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by Chen on 2018/4/23.
 */

public class NetUtil {
    private static final long SLEEP_TIME = 10000L;
    private static final int CONNECT_TIME_OUT = 2000;
    private static final String TAG = NetUtil.class.getSimpleName();
    private static int reTryTime = 3;
    private static boolean isRun = true;
    public static final String ACTION_TYPE_NET_ERROR = "ACTION_TYPE_NET_ERROR";
    public static final String ACTION_TYPE_NET_OK = "ACTION_TYPE_NET_OK";
    private static Intent intentError= new Intent(ACTION_TYPE_NET_ERROR);
    private static Intent intentOk = new Intent(ACTION_TYPE_NET_OK);
    private static Context mContext;
    private static BroadcastReceiver broadcastReceiver;

    /**
     * 初始化网络监测，程序退出时必须调用destroy方法
     * @param context
     * @param urlStr 服务器地址
     */
    public static void init(final Context context, final String urlStr){
        mContext = context;
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                ConnectivityManager connectivityManager=(ConnectivityManager)context.getSystemService(context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
                if(networkInfo!=null&&networkInfo.isAvailable()){
                } else{
                    context.sendBroadcast(intent);
                }
            }
        };
        context.registerReceiver(broadcastReceiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        //启动一个线程，不断ping服务器
        new Thread(new Runnable() {
            int time = 0;
            @Override
            public void run() {
                while (isRun){
                    try {
                        URL url = new URL(urlStr);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setConnectTimeout(CONNECT_TIME_OUT);
                        int responseCode = connection.getResponseCode();
                        if (responseCode != 200){
                            time++;
                            if (time < reTryTime){
                                //重试此时未达reTryTime次
                                LogWraper.d(TAG,"重试次数已达"+time+"次");
                                continue;
                            }else {
                                time = 0;
                                //重试此时已达reTryTime次，认为连接不上服务器
                                context.sendBroadcast(intentError);
                                Thread.sleep(SLEEP_TIME);
                                LogWraper.d(TAG,"重试次数已达3次，10s后再试");
                            }
                        }else {
                            LogWraper.d(TAG,"网络正常，10s后再试");
                            //网络连接正常，SLEEP_TIME毫秒后重新监测，
                            //网络正常时没有广播，？？？,注册广播过滤器弄错了
                            // TODO: 2018/4/23 切换网络时崩溃
                            context.sendBroadcast(intentOk);
                            Thread.sleep(SLEEP_TIME);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        try {
                            Thread.sleep(SLEEP_TIME);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                        context.sendBroadcast(intentError);
                    }

                }
            }
        }).start();
    }

    public static void destroy(){
        isRun = false;
        mContext.unregisterReceiver(broadcastReceiver);
        mContext = null;
    }

}
