package com.ruiduoyi.skwmes.presenter;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;

import com.ruiduoyi.skwmes.Config;
import com.ruiduoyi.skwmes.bean.DateBean;
import com.ruiduoyi.skwmes.bean.GzBean;
import com.ruiduoyi.skwmes.bean.InfoBean;
import com.ruiduoyi.skwmes.bean.StopOrder;
import com.ruiduoyi.skwmes.bean.SystemBean;
import com.ruiduoyi.skwmes.bean.UpdateBean;
import com.ruiduoyi.skwmes.bean.XbBean;
import com.ruiduoyi.skwmes.contact.MainActivityContact;
import com.ruiduoyi.skwmes.net.RetrofitManager;
import com.ruiduoyi.skwmes.util.DownloadUtils;
import com.ruiduoyi.skwmes.util.GpioUtil;
import com.ruiduoyi.skwmes.util.LogWraper;
import com.ruiduoyi.skwmes.util.PreferencesUtil;
import com.ruiduoyi.skwmes.util.Util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by Chen on 2018/4/25.
 */

public class MainActivityPresenter implements MainActivityContact.Presenter, GpioUtil.OnGpioChangeListener {
    private static final String TAG = MainActivityPresenter.class.getSimpleName();
    //每隔多少毫秒发送一次gpio信息，（并同时检测服务器是否接通）
    private static final long SEND_GPIO_TIME = 5000L;
    //每隔多少毫秒请求一次服务器信息
    private static final long REQUEST_STOP_ORDER_TIME = 10000L;
    private MainActivityContact.View view;
    private Activity context;
    public static final String GPIO_INDEX_1 = "1";
    public static final String GPIO_INDEX_2 = "2";
    public static final String GPIO_INDEX_3 = "3";
    public static final String GPIO_INDEX_4 = "4";
    public static final String GPIO_INDEX = "gpioIndex";
    public static final int REQUEST_CODE_CHANGEGPIO = 1001;
    public static final int REQUEST_CODE_CHANGSYBXTGZ = 1002;
    private GpioUtil gpioUtil3;
    private GpioUtil gpioUtil4;
    Timer timer = new Timer();
    Timer timer2 = new Timer();
    Timer dateTimer = new Timer();
    private boolean isConnect = false;

    private String gzms3 = "";
    private String gzms4 = "";
    private String gzxx3 = "";
    private String gzxx4 = "";
    private PreferencesUtil preferencesUtil;
    private boolean isFirstTime = true;
    private InfoBean value;


    public MainActivityPresenter(MainActivityContact.View view, Activity context) {
        this.view = view;
        this.context = context;
        preferencesUtil = new PreferencesUtil(context);
        gzms3 = preferencesUtil.getGZMS3();
        gzms4 = preferencesUtil.getGZMS4();
        gzxx3 = preferencesUtil.getSybXtGz().get(PreferencesUtil.GZ_Code_3);
        gzxx4 = preferencesUtil.getSybXtGz().get(PreferencesUtil.GZ_Code_4);
        view.onGpioGzmsChange(gzms3,gzms4);
        init();
    }

