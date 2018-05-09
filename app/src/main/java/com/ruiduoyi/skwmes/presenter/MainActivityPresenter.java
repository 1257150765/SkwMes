package com.ruiduoyi.skwmes.presenter;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;

import com.ruiduoyi.skwmes.Config;
import com.ruiduoyi.skwmes.bean.GzBean;
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
    private boolean isConnect = false;

    private String gzms3 = "";
    private String gzms4 = "";
    private String gzxx3 = "";
    private String gzxx4 = "";
    private PreferencesUtil preferencesUtil;

    private StopOrder value;


    public MainActivityPresenter(MainActivityContact.View view, Activity context) {
        this.view = view;
        this.context = context;
        preferencesUtil = new PreferencesUtil(context);
        gzms3 = preferencesUtil.getGZMS3();
        gzms4 = preferencesUtil.getGZMS4();
        view.onGpioGzmsChange(GPIO_INDEX_3,gzms3);
        view.onGpioGzmsChange(GPIO_INDEX_4,gzms4);
        init();
    }

    @Override
    public void init() {
        //RetrofitManager.init();

        Map<String, String> sybXtGz = preferencesUtil.getSybXtGz();
        if (null == sybXtGz){
            loadSyb();
        }else {
            view.setSybXtGz(sybXtGz.get(PreferencesUtil.SYB_NAME),sybXtGz.get(PreferencesUtil.XT_NAME),sybXtGz.get(PreferencesUtil.GZ_NAME_3), sybXtGz.get(PreferencesUtil.GZ_NAME_4));
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
                            String isStop = value.getIsStop();
                            LogWraper.d(TAG,"接收到服务器命令,一轨指令--"+isStop);
                            if (Config.IS_STOP_1.equals(isStop)) {
                                gpioUtil3.sendOne();
                                startSend(GPIO_INDEX_3);
                                LogWraper.d(TAG,"一轨发送一次信号");
                            } else if (Config.IS_STOP_0.equals(isStop)||!isConnect) {
                                //stopSend(GPIO_INDEX_3);
                                //当接收到暂停命令时，不做处理即可，
                                stopSend(GPIO_INDEX_3);
                            }
                        }
                        break;
                    //手工放行
                    case Config.GZMS_SGFX:
                        if (null == value){
                            LogWraper.d(TAG,"没有接收到服务器命令");
                        }else {
                            String isStop = value.getIsStop();
                            LogWraper.d(TAG,"一轨发送一次信号");
                            gpioUtil3.sendOne();
                            startSend(GPIO_INDEX_3);
                        }
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
                            String isStop2 = value.getIsStop2();
                            LogWraper.d(TAG,"接收到服务器命令,二轨指令--"+isStop2);
                            if (Config.IS_STOP_1.equals(isStop2)) {
                                LogWraper.d(TAG,"二轨发送一次信号");
                                gpioUtil4.sendOne();
                                startSend(GPIO_INDEX_4);
                            } else if (Config.IS_STOP_0.equals(isStop2)||!isConnect) {
                                //stopSend(GPIO_INDEX_4);
                                //当接收到暂停命令时，不做处理即可，
                                stopSend(GPIO_INDEX_4);
                            }
                        }
                        break;
                    //手工放行
                    case Config.GZMS_SGFX:
                        if (null == value){
                            LogWraper.d(TAG,"没有接收到服务器命令");
                        }else {
                            LogWraper.d(TAG,"二轨发送一次信号");
                            gpioUtil4.sendOne();
                            startSend(GPIO_INDEX_4);
                        }
                        break;
                    //暂停运行
                    case Config.GZMS_ZTYX:
                        stopSend(GPIO_INDEX_4);
                        break;
                    default:
                        break;
                }
                RetrofitManager.getSystemName().subscribe(new Observer<SystemBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }
                    @Override
                    public void onNext(final SystemBean value) {
                        if (context.isDestroyed()){
                            return;
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
                /*RetrofitManager.getStopOrder().subscribe(new Observer<StopOrder>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }
                    @Override
                    public void onNext(final StopOrder value) {
                        if (context.isDestroyed()){
                            return;
                        }
                        LogWraper.d(TAG,"开始请求下一次发送命令");
                        //每次有返回值表示与服务器有连接
                        MainActivityPresenter.this.value = value;
                        isConnect = true;
                        LogWraper.d(TAG, "" + value.getIsStop());
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
                });*/
                //检测到服务器连接失败，不请求数据
                if (!isConnect){
                    return;
                }
                RetrofitManager.getSystemName().subscribe(new Observer<SystemBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }
                    @Override
                    public void onNext(final SystemBean value) {
                        if (context.isDestroyed()){
                            return;
                        }
                        LogWraper.d(TAG,"开始请求下一次发送命令");
                        //每次有返回值表示与服务器有连接
                        StopOrder stopOrder = new StopOrder();
                        stopOrder.setIsStop("1");
                        stopOrder.setIsStop2("1");
                        MainActivityPresenter.this.value = stopOrder;
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
    public void loadXtAndGz(SystemBean.UcDataBean syb) {
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
        RetrofitManager.getGzBean(syb.getPrj_server(),syb.getPrj_database(),syb.getPrj_uid(),syb.getPrj_pwd())
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



    /**
     * 启动发送
     * @param gpioIndex 引脚
     *//*

    private void startSend(final String gpioIndex) {
        if (GPIO_INDEX_3.equals(gpioIndex)) {
            if (null != gpioUtil3) {
                shouDong3 = true;
                //保存手工设置的状态
                preferencesUtil.setShouDong3(true);
            }

        } else if (GPIO_INDEX_4.equals(gpioIndex)) {
            if (null != gpioUtil4) {
                shouDong4 = true;
                preferencesUtil.setShouDong4(true);
            }
        }
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                view.onStartSend(gpioIndex);
            }
        });

    }

    *//**
     * 停止发送
     * @param
     *//*

    private void stopSend(final String gpioIndex) {
        if (GPIO_INDEX_3.equals(gpioIndex)) {
            if (null != gpioUtil3) {
                shouDong3 = false;
                //保存手工设置的状态
                preferencesUtil.setShouDong3(false);
            }
        } else if (GPIO_INDEX_4.equals(gpioIndex)) {
            if (null != gpioUtil4) {
                shouDong4 = false;
                preferencesUtil.setShouDong4(false);
            }
        }
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                view.onStopSend(gpioIndex);
            }
        });


    }*/

    @Override
    public void detroy() {
        timer.cancel();
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
