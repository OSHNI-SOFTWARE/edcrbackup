package bd.com.aristo.edcr.modules.reports.others;


import java.io.Serializable;

/**
 * Created by monir.sobuj on 01/11/17.
 */

public class DayDot implements Serializable {

    private int day;
    private String weekDay;
    private boolean isAbsent;
    private boolean isNew;
    private long dcrId;

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(String weekDay) {
        this.weekDay = weekDay;
    }

    public boolean isAbsent() {
        return isAbsent;
    }

    public void setAbsent(boolean absent) {
        isAbsent = absent;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public long getDcrId() {
        return dcrId;
    }

    public void setDcrId(long dcrId) {
        this.dcrId = dcrId;
    }
}
