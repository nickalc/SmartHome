package com.nick.smarthome.bean;

/**
 * Author:  nick
 * Email:   nickdevp@gmail.com
 * Date:    15/12/30 16:14.
 * Description:
 */
public class OrderDateEntity {

    String showDate;
    String date;
    boolean isSelect;
    String selectedDate;
    boolean isSelctNight;
    int dateId;

    public boolean isSelctNight() {
        return isSelctNight;
    }

    public void setIsSelctNight(boolean isSelctNight) {
        this.isSelctNight = isSelctNight;
    }

    public int getDateId() {
        return dateId;
    }

    public void setDateId(int dateId) {
        this.dateId = dateId;
    }

    public String getShowDate() {
        return showDate;
    }

    public void setShowDate(String showDate) {
        this.showDate = showDate;
    }
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setIsSelect(boolean isSelect) {
        this.isSelect = isSelect;
    }

    public String getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(String selectedDate) {
        this.selectedDate = selectedDate;
    }
}
