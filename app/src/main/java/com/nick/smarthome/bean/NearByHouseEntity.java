package com.nick.smarthome.bean;

/**
 * Author:  nick
 * Email:   nickdevp@gmail.com
 * Date:    15/12/29 10:45.
 * Description:
 */
public class NearByHouseEntity {


    /**
     * houseTitle : 华南碧桂园
     * roomId : 10004
     * isPublic : 1
     * rnLockId : -1
     * houseDistance : 1113
     * houseAdress : 广州番禺南浦
     * customerId : 10007
     * doorNo : 1
     * salePrice : 100
     * longitude : 12.23
     * latitude : 12.22
     * photoUrl :
     * houseId : 10000
     */

    private String houseTitle;
    private int roomId;
    private int isPublic;
    private int rnLockId;
    private int houseDistance;
    private String houseAdress;
    private int customerId;
    private String doorNo;
    private int salePrice;
    private double longitude;
    private double latitude;
    private String photoUrl;
    private int houseId;

    public void setHouseTitle(String houseTitle) {
        this.houseTitle = houseTitle;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public void setIsPublic(int isPublic) {
        this.isPublic = isPublic;
    }

    public void setRnLockId(int rnLockId) {
        this.rnLockId = rnLockId;
    }

    public void setHouseDistance(int houseDistance) {
        this.houseDistance = houseDistance;
    }

    public void setHouseAdress(String houseAdress) {
        this.houseAdress = houseAdress;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public void setDoorNo(String doorNo) {
        this.doorNo = doorNo;
    }

    public void setSalePrice(int salePrice) {
        this.salePrice = salePrice;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public void setHouseId(int houseId) {
        this.houseId = houseId;
    }

    public String getHouseTitle() {
        return houseTitle;
    }

    public int getRoomId() {
        return roomId;
    }

    public int getIsPublic() {
        return isPublic;
    }

    public int getRnLockId() {
        return rnLockId;
    }

    public int getHouseDistance() {
        return houseDistance;
    }

    public String getHouseAdress() {
        return houseAdress;
    }

    public int getCustomerId() {
        return customerId;
    }

    public String getDoorNo() {
        return doorNo;
    }

    public int getSalePrice() {
        return salePrice;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public int getHouseId() {
        return houseId;
    }
}
