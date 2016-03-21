package com.nick.smarthome.bean;

import java.util.List;

/**
 * Author:  nick
 * Email:   nickdevp@gmail.com
 * Date:    15/12/14 12:59.
 * Description:
 */
public class TradeListResult {


    /**
     * message : 信息查询成功！
     * data : {"totalPage ":2,"list":[{"houseTitle":"汇景新城","custOrderId":10089,"payStatus":false,"orderCode":"M20160118134835773","roomType":"HOUSE","roomNo":"3","timeSegmentList":[{"startTime":"8:00","checkInDate":"2016-01-18","checkOutDate":"2016-01-18","endTime":"10:00","timeId":1}],"orderStatus":"1","houseAdress":"广州市天河区汇景北路靠近中成教育华农校区","totalPrice":60},{"houseTitle":"五山公寓","custOrderId":10083,"payStatus":false,"orderCode":"M20160118101931710","roomType":"ROOM","roomNo":"2","timeSegmentList":[{"startTime":"10:00","checkInDate":"2016-01-19","checkOutDate":"2016-01-19","endTime":"14:00","timeId":3}],"orderStatus":"5","houseAdress":"广州市天河区汇景北路靠近中成教育华农校区","totalPrice":97},{"houseTitle":"创客空间三楼","custOrderId":10082,"payStatus":false,"orderCode":"M20160118095003162","roomType":"ROOM","roomNo":"1","timeSegmentList":[{"startTime":"10:00","checkInDate":"2016-01-19","checkOutDate":"2016-01-19","endTime":"14:00","timeId":3}],"orderStatus":"5","houseAdress":"广州市天河区五山路靠近广东高校学生公寓","totalPrice":69},{"houseTitle":"碧桂园华苑37","custOrderId":10052,"payStatus":false,"orderCode":"M20160108225137171","roomType":"ROOM","roomNo":"2","timeSegmentList":[{"startTime":"8:00","checkInDate":"2016-01-08","checkOutDate":"2016-01-08","endTime":"12:00","timeId":2}],"orderStatus":"5","houseAdress":"广州市番禺区M05乡道靠近博仁药店","totalPrice":78},{"houseTitle":"碧桂园华苑37","custOrderId":10048,"payStatus":false,"orderCode":"M20160108151559141","roomType":"ROOM","roomNo":"2","timeSegmentList":[{"startTime":"16:00","checkInDate":"2016-01-08","checkOutDate":"2016-01-08","endTime":"20:00","timeId":6}],"orderStatus":"1","houseAdress":"广州市番禺区M05乡道靠近博仁药店","totalPrice":78},{"houseTitle":"碧桂园华苑37","custOrderId":10047,"payStatus":false,"orderCode":"M20160108151502170","roomType":"ROOM","roomNo":"2","timeSegmentList":[{"startTime":"8:00","checkInDate":"2016-01-08","checkOutDate":"2016-01-08","endTime":"10:00","timeId":1},{"startTime":"16:00","checkInDate":"2016-01-08","checkOutDate":"2016-01-09","endTime":"8:00","timeId":7},{"startTime":"8:00","checkInDate":"2016-01-09","checkOutDate":"2016-01-09","endTime":"10:00","timeId":1},{"startTime":"8:00","checkInDate":"2016-01-12","checkOutDate":"2016-01-12","endTime":"10:00","timeId":1},{"startTime":"18:00","checkInDate":"2016-01-12","checkOutDate":"2016-01-12","endTime":"20:00","timeId":6}],"orderStatus":"1","houseAdress":"广州市番禺区M05乡道靠近博仁药店","totalPrice":118},{"houseTitle":"碧桂园华苑37","custOrderId":10046,"payStatus":true,"orderCode":"M20160108150442501","roomType":"ROOM","roomNo":"2","timeSegmentList":[{"startTime":"8:00","checkInDate":"2016-01-08","checkOutDate":"2016-01-08","endTime":"12:00","timeId":2},{"startTime":"14:00","checkInDate":"2016-01-09","checkOutDate":"2016-01-09","endTime":"18:00","timeId":5}],"orderStatus":"5","houseAdress":"广州市番禺区M05乡道靠近博仁药店","totalPrice":156},{"houseTitle":"创客空间二楼","custOrderId":10045,"payStatus":true,"orderCode":"M20160108111340119","roomType":"ROOM","roomNo":"1","timeSegmentList":[{"startTime":"8:00","checkInDate":"2016-01-09","checkOutDate":"2016-01-09","endTime":"12:00","timeId":2}],"orderStatus":"5","houseAdress":"广州市天河区五山路靠近广东高校学生公寓","totalPrice":94},{"houseTitle":"创客空间二楼","custOrderId":10044,"payStatus":true,"orderCode":"M20160108111330321","roomType":"ROOM","roomNo":"2","timeSegmentList":[{"startTime":"8:00","checkInDate":"2016-01-08","checkOutDate":"2016-01-08","endTime":"12:00","timeId":2}],"orderStatus":"5","houseAdress":"广州市天河区五山路靠近广东高校学生公寓","totalPrice":80},{"houseTitle":"创客空间三楼","custOrderId":10043,"payStatus":true,"orderCode":"M20160108100911395","roomType":"ROOM","roomNo":"1","timeSegmentList":[{"startTime":"8:00","checkInDate":"2016-01-08","checkOutDate":"2016-01-08","endTime":"10:00","timeId":1}],"orderStatus":"5","houseAdress":"广州市天河区五山路靠近广东高校学生公寓","totalPrice":50}],"totalCnt":14}
     * statuscode : 1
     */

