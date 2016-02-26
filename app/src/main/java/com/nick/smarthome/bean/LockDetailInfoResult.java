package com.nick.smarthome.bean;

/**
 * Author:  nick
 * Email:   nickdevp@gmail.com
 * Date:    15/12/29 15:19.
 * Description:
 */
public class LockDetailInfoResult {

    /**
     * message : 信息查询成功！
     * data : {"lockFileJson":"[{\"imgPath\":\"http://139.196.12.187:8080/HotelAppServer/fileupload/keyImg/20151228144757495.png\",\"fileId\":10012},{\"imgPath\":\"http://139.196.12.187:8080/HotelAppServer/fileupload/keyImg/20151228144757865.png\",\"fileId\":10013}]","lockTimeSegmentRelaJson":"[{\"timeId\":1,\"salePrice\":20},{\"timeId\":2,\"salePrice\":89},{\"timeId\":3,\"salePrice\":130},{\"timeId\":4,\"salePrice\":159},{\"timeId\":5,\"salePrice\":181},{\"timeId\":6,\"salePrice\":199},{\"timeId\":7,\"salePrice\":215}]","noBusinessDateJson":"[{\"noBusinessDate\":{},\"noBusinessDateId\":10013},{\"noBusinessDate\":{},\"noBusinessDateId\":10014}]","rnlockInfoJson":"{\"rnLockId\":10012,\"customerId\":10005,\"lockType\":\"HOUSE\",\"doorNo\":\"4\",\"lockCode\":\"100000000003\",\"photoUrl\":\"http://139.196.12.187:8080/HotelAppServer/fileupload/keyImg/20151228144757495.png\",\"isBound\":0,\"startPrice\":20,\"everyTwoHourPrice\":20,\"discountRate\":2,\"isPublic\":0,\"isHaveModify\":1}"}
     * statuscode : 1
     */

    private String message;
    /**
     * lockFileJson : [{"imgPath":"http://139.196.12.187:8080/HotelAppServer/fileupload/keyImg/20151228144757495.png","fileId":10012},{"imgPath":"http://139.196.12.187:8080/HotelAppServer/fileupload/keyImg/20151228144757865.png","fileId":10013}]
     * lockTimeSegmentRelaJson : [{"timeId":1,"salePrice":20},{"timeId":2,"salePrice":89},{"timeId":3,"salePrice":130},{"timeId":4,"salePrice":159},{"timeId":5,"salePrice":181},{"timeId":6,"salePrice":199},{"timeId":7,"salePrice":215}]
     * noBusinessDateJson : [{"noBusinessDate":{},"noBusinessDateId":10013},{"noBusinessDate":{},"noBusinessDateId":10014}]
     * rnlockInfoJson : {"rnLockId":10012,"customerId":10005,"lockType":"HOUSE","doorNo":"4","lockCode":"100000000003","photoUrl":"http://139.196.12.187:8080/HotelAppServer/fileupload/keyImg/20151228144757495.png","isBound":0,"startPrice":20,"everyTwoHourPrice":20,"discountRate":2,"isPublic":0,"isHaveModify":1}
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
        private String lockFileJson;
        private String lockTimeSegmentRelaJson;
        private String noBusinessDateJson;
        private String rnlockInfoJson;

        public void setLockFileJson(String lockFileJson) {
            this.lockFileJson = lockFileJson;
        }

        public void setLockTimeSegmentRelaJson(String lockTimeSegmentRelaJson) {
            this.lockTimeSegmentRelaJson = lockTimeSegmentRelaJson;
        }

        public void setNoBusinessDateJson(String noBusinessDateJson) {
            this.noBusinessDateJson = noBusinessDateJson;
        }

        public void setRnlockInfoJson(String rnlockInfoJson) {
            this.rnlockInfoJson = rnlockInfoJson;
        }

        public String getLockFileJson() {
            return lockFileJson;
        }

        public String getLockTimeSegmentRelaJson() {
            return lockTimeSegmentRelaJson;
        }

        public String getNoBusinessDateJson() {
            return noBusinessDateJson;
        }

        public String getRnlockInfoJson() {
            return rnlockInfoJson;
        }
    }
}
