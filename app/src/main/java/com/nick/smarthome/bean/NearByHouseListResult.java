package com.nick.smarthome.bean;

import java.util.List;

/**
 * Author:  nick
 * Email:   nickdevp@gmail.com
 * Date:    15/12/29 10:44.
 * Description:
 */
public class NearByHouseListResult {
    private String message;

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
