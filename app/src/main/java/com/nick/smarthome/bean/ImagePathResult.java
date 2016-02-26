package com.nick.smarthome.bean;

import java.util.List;

/**
 * Author:  nick
 * Email:   nickdevp@gmail.com
 * Date:    15/12/22 13:20.
 * Description:
 */
public class ImagePathResult {


    /**
     * message : 图片上传成功！
     * data : {"imgPath":["http://139.196.12.187:8080/HotelAppServer/fileupload/keyImg/20151225144832.png"]}
     * statuscode : 1
     */

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
        private List<String> imgPath;

        public void setImgPath(List<String> imgPath) {
            this.imgPath = imgPath;
        }

        public List<String> getImgPath() {
            return imgPath;
        }
    }
}