    @Override
    public void init() {
        //RetrofitManager.init();
        Map<String, String> sybXtGz = preferencesUtil.getSybXtGz();
        if (null == sybXtGz){
            loadSyb();
        }else {
            view.setSybXt(sybXtGz.get(PreferencesUtil.SYB_NAME),sybXtGz.get(PreferencesUtil.XT_NAME));
            view.setGz(sybXtGz.get(PreferencesUtil.GZ_NAME_3), sybXtGz.get(PreferencesUtil.GZ_NAME_4));
        }
        gpioUtil3 = new GpioUtil(GPIO_INDEX_3, this);
        gpioUtil4 = new GpioUtil(GPIO_INDEX_4,this);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //轨道一
                LogWraper.d(TAG,"到五秒了，开始发送");
                switch (gzms3){
                    //系统控制
                    case Config.GZMS_SYSTEM_CONTROL:
                        if (null == value){
                            stopSend(GPIO_INDEX_3);
                            LogWraper.d(TAG,"没有接收到服务器命令");
                        }else {
                            for (InfoBean.UcDataBean bean:value.getUcData()){
                                //循环找到对应的工站
                                if (gzxx3.equals(bean.getErl_gzdm())){
                                    LogWraper.d(TAG,"接收到服务器命令,一轨指令--"+bean.isErl_signal());
                                    if (bean.isErl_signal()) {
                                        gpioUtil3.sendOne();
                                        startSend(GPIO_INDEX_3);
                                        LogWraper.d(TAG,"一轨发送一次信号");
                                    } else if (!bean.isErl_signal()||!isConnect) {
                                        //stopSend(GPIO_INDEX_3);
                                        //当接收到暂停命令时，不做处理即可，
                                        stopSend(GPIO_INDEX_3);
                                    }
                                }
                            }


                        }
                        break;
                    //手工放行
                    case Config.GZMS_SGFX:
                        LogWraper.d(TAG,"一轨发送一次信号");
                        gpioUtil3.sendOne();
                        startSend(GPIO_INDEX_3);

                        break;
                    //暂停运行
                    case Config.GZMS_ZTYX:
                        stopSend(GPIO_INDEX_3);
                        break;
                    default:
                        break;
                }
                switch (gzms4){
                    //系统控制
                    case Config.GZMS_SYSTEM_CONTROL:
                        if (null == value){
                            LogWraper.d(TAG,"没有接收到服务器命令");
                            stopSend(GPIO_INDEX_4);
                        }else {
                            //轨道二
                            for (InfoBean.UcDataBean bean:value.getUcData()){
                                //循环找到对应的工站
                                if (gzxx4.equals(bean.getErl_gzdm())){
                                    LogWraper.d(TAG,"接收到服务器命令,二轨指令--"+bean.isErl_signal());
                                    if (bean.isErl_signal()) {
                                        gpioUtil4.sendOne();
                                        startSend(GPIO_INDEX_4);
                                        LogWraper.d(TAG,"二轨发送一次信号");
                                    } else if (!bean.isErl_signal()||!isConnect) {
                                        //stopSend(GPIO_INDEX_3);
                                        //当接收到暂停命令时，不做处理即可，
                                        stopSend(GPIO_INDEX_4);
                                    }
                                }
                            }
                        }
                        break;
                    //手工放行
                    case Config.GZMS_SGFX:
                        LogWraper.d(TAG,"二轨发送一次信号");
                        gpioUtil4.sendOne();
                        startSend(GPIO_INDEX_4);
                        break;
                    //暂停运行
                    case Config.GZMS_ZTYX:
                        stopSend(GPIO_INDEX_4);
                        break;
                    default:
                        break;
                }
                RetrofitManager.getDate().subscribe(new Observer<DateBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }
                    @Override
                    public void onNext(final DateBean value) {
                        if (context.isDestroyed()){
                            return;
                        }
                       if (isFirstTime) {
                            isFirstTime = false;
                           String time = value.getUcData().get(0)
                                   .getV_curdate().replaceAll("-", "")
                                   .replaceAll(" ", "\\.")
                                   .replaceAll(":", "");
                           LogWraper.d(TAG,"初始化APP，设置系统时间"+time);
                           Util.setSystemTime(context,time);
                           final SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd E");
                           dateTimer.schedule(new TimerTask() {
                               @Override
                               public void run() {
                                   final String date = format.format(new Date());
                                   context.runOnUiThread(new Runnable() {
                                       @Override
                                       public void run() {
                                           view.onDateUpdate(date);
                                       }
                                   });
                               }
                           },0,12*60*60*1000);

                       }
                        LogWraper.d(TAG,"检测与服务器的连接");
                        //每次有返回值表示与服务器有连接
                        isConnect = true;
                        view.onNetInfoChange(Config.IS_STOP_1);
                    }

                    @Override
                    public void onError(Throwable e) {
                        //e.printStackTrace();
                        //请求网络出错
                        isConnect = false;
                        view.onNetInfoChange(Config.IS_STOP_0);
                        MainActivityPresenter.this.value = null;
                        context.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                view.onStopSend(GPIO_INDEX_3);
                                view.onStopSend(GPIO_INDEX_4);
                            }
                        });
                    }
                    @Override
                    public void onComplete() {
                    }
                });
            }
        },0,SEND_GPIO_TIME);
        //此定时器每隔
        timer2.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!isConnect){
                    return;
                }
                Map<String, String> sybXtGz1 = preferencesUtil.getSybXtGz();

                RetrofitManager.getInfoBean(sybXtGz1.get(PreferencesUtil.SYB_SERVER),
                        sybXtGz1.get(PreferencesUtil.SYB_DATABASE),
                        sybXtGz1.get(PreferencesUtil.SYB_UID),
                        sybXtGz1.get(PreferencesUtil.SYB_PWD),
                        sybXtGz1.get(PreferencesUtil.XT_CODE),
                        sybXtGz1.get(PreferencesUtil.GZ_Code_3)+","+ sybXtGz1.get(PreferencesUtil.GZ_Code_4)
                        ).subscribe(new Observer<InfoBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }
                    @Override
                    public void onNext(final InfoBean value) {
                        if (context.isDestroyed()){
                            return;
                        }
                        LogWraper.d(TAG,"开始请求下一次发送命令");
                        //每次有返回值表示与服务器有连接
                        MainActivityPresenter.this.value = value;
                        isConnect = true;
                        view.onNetInfoChange(Config.IS_STOP_1);
                        InfoBean.UcDataBean info3 = null;
                        InfoBean.UcDataBean info4 = null;
                        for (InfoBean.UcDataBean bean:value.getUcData()){
                            if (gzxx3.equals(bean.getErl_gzdm())){
                                info3 = bean;
                            }
                            if (gzxx4.equals(bean.getErl_gzdm())){
                                info4 = bean;
                            }
                        }
                        view.onLoadInfoSucceed(info3,info4);
                    }

                    @Override
                    public void onError(Throwable e) {
                        //e.printStackTrace();
                        //请求网络出错
                        isConnect = false;
                        view.onNetInfoChange(Config.IS_STOP_0);
                        MainActivityPresenter.this.value = null;
                        context.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                view.onStopSend(GPIO_INDEX_3);
                                view.onStopSend(GPIO_INDEX_4);
                            }
                        });
                    }
                    @Override
                    public void onComplete() {
                    }
                });
            }
        },0,REQUEST_STOP_ORDER_TIME);
    }

    private void startSend(final String gpioIndex) {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                view.onStartSend(gpioIndex);
            }
        });
    }

    private void stopSend(final String gpioIndex) {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                view.onStopSend(gpioIndex);
            }
        });

    }

    @Override
    public void checkUpdate() {
        RetrofitManager.getUpdateInfo().subscribe(new Observer<UpdateBean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(UpdateBean value) {
                LogWraper.d(TAG,"version"+value.getVersion());
                LogWraper.d(TAG,"url"+value.getUrl());
                if (haveNewVersion(value.getVersion())){
                    view.onCheckUpdateSucceed(true,value.getUrl());
                }else {
                    view.onCheckUpdateSucceed(false,null);
                }
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onComplete() {

            }
        });

    }

    @Override
    public void update(String url) {
        DownloadUtils downloadUtils = new DownloadUtils(context);
        //下载后安装新版本
        downloadUtils.downloadAPK(url, Environment.getExternalStorageDirectory().getPath(), "app-release.apk").subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }
            @Override
            public void onNext(Integer value) {
                view.onUpdate(value);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                view.onShowMsgDialog("下载失败");
            }

            @Override
            public void onComplete() {
                view.onLoad(false);
            }
        });
    }

    @Override
    public void loadXt(SystemBean.UcDataBean syb) {
        RetrofitManager.getXbBean(syb.getPrj_server(),syb.getPrj_database(),syb.getPrj_uid(),syb.getPrj_pwd())
                .subscribe(new Observer<XbBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }
                    @Override
                    public void onNext(XbBean value) {
                        view.onLoadXtSucceed(value.getUcData());
                    }
                    @Override
                    public void onError(Throwable e) {
                    }
                    @Override
                    public void onComplete() {

                    }
                });

    }

    @Override
    public void loadGz() {
        Map<String, String> sybXtGz = preferencesUtil.getSybXtGz();
        RetrofitManager.getGzBean(sybXtGz.get(PreferencesUtil.SYB_SERVER), sybXtGz.get(PreferencesUtil.SYB_DATABASE), sybXtGz.get(PreferencesUtil.SYB_UID), sybXtGz.get(PreferencesUtil.SYB_PWD))
                .subscribe(new Observer<GzBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(GzBean value) {
                        view.onLoadGzSucceed(value.getUcData());
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                    }
                });

    }

    @Override
    public void loadSyb() {
        RetrofitManager.getSystemName().subscribe(new Observer<SystemBean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(SystemBean value) {
                view.onLoadSybSecceed(value);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

    }

    @Override
    public void changeGzms(String road1GzmsStr, String road2GzmsStr) {
            this.gzms3 = road1GzmsStr;
            preferencesUtil.setGZMS3(road1GzmsStr);
            this.gzms4 = road2GzmsStr;
            preferencesUtil.setGZMS4(road2GzmsStr);
            view.onGpioGzmsChange(road1GzmsStr,road2GzmsStr);
    }

    @Override
    public void detroy() {
        timer.cancel();
        timer2.cancel();
        dateTimer.cancel();
    }

    @Override
    public void changeGzxx(String road1GzxxStr, String road2GzxxStr) {
        gzxx3 = road1GzxxStr;
        gzxx4 = road2GzxxStr;
    }

    @Override
    public void onGpioStatuChange(String index, String statu) {

    }
    /**
     * 是否有新版本
     * @param newVersion
     * @return
     */
    private boolean haveNewVersion(String newVersion) {
        PackageManager pm = context.getPackageManager();
        PackageInfo pi = null;
        try {
            pi = pm.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String oldVersion = pi.versionName;
        boolean isHaveNewVersion = false;
        //LogWraper.d(TAG, "haveNewVersion: newVersion"+newVersion);
        //LogWraper.d(TAG, "haveNewVersion: oldVersion"+oldVersion);
        String[] oldVersionArr = oldVersion.split("\\.");
        String[] newVersionArr = newVersion.split("\\.");

        //Log.d(TAG, "haveNewVersion: length"+oldVersionArr.length);
        for (int i=0;i<oldVersionArr.length; i++){
            if((Integer.parseInt(newVersionArr[i])) > (Integer.parseInt(oldVersionArr[i]))){
                isHaveNewVersion = true;
            }else if ((Integer.parseInt(newVersionArr[i])) == (Integer.parseInt(oldVersionArr[i]))){
                continue;
            }else{
                break;
            }
        }
        return isHaveNewVersion;
    }
}
