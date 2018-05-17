package com.ruiduoyi.skwmes.bean;

import java.util.List;

/**
 * Created by Chen on 2018/4/25.
 */

public class UpdateBean {

    /**
     * utStatus : true
     * ucMsg : 数据读取成功
     * ucData : [{"v_UpFlag":"Y","v_SrvVer":"1.0.0.1","v_UpAddr":"http://192.168.2.2:9998/Update/SmtApp.apk"}]
     */

    private boolean utStatus;
    private String ucMsg;
    private List<UcDataBean> ucData;

    public boolean isUtStatus() {
        return utStatus;
    }

    public void setUtStatus(boolean utStatus) {
        this.utStatus = utStatus;
    }

    public String getUcMsg() {
        return ucMsg;
    }

    public void setUcMsg(String ucMsg) {
        this.ucMsg = ucMsg;
    }

    public List<UcDataBean> getUcData() {
        return ucData;
    }

    public void setUcData(List<UcDataBean> ucData) {
        this.ucData = ucData;
    }

    public static class UcDataBean {
        /**
         * v_UpFlag : Y
         * v_SrvVer : 1.0.0.1
         * v_UpAddr : http://192.168.2.2:9998/Update/SmtApp.apk
         */

        private String v_UpFlag;
        private String v_SrvVer;
        private String v_UpAddr;

        public String getV_UpFlag() {
            return v_UpFlag;
        }

        public void setV_UpFlag(String v_UpFlag) {
            this.v_UpFlag = v_UpFlag;
        }

        public String getV_SrvVer() {
            return v_SrvVer;
        }

        public void setV_SrvVer(String v_SrvVer) {
            this.v_SrvVer = v_SrvVer;
        }

        public String getV_UpAddr() {
            return v_UpAddr;
        }

        public void setV_UpAddr(String v_UpAddr) {
            this.v_UpAddr = v_UpAddr;
        }
    }
}
