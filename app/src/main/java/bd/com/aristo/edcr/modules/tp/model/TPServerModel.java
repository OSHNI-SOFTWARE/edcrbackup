package bd.com.aristo.edcr.modules.tp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by monir.sobuj on 6/8/17.
 */

public class TPServerModel extends RealmObject {

    @SerializedName("DetailSL")
    @Expose
    private String serverId;

    @PrimaryKey
    @SerializedName("AnDetailSL")
    @Expose
    private long localId;

    @SerializedName("DayNumber")
    @Expose
    private String day;

    @SerializedName("MeetingPlace")
    @Expose
    private String contactPlace;

    @SerializedName("SetTime")
    @Expose
    private String reportTime;

    @SerializedName("ShiftType")
    @Expose
    private String ShiftType;

    @SerializedName("ShiftName")
    @Expose
    private String shift;

    @SerializedName("AllowanceNature")
    @Expose
    private String nDA;

    @SerializedName("CalendarCell")
    @Expose
    private String cCell;


    @SerializedName("Year")
    @Expose
    private int year;
    @SerializedName("MonthNumber")
    @Expose
    private int month;

    private boolean isApproved;
    private boolean isUploaded;
    private boolean isChanged;
    private boolean isChangedFromServer;


    @Ignore
    public static String COL_SHIFT = "shift", COL_LOCAL_ID = "localId", COL_DAY = "day", COL_YEAR = "year", COL_MONTH = "month",
        COL_CONTACT = "contactPlace", COL_IS_CHANGED = "isChanged", COL_IS_APPROVED = "isApproved", COL_IS_UPLOADED = "isUploaded";

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public long getLocalId() {
        return localId;
    }

    public void setLocalId(long localId) {
        this.localId = localId;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getContactPlace() {
        return contactPlace;
    }

    public void setContactPlace(String contactPlace) {
        this.contactPlace = contactPlace;
    }

    public String getReportTime() {
        return reportTime;
    }

    public void setReportTime(String reportTime) {
        this.reportTime = reportTime;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public String getnDA() {
        return nDA;
    }

    public void setnDA(String nDA) {
        this.nDA = nDA;
    }

    public String getcCell() {
        return cCell;
    }

    public void setcCell(String cCell) {
        this.cCell = cCell;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }

    public boolean isUploaded() {
        return isUploaded;
    }

    public void setUploaded(boolean uploaded) {
        isUploaded = uploaded;
    }

    public boolean isChanged() {
        return isChanged;
    }

    public void setChanged(boolean changed) {
        isChanged = changed;
    }

    public String getShiftType() {
        return ShiftType;
    }

    public void setShiftType(String shiftType) {
        ShiftType = shiftType;
    }

    public boolean isChangedFromServer() {
        return isChangedFromServer;
    }

    public void setChangedFromServer(boolean changedFromServer) {
        isChangedFromServer = changedFromServer;
    }

    @Override
    public String toString() {
        return "TPServerModel{" +
                "serverId='" + serverId + '\'' +
                ", localId='" + localId + '\'' +
                ", day='" + day + '\'' +
                ", contactPlace='" + contactPlace + '\'' +
                ", reportTime='" + reportTime + '\'' +
                ", shift='" + shift + '\'' +
                ", shiftType='" + ShiftType + '\'' +
                ", nDA='" + nDA + '\'' +
                ", cCell='" + cCell + '\'' +
                ", month=" + month +
                ", year=" + year +
                ", isApproved=" + isApproved +
                ", isUploaded=" + isUploaded +
                ", isChanged=" + isChanged +
                ", isChangedFromServer=" + isChangedFromServer +
                '}';
    }
}
