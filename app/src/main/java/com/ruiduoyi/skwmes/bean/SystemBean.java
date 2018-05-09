package com.ruiduoyi.skwmes.bean;

import java.util.List;

/**
 * Created by Chen on 2018/5/9.
 */

public class SystemBean {

    /**
     * utStatus : true
     * ucMsg : 数据读取成功！
     * ucData : [{"prj_code":"P01","prj_name":"OPPO-SMT","prj_flag":true,"prj_server":"4D546B794C6A45324F4334794C6A49304F513D3D","prj_database":"553074584F4639536157317A5830467763413D3D","prj_uid":"556D6C7463773D3D","prj_pwd":"626D397759576C75626D396E59576C75","prj_jlry":"System","prj_jlrq":"2018-05-08T20:35:04.15"},{"prj_code":"P02","prj_name":"中诺-SMT","prj_flag":true,"prj_server":"4D546B794C6A45324F4334794C6A493D","prj_database":"576C4A66556D6C74633139426348413D","prj_uid":"556D6C7463773D3D","prj_pwd":"626D397759576C75626D396E59576C75","prj_jlry":"System","prj_jlrq":"2018-05-08T20:35:04.15"},{"prj_code":"P03","prj_name":"三星-SMT","prj_flag":true,"prj_server":"4D546B794C6A45324F4334794C6A49304F513D3D","prj_database":"55316866556D6C74633139426348413D","prj_uid":"556D6C7463773D3D","prj_pwd":"626D397759576C75626D396E59576C75","prj_jlry":"System","prj_jlrq":"2018-05-08T20:35:04.15"},{"prj_code":"P04","prj_name":"闻泰-SMT","prj_flag":true,"prj_server":"4D546B794C6A45324F4334794C6A493D","prj_database":"56315266556D6C74633139426348413D","prj_uid":"556D6C7463773D3D","prj_pwd":"626D397759576C75626D396E59576C75","prj_jlry":"System","prj_jlrq":"2018-05-08T20:35:04.15"}]
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
         * prj_code : P01
         * prj_name : OPPO-SMT
         * prj_flag : true
         * prj_server : 4D546B794C6A45324F4334794C6A49304F513D3D
         * prj_database : 553074584F4639536157317A5830467763413D3D
         * prj_uid : 556D6C7463773D3D
         * prj_pwd : 626D397759576C75626D396E59576C75
         * prj_jlry : System
         * prj_jlrq : 2018-05-08T20:35:04.15
         */

        private String prj_code;
        private String prj_name;
        private boolean prj_flag;
        private String prj_server;
        private String prj_database;
        private String prj_uid;
        private String prj_pwd;
        private String prj_jlry;
        private String prj_jlrq;

        public String getPrj_code() {
            return prj_code;
        }

        public void setPrj_code(String prj_code) {
            this.prj_code = prj_code;
        }

        public String getPrj_name() {
            return prj_name;
        }

        public void setPrj_name(String prj_name) {
            this.prj_name = prj_name;
        }

        public boolean isPrj_flag() {
            return prj_flag;
        }

        public void setPrj_flag(boolean prj_flag) {
            this.prj_flag = prj_flag;
        }

        public String getPrj_server() {
            return prj_server;
        }

        public void setPrj_server(String prj_server) {
            this.prj_server = prj_server;
        }

        public String getPrj_database() {
            return prj_database;
        }

        public void setPrj_database(String prj_database) {
            this.prj_database = prj_database;
        }

        public String getPrj_uid() {
            return prj_uid;
        }

        public void setPrj_uid(String prj_uid) {
            this.prj_uid = prj_uid;
        }

        public String getPrj_pwd() {
            return prj_pwd;
        }

        public void setPrj_pwd(String prj_pwd) {
            this.prj_pwd = prj_pwd;
        }

        public String getPrj_jlry() {
            return prj_jlry;
        }

        public void setPrj_jlry(String prj_jlry) {
            this.prj_jlry = prj_jlry;
        }

        public String getPrj_jlrq() {
            return prj_jlrq;
        }

        public void setPrj_jlrq(String prj_jlrq) {
            this.prj_jlrq = prj_jlrq;
        }
    }
}
