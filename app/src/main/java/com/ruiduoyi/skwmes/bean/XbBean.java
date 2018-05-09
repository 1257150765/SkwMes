package com.ruiduoyi.skwmes.bean;

import java.util.List;

/**
 * Created by Chen on 2018/5/9.
 */

public class XbBean {
    /**
     * utStatus : true
     * ucMsg : 数据读取成功！
     * ucData : [{"v_xbdm":"SMT01","v_xbname":"贴片1线"},{"v_xbdm":"SMT02","v_xbname":"贴片2线"},{"v_xbdm":"SMT03","v_xbname":"贴片3线"},{"v_xbdm":"SMT04","v_xbname":"贴片4线"},{"v_xbdm":"SMT05","v_xbname":"贴片5线"},{"v_xbdm":"SMT06","v_xbname":"贴片6线"},{"v_xbdm":"SMT07","v_xbname":"贴片7线"},{"v_xbdm":"SMT08","v_xbname":"贴片8线"}]
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
         * v_xbdm : SMT01
         * v_xbname : 贴片1线
         */

        private String v_xbdm;
        private String v_xbname;

        public String getV_xbdm() {
            return v_xbdm;
        }

        public void setV_xbdm(String v_xbdm) {
            this.v_xbdm = v_xbdm;
        }

        public String getV_xbname() {
            return v_xbname;
        }

        public void setV_xbname(String v_xbname) {
            this.v_xbname = v_xbname;
        }
    }
}
