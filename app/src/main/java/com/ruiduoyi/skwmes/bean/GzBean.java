package com.ruiduoyi.skwmes.bean;

import java.util.List;

/**
 * Created by Chen on 2018/5/9.
 */

public class GzBean {
    /**
     * utStatus : true
     * ucMsg : 数据读取成功！
     * ucData : [{"v_gzdm":"OP100","v_gzname":"PCB UPN 绑定大板SN"},{"v_gzdm":"OP211","v_gzname":"轨道1 SPI工站 1 数据采集"},{"v_gzdm":"OP213","v_gzname":"轨道1 AOI工站 1 数据采集"},{"v_gzdm":"OP214","v_gzname":"轨道1 AOI工站 2 数据采集"},{"v_gzdm":"OP221","v_gzname":"轨道2 SPI工站 1 数据采集"},{"v_gzdm":"OP223","v_gzname":"轨道2 AOI工站 1 数据采集"},{"v_gzdm":"OP224","v_gzname":"轨道2 AOI工站 2 数据采集"},{"v_gzdm":"OP300","v_gzname":"PCB大板SN绑定小板SN"}]
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
         * v_gzdm : OP100
         * v_gzname : PCB UPN 绑定大板SN
         */

        private String v_gzdm;
        private String v_gzname;

        public String getV_gzdm() {
            return v_gzdm;
        }

        public void setV_gzdm(String v_gzdm) {
            this.v_gzdm = v_gzdm;
        }

        public String getV_gzname() {
            return v_gzname;
        }

        public void setV_gzname(String v_gzname) {
            this.v_gzname = v_gzname;
        }
    }
}
