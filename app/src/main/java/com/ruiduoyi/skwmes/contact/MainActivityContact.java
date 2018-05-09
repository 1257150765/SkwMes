package com.ruiduoyi.skwmes.contact;

import com.ruiduoyi.skwmes.bean.GzBean;
import com.ruiduoyi.skwmes.bean.SystemBean;
import com.ruiduoyi.skwmes.bean.XbBean;

import java.util.List;

/**
 * Created by Chen on 2018/4/25.
 */

public interface MainActivityContact {
    interface Presenter{
        void init();
        void checkUpdate();
        void update(String url);
        void loadXtAndGz(SystemBean.UcDataBean sybStr);
        void loadSyb();
        void changeGzms(String gzms3,String gzms4);
        void detroy();

    }
    interface View{
        void onLoadSybSecceed(SystemBean sybData);
        void onShowMsgDialog(String msg);
        void onGpioGzmsChange(String gzms1, String gzms2);
        void onUpdate(int progress);
        void onCheckUpdateSucceed(boolean hasUpdate, String url);
        void onNetInfoChange(String netInfo);
        void onLoad(boolean isLoad);
        void onStartSend(String gpioIndex);
        void onStopSend(String gpioIndex);
        void setSybXtGz(String sybStr, String xtStr, String gzStr1, String gzStr2);

        void onLoadXtSucceed(List<XbBean.UcDataBean> value);

        void onLoadGzSucceed(List<GzBean.UcDataBean> ucData);
    }
}
