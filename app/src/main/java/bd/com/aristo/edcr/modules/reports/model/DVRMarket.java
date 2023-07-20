package bd.com.aristo.edcr.modules.reports.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by monir.sobuj on 10/10/2018.
 */

public class DVRMarket {

    @SerializedName("DayNumber")
    private String day;
    @SerializedName("ShiftName")
    private String shift;
    @SerializedName("SubDetailList")
    private List<DVRMarketDoctor> marketDoctorList;

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

    public List<DVRMarketDoctor> getMarketDoctorList() {
        return marketDoctorList;
    }

    public void setMarketDoctorList(List<DVRMarketDoctor> marketDoctorList) {
        this.marketDoctorList = marketDoctorList;
    }
}