    private String message;
    /**
     * totalPage  : 2
     * list : [{"houseTitle":"汇景新城","custOrderId":10089,"payStatus":false,"orderCode":"M20160118134835773","roomType":"HOUSE","roomNo":"3","timeSegmentList":[{"startTime":"8:00","checkInDate":"2016-01-18","checkOutDate":"2016-01-18","endTime":"10:00","timeId":1}],"orderStatus":"1","houseAdress":"广州市天河区汇景北路靠近中成教育华农校区","totalPrice":60},{"houseTitle":"五山公寓","custOrderId":10083,"payStatus":false,"orderCode":"M20160118101931710","roomType":"ROOM","roomNo":"2","timeSegmentList":[{"startTime":"10:00","checkInDate":"2016-01-19","checkOutDate":"2016-01-19","endTime":"14:00","timeId":3}],"orderStatus":"5","houseAdress":"广州市天河区汇景北路靠近中成教育华农校区","totalPrice":97},{"houseTitle":"创客空间三楼","custOrderId":10082,"payStatus":false,"orderCode":"M20160118095003162","roomType":"ROOM","roomNo":"1","timeSegmentList":[{"startTime":"10:00","checkInDate":"2016-01-19","checkOutDate":"2016-01-19","endTime":"14:00","timeId":3}],"orderStatus":"5","houseAdress":"广州市天河区五山路靠近广东高校学生公寓","totalPrice":69},{"houseTitle":"碧桂园华苑37","custOrderId":10052,"payStatus":false,"orderCode":"M20160108225137171","roomType":"ROOM","roomNo":"2","timeSegmentList":[{"startTime":"8:00","checkInDate":"2016-01-08","checkOutDate":"2016-01-08","endTime":"12:00","timeId":2}],"orderStatus":"5","houseAdress":"广州市番禺区M05乡道靠近博仁药店","totalPrice":78},{"houseTitle":"碧桂园华苑37","custOrderId":10048,"payStatus":false,"orderCode":"M20160108151559141","roomType":"ROOM","roomNo":"2","timeSegmentList":[{"startTime":"16:00","checkInDate":"2016-01-08","checkOutDate":"2016-01-08","endTime":"20:00","timeId":6}],"orderStatus":"1","houseAdress":"广州市番禺区M05乡道靠近博仁药店","totalPrice":78},{"houseTitle":"碧桂园华苑37","custOrderId":10047,"payStatus":false,"orderCode":"M20160108151502170","roomType":"ROOM","roomNo":"2","timeSegmentList":[{"startTime":"8:00","checkInDate":"2016-01-08","checkOutDate":"2016-01-08","endTime":"10:00","timeId":1},{"startTime":"16:00","checkInDate":"2016-01-08","checkOutDate":"2016-01-09","endTime":"8:00","timeId":7},{"startTime":"8:00","checkInDate":"2016-01-09","checkOutDate":"2016-01-09","endTime":"10:00","timeId":1},{"startTime":"8:00","checkInDate":"2016-01-12","checkOutDate":"2016-01-12","endTime":"10:00","timeId":1},{"startTime":"18:00","checkInDate":"2016-01-12","checkOutDate":"2016-01-12","endTime":"20:00","timeId":6}],"orderStatus":"1","houseAdress":"广州市番禺区M05乡道靠近博仁药店","totalPrice":118},{"houseTitle":"碧桂园华苑37","custOrderId":10046,"payStatus":true,"orderCode":"M20160108150442501","roomType":"ROOM","roomNo":"2","timeSegmentList":[{"startTime":"8:00","checkInDate":"2016-01-08","checkOutDate":"2016-01-08","endTime":"12:00","timeId":2},{"startTime":"14:00","checkInDate":"2016-01-09","checkOutDate":"2016-01-09","endTime":"18:00","timeId":5}],"orderStatus":"5","houseAdress":"广州市番禺区M05乡道靠近博仁药店","totalPrice":156},{"houseTitle":"创客空间二楼","custOrderId":10045,"payStatus":true,"orderCode":"M20160108111340119","roomType":"ROOM","roomNo":"1","timeSegmentList":[{"startTime":"8:00","checkInDate":"2016-01-09","checkOutDate":"2016-01-09","endTime":"12:00","timeId":2}],"orderStatus":"5","houseAdress":"广州市天河区五山路靠近广东高校学生公寓","totalPrice":94},{"houseTitle":"创客空间二楼","custOrderId":10044,"payStatus":true,"orderCode":"M20160108111330321","roomType":"ROOM","roomNo":"2","timeSegmentList":[{"startTime":"8:00","checkInDate":"2016-01-08","checkOutDate":"2016-01-08","endTime":"12:00","timeId":2}],"orderStatus":"5","houseAdress":"广州市天河区五山路靠近广东高校学生公寓","totalPrice":80},{"houseTitle":"创客空间三楼","custOrderId":10043,"payStatus":true,"orderCode":"M20160108100911395","roomType":"ROOM","roomNo":"1","timeSegmentList":[{"startTime":"8:00","checkInDate":"2016-01-08","checkOutDate":"2016-01-08","endTime":"10:00","timeId":1}],"orderStatus":"5","houseAdress":"广州市天河区五山路靠近广东高校学生公寓","totalPrice":50}]
     * totalCnt : 14
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
         * houseTitle : 汇景新城
         * custOrderId : 10089
         * payStatus : false
         * orderCode : M20160118134835773
         * roomType : HOUSE
         * roomNo : 3
         * timeSegmentList : [{"startTime":"8:00","checkInDate":"2016-01-18","checkOutDate":"2016-01-18","endTime":"10:00","timeId":1}]
         * orderStatus : 1
         * houseAdress : 广州市天河区汇景北路靠近中成教育华农校区
         * totalPrice : 60
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
            private boolean payStatus;
            private String orderCode;
            private String roomType;
            private String roomNo;
            private String orderStatus;
            private String houseAdress;
            private String totalPrice;
            /**
             * startTime : 8:00
             * checkInDate : 2016-01-18
             * checkOutDate : 2016-01-18
             * endTime : 10:00
             * timeId : 1
             */

