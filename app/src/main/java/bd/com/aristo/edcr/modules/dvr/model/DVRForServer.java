package bd.com.aristo.edcr.modules.dvr.model;


import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by monir.sobuj on 01/11/17.
 */

public class DVRForServer extends RealmObject {

    private int day;

    private boolean isMorning;

    private String serverId;

    @PrimaryKey
    private long id;

    private boolean isApproved;

    private int month;

    private int year;

    private boolean isInitialize;

    @Ignore
    public static String COL_ID = "id", COL_DAY = "day", COL_STATUS = "isApproved",  COL_MONTH = "month", COL_YEAR = "year", COL_SHIFT = "isMorning", COL_INIT = "isInitialize";

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

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public boolean isInitialize() {
        return isInitialize;
    }

    public void setInitialize(boolean initialize) {
        isInitialize = initialize;
    }

    @Override
    public String toString() {
        return "DVRForServer{" +
                "day=" + day +
                ", isMorning=" + isMorning +
                ", serverId='" + serverId + '\'' +
                ", id=" + id +
                ", isApproved=" + isApproved +
                ", month=" + month +
                ", year=" + year +
                ", isInitialize=" + isInitialize +
                '}';
    }
}
