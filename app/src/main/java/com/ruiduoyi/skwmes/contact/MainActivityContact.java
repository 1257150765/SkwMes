package com.ruiduoyi.skwmes.contact;

import java.util.List;

/**
 * Created by Chen on 2018/4/25.
 */

public interface MainActivityContact {
    interface Presenter{
        void init();
        void checkUpdate();
        void update(String url);
        void loadXtAndGz(String sybStr);
        void loadSyb();
        void changeGpioStatu(String gpioIndex);
        void detroy();

    }
    interface View{
        void onLoadSybSecceed(List<String> sybData);
        void onLoadXtAndGzSucceed(List<String> xtData, List<String> gzData);
        void onShowMsgDialog(String msg);
        void onGpioStatuChange(String gpioIndex, String statu);
        void onUpdate(int progress);
        void onCheckUpdateSucceed(boolean hasUpdate, String url);
        void onNetInfoChange(String netInfo);
        void onLoad(boolean isLoad);
        void onStartSend(String gpioIndex);
        void onStopSend(String gpioIndex);
        void setSybXtGz(String sybStr, String xtStr, String gzStr);
    }
}
