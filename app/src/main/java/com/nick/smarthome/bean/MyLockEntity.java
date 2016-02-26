package com.nick.smarthome.bean;

import java.io.Serializable;

/**
 * Author:  nick
 * Email:   nickdevp@gmail.com
 * Date:    15/12/15 17:06.
 * Description:
 */
public class MyLockEntity implements Serializable {

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

    private String houseTitle;
    private int customerId;
    private String lockType;
    private String address;
    private String doorNo;
    private String lockCode;
    private String photoUrl;
    private int rnLockId;
    private int houseId;
    private int isBound;
    private boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public void setHouseTitle(String houseTitle) {
        this.houseTitle = houseTitle;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public void setLockType(String lockType) {
        this.lockType = lockType;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDoorNo(String doorNo) {
        this.doorNo = doorNo;
    }

    public void setLockCode(String lockCode) {
        this.lockCode = lockCode;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public void setRnLockId(int rnLockId) {
        this.rnLockId = rnLockId;
    }

    public void setHouseId(int houseId) {
        this.houseId = houseId;
    }

    public void setIsBound(int isBound) {
        this.isBound = isBound;
    }

    public String getHouseTitle() {
        return houseTitle;
    }

    public int getCustomerId() {
        return customerId;
    }

    public String getLockType() {
        return lockType;
    }

    public String getAddress() {
        return address;
    }

    public String getDoorNo() {
        return doorNo;
    }

    public String getLockCode() {
        return lockCode;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public int getRnLockId() {
        return rnLockId;
    }

    public int getHouseId() {
        return houseId;
    }

    public int getIsBound() {
        return isBound;
    }
}
