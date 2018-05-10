package com.ruiduoyi.skwmes.bean;

import java.util.List;

/**
 * Created by Chen on 2018/5/9.
 */

public class DateBean {

    /**
     * utStatus : true
     * ucMsg : 数据读取成功
     * ucData : [{"v_curdate":"2018-05-09 20:08:15"}]
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
         * v_curdate : 2018-05-09 20:08:15
         */

        private String v_curdate;

        public String getV_curdate() {
            return v_curdate;
        }

        public void setV_curdate(String v_curdate) {
            this.v_curdate = v_curdate;
        }
    }
}
