package bd.com.aristo.edcr.modules.reports.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AbsentReport implements Serializable {

    @SerializedName("Absent")
    private String absentCount;
    @SerializedName("DoctorID")
    private String doctorID;
    @SerializedName("SetDate")
    private String absentDates;

    public String getAbsentDates() {
        return absentDates;
    }

    public void setAbsentDates(String absentDates) {
        this.absentDates = absentDates;
    }

    public int getAbsentCount() {
        return Integer.parseInt(absentCount);
    }

    public void setAbsentCount(String absentCount) {
        this.absentCount = absentCount;
    }

    public String getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(String doctorID) {
        this.doctorID = doctorID;
    }
}
