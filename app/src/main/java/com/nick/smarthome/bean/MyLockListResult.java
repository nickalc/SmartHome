package com.nick.smarthome.bean;

import java.util.List;

/**
 * Author:  nick
 * Email:   nickdevp@gmail.com
 * Date:    15/12/25 15:28.
 * Description:
 */
public class MyLockListResult {


    /**
     * message : 信息查询成功！
     * data : {"list":[{"houseTitle":"广州碧桂园","customerId":10007,"lockType":"HOUSE","address":"广州增城新塘","doorNo":"8","lockCode":"55555555555555555555555","photoUrl":"","rnLockId":-7,"houseId":-2,"isBound":1}],"totalCnt":1,"totalPage":1}
     * statuscode : 1
     */

    private String message;
    /**
     * list : [{"houseTitle":"广州碧桂园","customerId":10007,"lockType":"HOUSE","address":"广州增城新塘","doorNo":"8","lockCode":"55555555555555555555555","photoUrl":"","rnLockId":-7,"houseId":-2,"isBound":1}]
     * totalCnt : 1
     * totalPage : 1
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
         * doorNo : 8
         * lockCode : 55555555555555555555555
         * photoUrl :
         * rnLockId : -7
         * houseId : -2
         * isBound : 1
         */

        private List<MyLockEntity> list;

        public void setTotalCnt(int totalCnt) {
            this.totalCnt = totalCnt;
        }

        public void setTotalPage(int totalPage) {
            this.totalPage = totalPage;
        }

        public void setList(List<MyLockEntity> list) {
            this.list = list;
        }

        public int getTotalCnt() {
            return totalCnt;
        }

        public int getTotalPage() {
            return totalPage;
        }

        public List<MyLockEntity> getList() {
            return list;
        }

//        public static class ListEntity {
//            private String houseTitle;
//            private int customerId;
//            private String lockType;
//            private String address;
//            private String doorNo;
//            private String lockCode;
//            private String photoUrl;
//            private int rnLockId;
//            private int houseId;
//            private int isBound;
//
//            public void setHouseTitle(String houseTitle) {
//                this.houseTitle = houseTitle;
//            }
//
//            public void setCustomerId(int customerId) {
//                this.customerId = customerId;
//            }
//
//            public void setLockType(String lockType) {
//                this.lockType = lockType;
//            }
//
//            public void setAddress(String address) {
//                this.address = address;
//            }
//
//            public void setDoorNo(String doorNo) {
//                this.doorNo = doorNo;
//            }
//
//            public void setLockCode(String lockCode) {
//                this.lockCode = lockCode;
//            }
//
//            public void setPhotoUrl(String photoUrl) {
//                this.photoUrl = photoUrl;
//            }
//
//            public void setRnLockId(int rnLockId) {
//                this.rnLockId = rnLockId;
//            }
//
//            public void setHouseId(int houseId) {
//                this.houseId = houseId;
//            }
//
//            public void setIsBound(int isBound) {
//                this.isBound = isBound;
//            }
//
//            public String getHouseTitle() {
//                return houseTitle;
//            }
//
//            public int getCustomerId() {
//                return customerId;
//            }
//
//            public String getLockType() {
//                return lockType;
//            }
//
//            public String getAddress() {
//                return address;
//            }
//
//            public String getDoorNo() {
//                return doorNo;
//            }
//
//            public String getLockCode() {
//                return lockCode;
//            }
//
//            public String getPhotoUrl() {
//                return photoUrl;
//            }
//
//            public int getRnLockId() {
//                return rnLockId;
//            }
//
//            public int getHouseId() {
//                return houseId;
//            }
//
//            public int getIsBound() {
//                return isBound;
//            }
//        }
    }
}
