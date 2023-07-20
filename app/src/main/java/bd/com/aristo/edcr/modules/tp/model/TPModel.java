package bd.com.aristo.edcr.modules.tp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by monir.sobuj on 6/8/17.
 */

public class TPModel {
    @SerializedName("DetailSL")
    @Expose
    private String DetailSL;
    @SerializedName("AnDetailSL")
    @Expose
    private String AnDetailSL;
    @SerializedName("DayNumber")
    @Expose
    private String DayNumber;
    @SerializedName("MeetingPlace")
    @Expose
    private String MeetingPlace;
    @SerializedName("SetTime")
    @Expose
    private String SetTime;
    @SerializedName("ShiftName")
    @Expose
    private String ShiftName;
    @SerializedName("ShiftType")
    @Expose
    private String ShiftType;
    @SerializedName("AllowanceNature")
    @Expose
    private String AllowanceNature;
    @SerializedName("CalendarCell")
    @Expose
    private String CalendarCell;
    @SerializedName("Year")
    @Expose
    private String Year;
    @SerializedName("MonthNumber")
    @Expose
    private String MonthNumber;
    @SerializedName("SubDetailList")
    @Expose
    List<PlaceModel> SubDetailList;

    @SerializedName("DetailStatus")
    private String approvalStatus; // Waiting, Approved

    @SerializedName("Review")
    private String changeStatus; // Yes, No, Approved


    public String getDetailSL() {
        return DetailSL;
    }

    public void setDetailSL(String detailSL) {
        this.DetailSL = detailSL;
    }

    public String getAnDetailSL() {
        return AnDetailSL;
    }

    public void setAnDetailSL(String anDetailSL) {
        this.AnDetailSL = anDetailSL;
    }

    public String getDayNumber() {
        return DayNumber;
    }

    public void setDayNumber(String dayNumber) {
        this.DayNumber = dayNumber;
    }

    public String getMeetingPlace() {
        return MeetingPlace;
    }

    public void setMeetingPlace(String meetingPlace) {
        this.MeetingPlace = meetingPlace;
    }

    public String getTime() {
        return SetTime;
    }

    public void setTime(String time) {
        this.SetTime = time;
    }

    public String getShift() {
        return ShiftName;
    }

    public void setShift(String shift) {
        this.ShiftName = shift;
    }

    public String getAllowanceNature() {
        return AllowanceNature;
    }

    public void setAllowanceNature(String allowanceNature) {
        this.AllowanceNature = allowanceNature;
    }

    public String getCalendarCell() {
        return CalendarCell;
    }

    public void setCalendarCell(String calendarCell) {
        this.CalendarCell = calendarCell;
    }

    public String getYear() {
        return Year;
    }

    public void setYear(String year) {
        this.Year = year;
    }

    public String getMonth() {
        return MonthNumber;
    }

    public void setMonth(String month) {
        this.MonthNumber = month;
    }

    public String getSetTime() {
        return SetTime;
    }

    public void setSetTime(String setTime) {
        SetTime = setTime;
    }

    public String getShiftName() {
        return ShiftName;
    }

    public void setShiftName(String shiftName) {
        ShiftName = shiftName;
    }

    public String getShiftType() {
        return ShiftType;
    }

    public void setShiftType(String shiftType) {
        ShiftType = shiftType;
    }

    public String getMonthNumber() {
        return MonthNumber;
    }

    public void setMonthNumber(String monthNumber) {
        MonthNumber = monthNumber;
    }

    public List<PlaceModel> getSubDetailList() {
        return SubDetailList;
    }

    public void setSubDetailList(List<PlaceModel> subDetailList) {
        SubDetailList = subDetailList;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getChangeStatus() {
        return changeStatus;
    }

    public void setChangeStatus(String changeStatus) {
        this.changeStatus = changeStatus;
    }

    @Override
    public String toString() {
        return "TPModel{" +
                "DetailSL='" + DetailSL + '\'' +
                ", AnDetailSL='" + AnDetailSL + '\'' +
                ", DayNumber='" + DayNumber + '\'' +
                ", MeetingPlace='" + MeetingPlace + '\'' +
                ", SetTime='" + SetTime + '\'' +
                ", ShiftName='" + ShiftName + '\'' +
                ", ShiftType='" + ShiftType + '\'' +
                ", AllowanceNature='" + AllowanceNature + '\'' +
                ", CalendarCell='" + CalendarCell + '\'' +
                ", Year='" + Year + '\'' +
                ", MonthNumber='" + MonthNumber + '\'' +
                ", SubDetailList=" + SubDetailList +
                ", approvalStatus='" + approvalStatus + '\'' +
                ", changeStatus='" + changeStatus + '\'' +
                '}';
    }
}
