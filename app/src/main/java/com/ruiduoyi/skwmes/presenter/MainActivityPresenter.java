package com.ruiduoyi.skwmes.presenter;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;

import com.ruiduoyi.skwmes.Config;
import com.ruiduoyi.skwmes.bean.DateBean;
import com.ruiduoyi.skwmes.bean.GzBean;
import com.ruiduoyi.skwmes.bean.InfoBean;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
    private static final long CHECK_UPDATE_TIME = 4*60*60*1000;
    //自动更新
    private static final long TYPE_AUTO_UPDATE = 100L;
    //手动更新
    private static final long TYPE_HAND_UPDATE = 101L;
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
    /*Timer timer = new Timer();
    Timer timer2 = new Timer();
    Timer dateTimer = new Timer();*/
    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;
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
        //启动服务，把scheduledThreadPoolExecutor保存起来，就不会被销毁
        //note:服务仅做保存，不做任何处理
        /*context.startService(new Intent(context, BackGroundService.class));
        scheduledThreadPoolExecutor = BackGroundService.getScheduledThreadPoolExecutor();
        if (scheduledThreadPoolExecutor == null){
            BackGroundService.setScheduledThreadPoolExecutor(scheduledThreadPoolExecutor);
        }*/
        scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(5);
        init();
    }

    @Override
    public void init() {
        //获取已保存的工作模式
        gzms3 = preferencesUtil.getGZMS3();
        gzms4 = preferencesUtil.getGZMS4();

        //获取保存的系统名称，线体名称，工站名称
        final Map<String, String> sybXtGz = preferencesUtil.getSybXtGz();
        if (null == sybXtGz){
            loadSystem();
        }else {
            //获取已保存的工站代码信息
            gzxx3 = preferencesUtil.getSybXtGz().get(PreferencesUtil.GZ_Code_3);
            gzxx4 = preferencesUtil.getSybXtGz().get(PreferencesUtil.GZ_Code_4);
            view.onGpioGzmsChange(gzms3,gzms4);
            view.setSybXt(sybXtGz.get(PreferencesUtil.SYB_NAME),sybXtGz.get(PreferencesUtil.XT_CODE)+" "+sybXtGz.get(PreferencesUtil.XT_NAME));
            view.setGz(sybXtGz.get(PreferencesUtil.GZ_NAME_3),sybXtGz.get(PreferencesUtil.GZ_NAME_4));
        }

        gpioUtil3 = new GpioUtil(GPIO_INDEX_3, this);
        gpioUtil4 = new GpioUtil(GPIO_INDEX_4,this);
        scheduledThreadPoolExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if ("".equals(gzxx3)||"".equals(gzxx4)){
                    LogWraper.d(TAG,"无工站信息");
                    return;
                }
                LogWraper.d(TAG,"到五秒了，开始发送");
                LogWraper.d(TAG,"一轨工作模式："+gzms3);
                LogWraper.d(TAG,"二轨工作模式："+gzms4);
                //轨道一
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

            }
        },0,SEND_GPIO_TIME, TimeUnit.MILLISECONDS);
        scheduledThreadPoolExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
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
                            String time = value.getUcData().get(0).getV_curdate();
                            String[] split = time.split("T");
                            String date1 = split[0].replaceAll("-", "");
                            String[] date2 = split[1].split(":");
                            int hourInt = Integer.parseInt(date2[0]);
                            if (hourInt>12){
                                date2[0] = ""+(hourInt-12);
                            }else {
                                date2[0] = ""+hourInt;
                            }
                            StringBuilder sb = new StringBuilder();
                            for (int i = 0; i < date2.length; i++) {
                                sb.append(date2[i]);
                            }
                            String resultTime = date1 +"."+ sb.toString();
                            LogWraper.d(TAG,"初始化APP，设置系统时间"+resultTime);
                            //设置时间需要转换成12小时制
                            Util.setSystemTime(context,resultTime);
                            final SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd E");
                            scheduledThreadPoolExecutor.scheduleAtFixedRate(new Runnable() {
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
                            },0,12*60*60*1000,TimeUnit.MILLISECONDS);
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
                    }
                    @Override
                    public void onComplete() {
                    }
                });
            }
        },0,SEND_GPIO_TIME,TimeUnit.MILLISECONDS);
        //此定时器每隔10s，请求一次工站信息
        scheduledThreadPoolExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                //如果没有网络连接|没有设置工站|没有设置系统|线体直接返回
                Map<String, String> sybXtGz1 = preferencesUtil.getSybXtGz();
                if (!isConnect || "".equals(gzxx3)||"".equals(gzxx4)||null == sybXtGz1){
                    return;
                }
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
                        LogWraper.d(TAG,"请求服务器信息");
                        //每次有返回值表示与服务器有连接
                        MainActivityPresenter.this.value = value;
                        //一轨的信息
                        InfoBean.UcDataBean info3 = null;
                        //二轨的信息
                        InfoBean.UcDataBean info4 = null;
                        //这里做处理是为了让工站对齐，（返回的工站代码和轨道设置的工站可能会不在同一位置）
                        for (InfoBean.UcDataBean bean:value.getUcData()){
                            //找到一轨的工站代码
                            if (gzxx3.equals(bean.getErl_gzdm())){
                                info3 = bean;
                            }
                            //找到二轨的工站代码
                            if (gzxx4.equals(bean.getErl_gzdm())){
                                info4 = bean;
                            }
                        }
                        view.onLoadInfoSucceed(info3,info4);
                    }

                    @Override
                    public void onError(Throwable e) {
                        MainActivityPresenter.this.value = null;
                        view.onLoadInfoSucceed(null,null);
                        //view.onShowMsgDialog("加载服务器指令出错");
                    }
                    @Override
                    public void onComplete() {
                    }
                });
            }
        },0,REQUEST_STOP_ORDER_TIME, TimeUnit.MILLISECONDS);

        scheduledThreadPoolExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                checkUpdate();
            }
        },0,CHECK_UPDATE_TIME,TimeUnit.MILLISECONDS);

    }

    /**
     * 开始发送gpio信号，（改变UI显示）
     * @param gpioIndex
     */
    private void startSend(final String gpioIndex) {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                view.onStartSend(gpioIndex);
            }
        });
    }
    /**
     * 结束发送gpio信号，（改变UI显示）
     * @param gpioIndex
     */
    private void stopSend(final String gpioIndex) {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                view.onStopSend(gpioIndex);
            }
        });

    }

    /**
     * 检查更新
     *
     */
    @Override
    public void checkUpdate() {
        RetrofitManager.getUpdateInfo().subscribe(new Observer<UpdateBean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(UpdateBean value) {
                UpdateBean.UcDataBean bean = value.getUcData().get(0);
                if ("Y".equals(bean.getV_UpFlag())) {
                    if (haveNewVersion(bean.getV_SrvVer())) {
                        view.onCheckUpdateSucceed(true, bean.getV_UpAddr());
                    } else {
                        view.onCheckUpdateSucceed(false, bean.getV_UpAddr());
                    }
                }else {
                    view.onCheckUpdateSucceed(false, null);
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

    /**
     * 正在更新
     * @param url
     */
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
                view.onUpdateSucceed();
            }

            @Override
            public void onComplete() {
                view.onUpdateSucceed();
            }
        });
    }

    /**
     * 加载线体
     * @param syb
     */
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
                        //view.onLoad(false);
                    }
                });

    }

    /**
     * 加载工站
     */
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

    /**
     * 加载系统
     */
    @Override
    public void loadSystem() {
        RetrofitManager.getSystemName().subscribe(new Observer<SystemBean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(SystemBean value) {
                view.onLoadSystemSecceed(value);
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        });

    }

    /**
     * 改变工作模式
     * @param road1GzmsStr
     * @param road2GzmsStr
     */
    @Override
    public void changeGzms(String road1GzmsStr, String road2GzmsStr) {
        //上面的代码，每次发送信号都要根据工作模式判断
        this.gzms3 = road1GzmsStr;
        preferencesUtil.setGZMS3(road1GzmsStr);
        this.gzms4 = road2GzmsStr;
        preferencesUtil.setGZMS4(road2GzmsStr);
        //改变UI
        view.onGpioGzmsChange(road1GzmsStr,road2GzmsStr);
    }
    /**
     * 改变工站信息
     * @param road1GzxxStr
     * @param road2GzxxStr
     */
    @Override
    public void changeGzxx(String road1GzxxStr, String road2GzxxStr) {
        gzxx3 = road1GzxxStr;
        gzxx4 = road2GzxxStr;
    }

    /**
     * 退出系统，销毁资源
     */
    @Override
    public void detroy() {
        scheduledThreadPoolExecutor.shutdown();
    }

    /**
     * goio状态监听器，暂时不用
     * @param index
     * @param statu
     */
    @Override
    public void onGpioStatuChange(String index, String statu) {}
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
        try {
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
        }catch (Exception e){
            return false;
        }
        return isHaveNewVersion;
    }
}
