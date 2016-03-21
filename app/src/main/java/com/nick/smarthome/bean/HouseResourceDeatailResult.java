package com.nick.smarthome.bean;

import java.util.List;

/**
 * Author:  nick
 * Email:   nickdevp@gmail.com
 * Date:    15/12/30 09:57.
 * Description:
 */
public class HouseResourceDeatailResult {


    /**
     * message : 信息查询成功！
     * data : {"roomImgList":[{"fileId":10089,"imgPath":"http://139.196.12.187:8080/HotelAppServer/fileupload/keyImg/20160118101108117.png"},{"fileId":10090,"imgPath":"http://139.196.12.187:8080/HotelAppServer/fileupload/keyImg/20160118101108833.png"}],"rnRoomInfoMap":{"houseTitle":"五山公寓","lockType":"ROOM","doorNo":"2","roomId":10096,"salePrice":40,"photoUrl":"http://139.196.12.187:8080/HotelAppServer/fileupload/keyImg/20160118101108117.png","houseDistance":0,"houseId":10033,"houseAdress":"广州市天河区汇景北路靠近中成教育华农校区"},"roomTimePriceList":[{"timeSegment":"8:00-10:00","salePrice":40,"timeId":1},{"timeSegment":"10:00-12:00","salePrice":97,"timeId":2},{"timeSegment":"12:00-14:00","salePrice":149,"timeId":3},{"timeSegment":"14:00-16:00","salePrice":197,"timeId":4},{"timeSegment":"16:00-18:00","salePrice":242,"timeId":5},{"timeSegment":"18:00-20:00","salePrice":283,"timeId":6},{"timeSegment":"20:00-8:00","salePrice":322,"timeId":7}],"haveBusinessDateSgementList":[{"checkInDate":"2016-01-19","timeId":2},{"checkInDate":"2016-01-19","timeId":3}],"noBusinessDateSgementList":[{"noBusinessDate":"2016-01-20"},{"noBusinessDate":"2016-01-22"}]}
     * statuscode : 1
     */

    private String message;
    /**
     * roomImgList : [{"fileId":10089,"imgPath":"http://139.196.12.187:8080/HotelAppServer/fileupload/keyImg/20160118101108117.png"},{"fileId":10090,"imgPath":"http://139.196.12.187:8080/HotelAppServer/fileupload/keyImg/20160118101108833.png"}]
     * rnRoomInfoMap : {"houseTitle":"五山公寓","lockType":"ROOM","doorNo":"2","roomId":10096,"salePrice":40,"photoUrl":"http://139.196.12.187:8080/HotelAppServer/fileupload/keyImg/20160118101108117.png","houseDistance":0,"houseId":10033,"houseAdress":"广州市天河区汇景北路靠近中成教育华农校区"}
     * roomTimePriceList : [{"timeSegment":"8:00-10:00","salePrice":40,"timeId":1},{"timeSegment":"10:00-12:00","salePrice":97,"timeId":2},{"timeSegment":"12:00-14:00","salePrice":149,"timeId":3},{"timeSegment":"14:00-16:00","salePrice":197,"timeId":4},{"timeSegment":"16:00-18:00","salePrice":242,"timeId":5},{"timeSegment":"18:00-20:00","salePrice":283,"timeId":6},{"timeSegment":"20:00-8:00","salePrice":322,"timeId":7}]
     * haveBusinessDateSgementList : [{"checkInDate":"2016-01-19","timeId":2},{"checkInDate":"2016-01-19","timeId":3}]
     * noBusinessDateSgementList : [{"noBusinessDate":"2016-01-20"},{"noBusinessDate":"2016-01-22"}]
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
         * houseTitle : 五山公寓
         * lockType : ROOM
         * doorNo : 2
         * roomId : 10096
         * salePrice : 40
         * photoUrl : http://139.196.12.187:8080/HotelAppServer/fileupload/keyImg/20160118101108117.png
         * houseDistance : 0
         * houseId : 10033
         * houseAdress : 广州市天河区汇景北路靠近中成教育华农校区
         */

        private RnRoomInfoMapEntity rnRoomInfoMap;
        /**
         * fileId : 10089
         * imgPath : http://139.196.12.187:8080/HotelAppServer/fileupload/keyImg/20160118101108117.png
         */

        private List<RoomImgListEntity> roomImgList;
        /**
         * timeSegment : 8:00-10:00
         * salePrice : 40
         * timeId : 1
         */

        private List<RoomTimePriceListEntity> roomTimePriceList;
        /**
         * checkInDate : 2016-01-19
         * timeId : 2
         */

        private List<HaveBusinessDateSgementListEntity> haveBusinessDateSgementList;
        /**
         * noBusinessDate : 2016-01-20
         */

        private List<NoBusinessDateSgementListEntity> noBusinessDateSgementList;

