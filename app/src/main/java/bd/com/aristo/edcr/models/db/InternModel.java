package bd.com.aristo.edcr.models.db;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;

/**
 * Created by monir.sobuj on 01/11/17.
 */

public class InternModel extends RealmObject {

    @SerializedName("InternID")
    private String internId;

    @SerializedName("Date")
    private String date;

    @SerializedName("TeamLeader")
    private String keyPerson;

    @SerializedName("ContactNo")
    private String contact;

    @SerializedName("InstName")
    private String institute;

    @SerializedName("Ward")
    private String unit;

    @SerializedName("ShiftName")
    private boolean isMorning;

    @SerializedName("TeamVolume")
    private String noOfIntern;

    @Ignore
    public static final String COL_ID = "internId", COL_DATE = "date", COL_SHIFT = "isMorning";

    public String getInternId() {
        return internId;
    }

    public void setInternId(String internId) {
        this.internId = internId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public String getKeyPerson() {
        return keyPerson;
    }

    public void setKeyPerson(String keyPerson) {
        this.keyPerson = keyPerson;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getInstitute() {
        return institute;
    }

    public void setInstitute(String institute) {
        this.institute = institute;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public boolean isMorning() {
        return isMorning;
    }

    public void setMorning(boolean morning) {
        isMorning = morning;
    }

    public String getNoOfIntern() {
        return noOfIntern;
    }

    public void setNoOfIntern(String noOfIntern) {
        this.noOfIntern = noOfIntern;
    }

    @Override
    public String toString() {
        return "InternModel{" +
                "internId='" + internId + '\'' +
                ", date='" + date + '\'' +
                ", keyPerson='" + keyPerson + '\'' +
                ", contact='" + contact + '\'' +
                ", institute='" + institute + '\'' +
                ", unit='" + unit + '\'' +
                ", isMorning='" + isMorning + '\'' +
                ", noOfIntern='" + noOfIntern + '\'' +
                '}';
    }
}
