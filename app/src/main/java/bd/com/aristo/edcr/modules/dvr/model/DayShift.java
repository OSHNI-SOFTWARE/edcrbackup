package bd.com.aristo.edcr.modules.dvr.model;


import java.io.Serializable;

/**
 * Created by monir.sobuj on 01/11/17.
 */

public class DayShift implements Serializable {

    private int day;
    private String weekDay;
    private boolean isMorning;
    private boolean isApproved;
    private boolean isFixed;

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public boolean isMorning() {
        return isMorning;
    }

    public void setMorning(boolean morning) {
        isMorning = morning;
    }

    public String getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(String weekDay) {
        this.weekDay = weekDay;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }

    public boolean isFixed() {
        return this.isFixed;
    }

    public void setFixed(final boolean fixed) {
        this.isFixed = fixed;
    }
}