        public void setRnRoomInfoMap(RnRoomInfoMapEntity rnRoomInfoMap) {
            this.rnRoomInfoMap = rnRoomInfoMap;
        }

        public void setRoomImgList(List<RoomImgListEntity> roomImgList) {
            this.roomImgList = roomImgList;
        }

        public void setRoomTimePriceList(List<RoomTimePriceListEntity> roomTimePriceList) {
            this.roomTimePriceList = roomTimePriceList;
        }

        public void setHaveBusinessDateSgementList(List<HaveBusinessDateSgementListEntity> haveBusinessDateSgementList) {
            this.haveBusinessDateSgementList = haveBusinessDateSgementList;
        }

        public void setNoBusinessDateSgementList(List<NoBusinessDateSgementListEntity> noBusinessDateSgementList) {
            this.noBusinessDateSgementList = noBusinessDateSgementList;
        }

        public RnRoomInfoMapEntity getRnRoomInfoMap() {
            return rnRoomInfoMap;
        }

        public List<RoomImgListEntity> getRoomImgList() {
            return roomImgList;
        }

        public List<RoomTimePriceListEntity> getRoomTimePriceList() {
            return roomTimePriceList;
        }

        public List<HaveBusinessDateSgementListEntity> getHaveBusinessDateSgementList() {
            return haveBusinessDateSgementList;
        }

        public List<NoBusinessDateSgementListEntity> getNoBusinessDateSgementList() {
            return noBusinessDateSgementList;
        }

        public static class RnRoomInfoMapEntity {
            private String houseTitle;
            private String lockType;
            private String doorNo;
            private int roomId;
            private int salePrice;
            private String photoUrl;
            private int houseDistance;
            private int houseId;
            private String houseOwnerId;
            private String houseAdress;

            public void setHouseTitle(String houseTitle) {
                this.houseTitle = houseTitle;
            }

            public void setLockType(String lockType) {
                this.lockType = lockType;
            }

            public void setDoorNo(String doorNo) {
                this.doorNo = doorNo;
            }

            public void setRoomId(int roomId) {
                this.roomId = roomId;
            }

            public void setSalePrice(int salePrice) {
                this.salePrice = salePrice;
            }

            public void setPhotoUrl(String photoUrl) {
                this.photoUrl = photoUrl;
            }

            public void setHouseDistance(int houseDistance) {
                this.houseDistance = houseDistance;
            }

            public void setHouseId(int houseId) {
                this.houseId = houseId;
            }

            public void setHouseAdress(String houseAdress) {
                this.houseAdress = houseAdress;
            }

            public String getHouseTitle() {
                return houseTitle;
            }

            public String getHouseOwnerId() {
                return houseOwnerId;
            }

            public void setHouseOwnerId(String houseOwnerId) {
                this.houseOwnerId = houseOwnerId;
            }

            public String getLockType() {
                return lockType;
            }

            public String getDoorNo() {
                return doorNo;
            }

            public int getRoomId() {
                return roomId;
            }

            public int getSalePrice() {
                return salePrice;
            }

            public String getPhotoUrl() {
                return photoUrl;
            }

            public int getHouseDistance() {
                return houseDistance;
            }

            public int getHouseId() {
                return houseId;
            }

            public String getHouseAdress() {
                return houseAdress;
            }
        }

        public static class RoomImgListEntity {
            private int fileId;
            private String imgPath;

            public void setFileId(int fileId) {
                this.fileId = fileId;
            }

            public void setImgPath(String imgPath) {
                this.imgPath = imgPath;
            }

            public int getFileId() {
                return fileId;
            }

            public String getImgPath() {
                return imgPath;
            }
        }

        public static class RoomTimePriceListEntity {
            private String timeSegment;
            private int salePrice;
            private int timeId;

            public void setTimeSegment(String timeSegment) {
                this.timeSegment = timeSegment;
            }

            public void setSalePrice(int salePrice) {
                this.salePrice = salePrice;
            }

            public void setTimeId(int timeId) {
                this.timeId = timeId;
            }

            public String getTimeSegment() {
                return timeSegment;
            }

            public int getSalePrice() {
                return salePrice;
            }

            public int getTimeId() {
                return timeId;
            }
        }

        public static class HaveBusinessDateSgementListEntity {
            private String checkInDate;
            private int timeId;

            public void setCheckInDate(String checkInDate) {
                this.checkInDate = checkInDate;
            }

            public void setTimeId(int timeId) {
                this.timeId = timeId;
            }

            public String getCheckInDate() {
                return checkInDate;
            }

            public int getTimeId() {
                return timeId;
            }
        }

        public static class NoBusinessDateSgementListEntity {
            private String noBusinessDate;

            public void setNoBusinessDate(String noBusinessDate) {
                this.noBusinessDate = noBusinessDate;
            }

            public String getNoBusinessDate() {
                return noBusinessDate;
            }
        }
    }
}
