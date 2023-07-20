package bd.com.aristo.edcr.modules.dvr.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by monir.sobuj on 01/11/17.
 */

public class GetDVR {

    @SerializedName("DayNumber")
    @Expose
    private String day;


    @SerializedName("ShiftName")
    @Expose
    private String shift;

    @SerializedName("DetailSL")
    @Expose
    private String serverId;

    @SerializedName("DetailStatus")
    @Expose
    private String status;

    @SerializedName("MonthNumber")
    @Expose
    private String monthName;

    @SerializedName("Year")
    @Expose
    private String year;

    @SerializedName("SubDetailList")
    @Expose
    private  List<GetDVRDoctor> dvrDoctorList;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public String getMonthName() {
        return monthName;
    }

    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<GetDVRDoctor> getDvrDoctorList() {
        return dvrDoctorList;
    }

    public void setDvrDoctorList(List<GetDVRDoctor> dvrDoctorList) {
        this.dvrDoctorList = dvrDoctorList;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    @Override
    public String toString() {
        return "GetDVR{" +
                "day='" + day + '\'' +
                ", shift='" + shift + '\'' +
                ", serverId='" + serverId + '\'' +
                ", status='" + status + '\'' +
                ", monthName='" + monthName + '\'' +
                ", year='" + year + '\'' +
                ", dvrDoctorList=" + dvrDoctorList +
                '}';
    }
}
