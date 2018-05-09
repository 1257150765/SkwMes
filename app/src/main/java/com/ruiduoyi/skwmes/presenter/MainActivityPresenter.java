package com.ruiduoyi.skwmes.presenter;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;

import com.ruiduoyi.skwmes.Config;
import com.ruiduoyi.skwmes.bean.StopOrder;
import com.ruiduoyi.skwmes.bean.UpdateBean;
import com.ruiduoyi.skwmes.contact.MainActivityContact;
import com.ruiduoyi.skwmes.net.RetrofitManager;
import com.ruiduoyi.skwmes.util.DownloadUtils;
import com.ruiduoyi.skwmes.util.GpioUtil;
import com.ruiduoyi.skwmes.util.LogWraper;
import com.ruiduoyi.skwmes.util.PreferencesUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Chen on 2018/4/25.
 */

public class MainActivityPresenter implements MainActivityContact.Presenter, GpioUtil.OnGpioChangeListener {
    private static final String TAG = MainActivityPresenter.class.getSimpleName();
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
    private long timerDelay = 5000L;
    Timer timer = new Timer();
    private boolean isConnect = false;
    private boolean shouDong3 = false;
    private boolean shouDong4 = false;
    private PreferencesUtil preferencesUtil;

    private StopOrder value;

    public MainActivityPresenter(MainActivityContact.View view, Activity context) {
        this.view = view;
        this.context = context;
        preferencesUtil = new PreferencesUtil(context);
        shouDong3 = preferencesUtil.getShouDong3();
        shouDong4 = preferencesUtil.getShouDong4();
        init();
    }

    @Override
    public void init() {
        //RetrofitManager.init();

        Map<String, String> sybXtGz = preferencesUtil.getSybXtGz();
        if (null == sybXtGz){
            loadSyb();
        }else {
            view.setSybXtGz(sybXtGz.get(PreferencesUtil.SYB),sybXtGz.get(PreferencesUtil.XT),sybXtGz.get(PreferencesUtil.GZ));
        }
        gpioUtil3 = new GpioUtil(GPIO_INDEX_3, timerDelay, this);
        gpioUtil4 = new GpioUtil(GPIO_INDEX_4, timerDelay, this);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //轨道一
                LogWraper.d(TAG,"到五秒了，开始发送");
                if (null == value){
                    LogWraper.d(TAG,"没有接收到服务器命令");
                }else {
                    String isStop = value.getIsStop();
                    LogWraper.d(TAG,"接收到服务器命令,一轨指令--"+isStop);
                    if (Config.IS_STOP_1.equals(isStop)) {

                        //如果是停止状态，并且手工设置为启动，则启动
                        if (shouDong3){
                            LogWraper.d(TAG,"一轨发送一次信号");
                            startSend(GPIO_INDEX_3);
                            gpioUtil3.sendOne();
                        }
                    } else if (Config.IS_STOP_0.equals(isStop)||!isConnect) {
                        //stopSend(GPIO_INDEX_3);
                        //当接收到暂停命令时，不做处理即可，
                    }
                    //轨道二
                    String isStop2 = value.getIsStop2();
                    LogWraper.d(TAG,"接收到服务器命令,二轨指令--"+isStop2);
                    if (Config.IS_STOP_1.equals(isStop2)) {
                        //如果是停止状态，并且手工设置为启动，则启动
                        if (shouDong4){
                            LogWraper.d(TAG,"二轨发送一次信号");
                            startSend(GPIO_INDEX_4);
                            gpioUtil4.sendOne();
                        }
                    } else if (Config.IS_STOP_0.equals(isStop2)||!isConnect) {
                        //stopSend(GPIO_INDEX_4);
                        //当接收到暂停命令时，不做处理即可，
                    }
                }
                RetrofitManager.getStopOrder().subscribe(new Observer<StopOrder>() {
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
                });
            }
        },0,5000);
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
    public void loadXtAndGz(String sybStr) {
        List<String> xtData;
        List<String> gzData;
        xtData = new ArrayList<>();
        xtData.add("点我选择线体");
        for (int i=1; i<=8; i++){
            xtData.add("SMT0"+(i));
        }
        gzData = new ArrayList<>();
        gzData.add("点我选择工站");
        gzData.add("SPI");
        gzData.add("AOI1");
        gzData.add("AOI2");
        view.onLoadXtAndGzSucceed(xtData,gzData);
    }

    @Override
    public void loadSyb() {
        List<String> sybData;
        sybData = new ArrayList<>();
        sybData.add("点我选择系统");
        sybData.add("SKWMES-ENOK");
        sybData.add("SKWMES-OPPO");
        sybData.add("SKWMES-SAMSUNG");
        view.onLoadSybSecceed(sybData);
    }

    @Override
    public void changeGpioStatu(String gpioIndex) {
        if (GPIO_INDEX_3.equals(gpioIndex)) {
            //切换发送状态
            shouDong3 = !shouDong3;
            if (shouDong3) {
                startSend(gpioIndex);
            } else {
                stopSend(gpioIndex);
            }
        } else if (GPIO_INDEX_4.equals(gpioIndex)) {
            //切换发送状态
            shouDong4 = !shouDong4;
            if (shouDong4) {
                startSend(gpioIndex);
            } else {
                stopSend(gpioIndex);
            }
        }
    }

    /**
     * 启动发送
     * @param gpioIndex 引脚
     */

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

    /**
     * 停止发送
     * @param gpioIndex
     */

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


    }

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
