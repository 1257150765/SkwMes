package com.ruiduoyi.skwmes.contact;

import com.ruiduoyi.skwmes.bean.GzBean;
import com.ruiduoyi.skwmes.bean.InfoBean;
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
        void loadXt(SystemBean.UcDataBean sybStr);
        void loadGz();
        void loadSystem();
        void changeGzms(String gzms3,String gzms4);
        void detroy();
        void changeGzxx(String road1GzxxStr, String road2GzxxStr);
    }
    interface View{
        void onLoadSystemSecceed(SystemBean sybData);
        void onShowMsgDialog(String msg);
        void onGpioGzmsChange(String gzms1, String gzms2);
        void onUpdate(int progress);
        void onCheckUpdateSucceed(boolean hasUpdate, String url);
        void onNetInfoChange(String netInfo);
        void onLoad(boolean isLoad);
        void onStartSend(String gpioIndex);
        void onStopSend(String gpioIndex);
        void setSybXt(String sybStr, String xtStr);
        void setGz( String gzStr1, String gzStr2);
        void onLoadXtSucceed(List<XbBean.UcDataBean> value);
        void onLoadGzSucceed(List<GzBean.UcDataBean> ucData);
        void onDateUpdate(String date);
        void onLoadInfoSucceed(InfoBean.UcDataBean info3, InfoBean.UcDataBean info4);
        void onUpdateSucceed();
    }
}
