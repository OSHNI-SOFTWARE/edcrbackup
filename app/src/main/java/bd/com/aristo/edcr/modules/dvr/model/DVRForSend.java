package bd.com.aristo.edcr.modules.dvr.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.realm.annotations.PrimaryKey;

/**
 * Created by monir.sobuj on 01/11/17.
 */

public class DVRForSend {

    @SerializedName("DayNumber")
    @Expose
    private String DayNumber;


    @SerializedName("ShiftName")
    @Expose
    private String Shift;

    @PrimaryKey
    @SerializedName("DetailSL")
    @Expose
    private String DetailSL;

    @SerializedName("MonthNumber")
    @Expose
    private String MonthName;

    @SerializedName("Year")
    @Expose
    private String Year;
    @SerializedName("SetTime")
    @Expose
    private String Time;
    @SerializedName("SubDetailList")
    @Expose
    private List<DVRDoctor> SubDetailList;

    public String getDayNumber() {
        return DayNumber;
    }

    public void setDayNumber(String dayNumber) {
        DayNumber = dayNumber;
    }

    public String getShift() {
        return Shift;
    }

    public void setShift(String shift) {
        Shift = shift;
    }

    public String getDetailSL() {
        return DetailSL;
    }

    public void setDetailSL(String detailSL) {
        DetailSL = detailSL;
    }

    public String getMonthName() {
        return MonthName;
    }

    public void setMonthName(String monthName) {
        MonthName = monthName;
    }

    public String getYear() {
        return Year;
    }

    public void setYear(String year) {
        Year = year;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public List<DVRDoctor> getSubDetailList() {
        return SubDetailList;
    }

    public void setSubDetailList(List<DVRDoctor> subDetailList) {
        SubDetailList = subDetailList;

    }

    @Override
    public String toString() {
        return "DVRForSend{" +
                "DayNumber='" + DayNumber + '\'' +
                ", Shift='" + Shift + '\'' +
                ", DetailSL='" + DetailSL + '\'' +
                ", MonthName='" + MonthName + '\'' +
                ", Year='" + Year + '\'' +
                ", Time='" + Time + '\'' +
                ", SubDetailList=" + SubDetailList +
                '}';
    }
}