            private List<TimeSegmentListEntity> timeSegmentList;

            public void setHouseTitle(String houseTitle) {
                this.houseTitle = houseTitle;
            }

            public void setCustOrderId(int custOrderId) {
                this.custOrderId = custOrderId;
            }

            public void setPayStatus(boolean payStatus) {
                this.payStatus = payStatus;
            }

            public void setOrderCode(String orderCode) {
                this.orderCode = orderCode;
            }

            public void setRoomType(String roomType) {
                this.roomType = roomType;
            }

            public void setRoomNo(String roomNo) {
                this.roomNo = roomNo;
            }

            public void setOrderStatus(String orderStatus) {
                this.orderStatus = orderStatus;
            }

            public void setHouseAdress(String houseAdress) {
                this.houseAdress = houseAdress;
            }

            public void setTotalPrice(String totalPrice) {
                this.totalPrice = totalPrice;
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

            public boolean isPayStatus() {
                return payStatus;
            }

            public String getOrderCode() {
                return orderCode;
            }

            public String getRoomType() {
                return roomType;
            }

            public String getRoomNo() {
                return roomNo;
            }

            public String getOrderStatus() {
                return orderStatus;
            }

            public String getHouseAdress() {
                return houseAdress;
            }

            public String getTotalPrice() {
                return totalPrice;
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
