package com.nick.smarthome.bean;

/**
 * Author:  nick
 * Email:   nickdevp@gmail.com
 * Date:    15/12/14 23:48.
 * Description:
 */
public class MyHouseListEntity {

    /**
     * houseTitle : 广州碧桂园
     * roomId : -1
     * isPublic : 1
     * rnLockId : -3
     * houseAdress : 广州番禺南浦
     * customerId : 10007
     * doorNo : 1003
     * salePrice : 9999
     * longitude : 12.22
     * latitude : 12.23
     * photoUrl : null
     * houseId : -1
     */

    private String houseTitle;
    private int roomId;
    private int isPublic;
    private int rnLockId;
    private String houseAdress;
    private int customerId;
    private String doorNo;
    private String salePrice;
    private double longitude;
    private double latitude;
    private String photoUrl;
    private int houseId;
    private boolean isSelected;

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

    public void setHouseAdress(String houseAdress) {
        this.houseAdress = houseAdress;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public void setDoorNo(String doorNo) {
        this.doorNo = doorNo;
    }

    public void setSalePrice(String salePrice) {
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

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public boolean isSelected() {
        return isSelected;
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

    public String getHouseAdress() {
        return houseAdress;
    }

    public int getCustomerId() {
        return customerId;
    }

    public String getDoorNo() {
        return doorNo;
    }

    public String getSalePrice() {
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
