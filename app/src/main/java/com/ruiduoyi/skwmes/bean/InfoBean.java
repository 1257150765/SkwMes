package com.ruiduoyi.skwmes.bean;

import java.util.List;

/**
 * Created by Chen on 2018/5/10.
 */

public class InfoBean {

    /**
     * utStatus : true
     * ucMsg : 数据读取成功！
     * ucData : [{"erl_xbdm":"SMT01","erl_gzdm":"OP214","erl_signal":true,"erl_color":"G","erl_allycms":""},{"erl_xbdm":"SMT01","erl_gzdm":"OP224","erl_signal":true,"erl_color":"G","erl_allycms":""}]
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
         * erl_xbdm : SMT01
         * erl_gzdm : OP214
         * erl_signal : true
         * erl_color : G
         * erl_allycms :
         */
        private String erl_xbdm;
        private String erl_gzdm;
        private boolean erl_signal;
        private String erl_color;
        private String erl_allycms;

        public String getErl_xbdm() {
            return erl_xbdm;
        }

        public void setErl_xbdm(String erl_xbdm) {
            this.erl_xbdm = erl_xbdm;
        }

        public String getErl_gzdm() {
            return erl_gzdm;
        }

        public void setErl_gzdm(String erl_gzdm) {
            this.erl_gzdm = erl_gzdm;
        }

        public boolean isErl_signal() {
            return erl_signal;
        }

        public void setErl_signal(boolean erl_signal) {
            this.erl_signal = erl_signal;
        }

        public String getErl_color() {
            return erl_color;
        }

        public void setErl_color(String erl_color) {
            this.erl_color = erl_color;
        }

        public String getErl_allycms() {
            return erl_allycms;
        }

        public void setErl_allycms(String erl_allycms) {
            this.erl_allycms = erl_allycms;
        }
    }
}
