package com.nick.smarthome.bean;

import java.util.List;

/**
 * Author:  nick
 * Email:   nickdevp@gmail.com
 * Date:    16/1/8 10:29.
 * Description:
 */
public class MyKeyListResult {

    /**
     * message : 信息查询成功！
     * data : {"totalPage ":1,"list":[{"houseTitle":"广州碧桂园雅苑","custOrderId":10034,"roomType":"ROOM","roomNo":"1003","timeSegmentList":[{"startTime":"14:00","checkInDate":"2015-01-01","checkOutDate":"2015-01-01","endTime":"16:00","timeId":4},{"startTime":"8:00","checkInDate":"2015-01-02","checkOutDate":"2015-01-02","endTime":"10:00","timeId":1},{"startTime":"18:00","checkInDate":"2015-01-03","checkOutDate":"2015-01-03","endTime":"20:00","timeId":6},{"startTime":"8:00","checkInDate":"2015-12-30","checkOutDate":"2015-12-30","endTime":"12:00","timeId":2},{"startTime":"20:00","checkInDate":"2015-12-31","checkOutDate":"2016-01-01","endTime":"8:00","timeId":7}],"keyState":"10N","lockKeyContent":"222222222222","houseAdress":"广州番禺南浦"}],"totalCnt":1}
     * statuscode : 1
     */

    private String message;
    /**
     * totalPage  : 1
     * list : [{"houseTitle":"广州碧桂园雅苑","custOrderId":10034,"roomType":"ROOM","roomNo":"1003","timeSegmentList":[{"startTime":"14:00","checkInDate":"2015-01-01","checkOutDate":"2015-01-01","endTime":"16:00","timeId":4},{"startTime":"8:00","checkInDate":"2015-01-02","checkOutDate":"2015-01-02","endTime":"10:00","timeId":1},{"startTime":"18:00","checkInDate":"2015-01-03","checkOutDate":"2015-01-03","endTime":"20:00","timeId":6},{"startTime":"8:00","checkInDate":"2015-12-30","checkOutDate":"2015-12-30","endTime":"12:00","timeId":2},{"startTime":"20:00","checkInDate":"2015-12-31","checkOutDate":"2016-01-01","endTime":"8:00","timeId":7}],"keyState":"10N","lockKeyContent":"222222222222","houseAdress":"广州番禺南浦"}]
     * totalCnt : 1
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
        private int totalPage;
        private int totalCnt;
        /**
         * houseTitle : 广州碧桂园雅苑
         * custOrderId : 10034
         * roomType : ROOM
         * roomNo : 1003
         * timeSegmentList : [{"startTime":"14:00","checkInDate":"2015-01-01","checkOutDate":"2015-01-01","endTime":"16:00","timeId":4},{"startTime":"8:00","checkInDate":"2015-01-02","checkOutDate":"2015-01-02","endTime":"10:00","timeId":1},{"startTime":"18:00","checkInDate":"2015-01-03","checkOutDate":"2015-01-03","endTime":"20:00","timeId":6},{"startTime":"8:00","checkInDate":"2015-12-30","checkOutDate":"2015-12-30","endTime":"12:00","timeId":2},{"startTime":"20:00","checkInDate":"2015-12-31","checkOutDate":"2016-01-01","endTime":"8:00","timeId":7}]
         * keyState : 10N
         * lockKeyContent : 222222222222
         * houseAdress : 广州番禺南浦
         */

        private List<ListEntity> list;

        public void setTotalPage(int totalPage) {
            this.totalPage = totalPage;
        }

        public void setTotalCnt(int totalCnt) {
            this.totalCnt = totalCnt;
        }

        public void setList(List<ListEntity> list) {
            this.list = list;
        }

        public int getTotalPage() {
            return totalPage;
        }

        public int getTotalCnt() {
            return totalCnt;
        }

        public List<ListEntity> getList() {
            return list;
        }

        public static class ListEntity {
            private String houseTitle;
            private int custOrderId;
            private String roomType;
            private String roomNo;
            private String keyState;
            private String orderCode;
            private String lockKeyContent;
            private String houseAdress;
            /**
             * startTime : 14:00
             * checkInDate : 2015-01-01
             * checkOutDate : 2015-01-01
             * endTime : 16:00
             * timeId : 4
             */

            private List<TimeSegmentListEntity> timeSegmentList;

            public void setHouseTitle(String houseTitle) {
                this.houseTitle = houseTitle;
            }

            public void setCustOrderId(int custOrderId) {
                this.custOrderId = custOrderId;
            }

            public void setRoomType(String roomType) {
                this.roomType = roomType;
            }


            public void setOrderCode(String orderCode) {
                this.orderCode = orderCode;
            }

            public void setRoomNo(String roomNo) {
                this.roomNo = roomNo;
            }

            public void setKeyState(String keyState) {
                this.keyState = keyState;
            }

            public void setLockKeyContent(String lockKeyContent) {
                this.lockKeyContent = lockKeyContent;
            }

            public void setHouseAdress(String houseAdress) {
                this.houseAdress = houseAdress;
            }

            public void setTimeSegmentList(List<TimeSegmentListEntity> timeSegmentList) {
                this.timeSegmentList = timeSegmentList;
            }

            public String getHouseTitle() {
                return houseTitle;
            }

            public int getCustOrderId() {
                return custOrderId;
            }

            public String getRoomType() {
                return roomType;
            }

            public String getRoomNo() {
                return roomNo;
            }

            public String getKeyState() {
                return keyState;
            }

            public String getOrderCode() {
                return orderCode;
            }

            public String getLockKeyContent() {
                return lockKeyContent;
            }

            public String getHouseAdress() {
                return houseAdress;
            }

            public List<TimeSegmentListEntity> getTimeSegmentList() {
                return timeSegmentList;
            }

            public static class TimeSegmentListEntity {
                private String startTime;
                private String checkInDate;
                private String checkOutDate;
                private String endTime;
                private int timeId;

                public void setStartTime(String startTime) {
                    this.startTime = startTime;
                }

                public void setCheckInDate(String checkInDate) {
                    this.checkInDate = checkInDate;
                }

                public void setCheckOutDate(String checkOutDate) {
                    this.checkOutDate = checkOutDate;
                }

                public void setEndTime(String endTime) {
                    this.endTime = endTime;
                }

                public void setTimeId(int timeId) {
                    this.timeId = timeId;
                }

                public String getStartTime() {
                    return startTime;
                }

                public String getCheckInDate() {
                    return checkInDate;
                }

                public String getCheckOutDate() {
                    return checkOutDate;
                }

                public String getEndTime() {
                    return endTime;
                }

                public int getTimeId() {
                    return timeId;
                }
            }
        }
    }
}
