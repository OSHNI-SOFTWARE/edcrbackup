package bd.com.aristo.edcr.models.db;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;

public class DCRForDVR extends RealmObject {
    //doctor id from server
    private String doctorID;
    //shift
    private boolean isMorning;
    //execution status
    private int status; //0 for not execution, 1 for execution, 2 for new dcr, 3 for absent
    //calendar date of execution
    private int day;
    //month number
    private int month;
    //year 4 digits
    private int year;

    @Ignore
    public static final String COL_DOCTOR_ID = "doctorID", COL_IS_MORNING = "isMorning", COL_STATUS = "status",
            COL_DAY = "day", COL_MONTH = "month", COL_YEAR = "year";

    public String getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(String doctorID) {
        this.doctorID = doctorID;
    }

    public boolean isMorning() {
        return isMorning;
    }

    public void setMorning(boolean morning) {
        isMorning = morning;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
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

    @Override
    public String toString() {
        return "DCRForDVR{" +
                "doctorID='" + doctorID + '\'' +
                ", isMorning=" + isMorning +
                ", status=" + status +
                ", day=" + day +
                ", month=" + month +
                ", year=" + year +
                '}';
    }
}
