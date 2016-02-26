package com.nick.smarthome.bean;

import java.util.List;

/**
 * Author:  nick
 * Email:   nickdevp@gmail.com
 * Date:    15/12/29 10:44.
 * Description:
 */
public class NearByHouseListResult {


    /**
     * message : 信息查询成功！
     * data : {"list":[{"houseTitle":"广州碧桂园","customerId":10007,"lockType":"HOUSE","address":"广州增城新塘","isHaveModify":1,"doorNo":"8","lockCode":"100000000011","photoUrl":"/abc.jpg","rnLockId":-7,"houseId":-2,"isBound":0}],"totalCnt":98,"totalPage":10}
     * statuscode : 1
     */

    private String message;
    /**
     * list : [{"houseTitle":"广州碧桂园","customerId":10007,"lockType":"HOUSE","address":"广州增城新塘","isHaveModify":1,"doorNo":"8","lockCode":"100000000011","photoUrl":"/abc.jpg","rnLockId":-7,"houseId":-2,"isBound":0}]
     * totalCnt : 98
     * totalPage : 10
     */

    private DataEntity data;
    private String statuscode;

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public void setStatuscode(String statuscode) {
        this.statuscode = statuscode;
    }

    public String getMessage() {
        return message;
    }

    public DataEntity getData() {
        return data;
    }

    public String getStatuscode() {
        return statuscode;
    }

    public static class DataEntity {
        private int totalCnt;
        private int totalPage;
        /**
         * houseTitle : 广州碧桂园
         * customerId : 10007
         * lockType : HOUSE
         * address : 广州增城新塘
         * isHaveModify : 1
         * doorNo : 8
         * lockCode : 100000000011
         * photoUrl : /abc.jpg
         * rnLockId : -7
         * houseId : -2
         * isBound : 0
         */

        private List<NearByHouseEntity> list;

        public void setTotalCnt(int totalCnt) {
            this.totalCnt = totalCnt;
        }

        public void setTotalPage(int totalPage) {
            this.totalPage = totalPage;
        }

        public void setList(List<NearByHouseEntity> list) {
            this.list = list;
        }

        public int getTotalCnt() {
            return totalCnt;
        }

        public int getTotalPage() {
            return totalPage;
        }

        public List<NearByHouseEntity> getList() {
            return list;
        }

    }
}
