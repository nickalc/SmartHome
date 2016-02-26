package com.nick.smarthome.bean;

import java.util.List;

/**
 * Author:  nick
 * Email:   nickdevp@gmail.com
 * Date:    16/1/19 21:05.
 * Description:
 */
public class MyHouseInfoResult {


    /**
     * message : 信息查询成功！
     * data : {"houseInfo":{"houseTitle":"创客空间三楼","houseName":"创客空间三楼","address":"广州市天河区五山路靠近广东高校学生公寓","longitude":113.369,"latitude":23.1516,"houseId":10021},"rnLockList":[{"lockCode":"20150106291","roomId":10081,"rnLockId":10030,"locktype":"ROOM"}]}
     * statuscode : 1
     */

    private String message;
    /**
     * houseInfo : {"houseTitle":"创客空间三楼","houseName":"创客空间三楼","address":"广州市天河区五山路靠近广东高校学生公寓","longitude":113.369,"latitude":23.1516,"houseId":10021}
     * rnLockList : [{"lockCode":"20150106291","roomId":10081,"rnLockId":10030,"locktype":"ROOM"}]
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
        /**
         * houseTitle : 创客空间三楼
         * houseName : 创客空间三楼
         * address : 广州市天河区五山路靠近广东高校学生公寓
         * longitude : 113.369
         * latitude : 23.1516
         * houseId : 10021
         */

        private HouseInfoEntity houseInfo;
        /**
         * lockCode : 20150106291
         * roomId : 10081
         * rnLockId : 10030
         * locktype : ROOM
         */

        private List<RnLockListEntity> rnLockList;

        public void setHouseInfo(HouseInfoEntity houseInfo) {
            this.houseInfo = houseInfo;
        }

        public void setRnLockList(List<RnLockListEntity> rnLockList) {
            this.rnLockList = rnLockList;
        }

        public HouseInfoEntity getHouseInfo() {
            return houseInfo;
        }

        public List<RnLockListEntity> getRnLockList() {
            return rnLockList;
        }

        public static class HouseInfoEntity {
            private String houseTitle;
            private String houseName;
            private String address;
            private double longitude;
            private double latitude;
            private int houseId;

            public void setHouseTitle(String houseTitle) {
                this.houseTitle = houseTitle;
            }

            public void setHouseName(String houseName) {
                this.houseName = houseName;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public void setLongitude(double longitude) {
                this.longitude = longitude;
            }

            public void setLatitude(double latitude) {
                this.latitude = latitude;
            }

            public void setHouseId(int houseId) {
                this.houseId = houseId;
            }

            public String getHouseTitle() {
                return houseTitle;
            }

            public String getHouseName() {
                return houseName;
            }

            public String getAddress() {
                return address;
            }

            public double getLongitude() {
                return longitude;
            }

            public double getLatitude() {
                return latitude;
            }

            public int getHouseId() {
                return houseId;
            }
        }

        public static class RnLockListEntity {
            private String lockCode;
            private int roomId;
            private int rnLockId;
            private String locktype;

            public void setLockCode(String lockCode) {
                this.lockCode = lockCode;
            }

            public void setRoomId(int roomId) {
                this.roomId = roomId;
            }

            public void setRnLockId(int rnLockId) {
                this.rnLockId = rnLockId;
            }

            public void setLocktype(String locktype) {
                this.locktype = locktype;
            }

            public String getLockCode() {
                return lockCode;
            }

            public int getRoomId() {
                return roomId;
            }

            public int getRnLockId() {
                return rnLockId;
            }

            public String getLocktype() {
                return locktype;
            }
        }
    }
}
