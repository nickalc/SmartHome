package com.nick.smarthome.bean;

/**
 * Author:  nick
 * Email:   nickdevp@gmail.com
 * Date:    15/12/30 16:14.
 * Description:时间段
 */
public class OrderTimeEntity {

    String time;
    boolean isSelect;
    boolean isSellOut;
    String selectedTime;
    String timeId;
    String Date;
    int dateId;

    public int getDateId() {
        return dateId;
    }

    public void setDateId(int dateId) {
        this.dateId = dateId;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTimeId() {
        return timeId;
    }

    public void setTimeId(String timeId) {
        this.timeId = timeId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setIsSelect(boolean isSelect) {
        this.isSelect = isSelect;
    }

    public boolean isSellOut() {
        return isSellOut;
    }

    public void setIsSellOut(boolean isSellOut) {
        this.isSellOut = isSellOut;
    }

    public String getSelectedTime() {
        return selectedTime;
    }

    public void setSelectedTime(String selectedTime) {
        this.selectedTime = selectedTime;
    }
}
