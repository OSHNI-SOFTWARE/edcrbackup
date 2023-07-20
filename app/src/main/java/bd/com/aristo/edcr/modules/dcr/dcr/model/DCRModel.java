package bd.com.aristo.edcr.modules.dcr.dcr.model;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Tariqul.Islam on 6/13/17.
 */

public class DCRModel extends RealmObject implements Serializable{

    @PrimaryKey
    private long id;
    private String dID;
    private String accompanyID;
    private String remarks;
    private String createDate;
    private String sendDate;
    private String status;
    private String statusCause;
    private String shift;
    private boolean isSent;
    private int month;
    private int year;
    private boolean isNew;

    @Ignore
    public static String COL_ID = "id", COL_DID = "dID", COL_CREATE_DATE = "createDate", COL_SEND_DATE = "sendDate",
            COL_STATUS = "status", COL_STATUS_CAUSE = "statusCause", COL_IS_NEW = "isNew", COL_SHIFT = "shift", COL_IS_MORNING = "isSynced"
            , COL_MONTH = "month", COL_YEAR = "year", COL_IS_SENT = "isSent";

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getdID() {
        return dID;
    }

    public void setdID(String dID) {
        this.dID = dID;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getSendDate() {
        return sendDate;
    }

    public void setSendDate(String sendDate) {
        this.sendDate = sendDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusCause() {
        return statusCause;
    }

    public void setStatusCause(String statusCause) {
        this.statusCause = statusCause;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public boolean isSent() {
        return isSent;
    }

    public void setSent(boolean synced) {
        isSent = synced;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public String getAccompanyID() {
        return accompanyID;
    }

    public void setAccompanyID(String accompanyID) {
        this.accompanyID = accompanyID;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    @Override
    public String toString() {
        return "DCRModel{" +
                "id=" + id +
                ", dID='" + dID + '\'' +
                ", accompanyID='" + accompanyID + '\'' +
                ", remarks='" + remarks + '\'' +
                ", createDate='" + createDate + '\'' +
                ", sendDate='" + sendDate + '\'' +
                ", status='" + status + '\'' +
                ", statusCause='" + statusCause + '\'' +
                ", shift='" + shift + '\'' +
                ", isSent=" + isSent +
                ", month=" + month +
                '}';
    }
}
