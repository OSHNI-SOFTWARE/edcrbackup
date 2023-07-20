package bd.com.aristo.edcr.modules.bill.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by monir.sobuj on 5/29/2018.
 */

public class Bill extends RealmObject {

    @PrimaryKey
    @SerializedName("AnSL")
    @Expose
    long id;
    @SerializedName("Date")
    @Expose
    int day;
    int month;
    int year;
    @SerializedName("AllowanceNature")
    @Expose
    String nDA;
    @SerializedName("MorningPlace")
    @Expose
    String placesMorning;
    @SerializedName("EveningPlace")
    @Expose
    String placesEvening;
    @SerializedName("DA")
    @Expose
    int daAmount;
    @SerializedName("Total")
    @Expose
    int billAmount;
    @SerializedName("IsHoliday")
    @Expose
    String isHolidayWork;
    @SerializedName("ReviewStatus")
    @Expose
    String isReviewEnabled;
    @SerializedName("TA")
    @Expose
    int ta;

    @SerializedName("TotalDistince")
    @Expose
    int distance;
    @SerializedName("StatusBoss1")
    @Expose
    String status;

    @SerializedName("Remarks")
    String remarks;
    @SerializedName("Recommend")
    String superRemarks;

    boolean isUploaded;

    public static String COL_ID = "id", COL_DAY = "day", COL_MONTH = "month", COL_YEAR = "year", COL_BILL_AMOUNT = "billAmount",
            COL_HOLIDAY_WORK = "isHolidayWork", COL_IS_UPLOADED = "isUploaded", COL_IS_FROM_SERVER = "isFromServer", COL_IS_MILLAGE = "isMillage";

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getnDA() {
        return nDA;
    }

    public void setnDA(String nDA) {
        this.nDA = nDA;
    }

    public String getPlacesMorning() {
        return placesMorning;
    }

    public void setPlacesMorning(String placesMorning) {
        this.placesMorning = placesMorning;
    }

    public String getPlacesEvening() {
        return placesEvening;
    }

    public void setPlacesEvening(String placesEvening) {
        this.placesEvening = placesEvening;
    }

    public int getDaAmount() {
        return daAmount;
    }

    public void setDaAmount(int daAmount) {
        this.daAmount = daAmount;
    }

    public int getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(int billAmount) {
        this.billAmount = billAmount;
    }

    public boolean isUploaded() {
        return isUploaded;
    }

    public void setUploaded(boolean uploaded) {
        isUploaded = uploaded;
    }

    public String getIsHolidayWork() {
        return isHolidayWork;
    }

    public void setIsHolidayWork(String isHolidayWork) {
        this.isHolidayWork = isHolidayWork;
    }

    public String getIsReviewEnabled() {
        return isReviewEnabled;
    }

    public void setIsReviewEnabled(String isReviewEnabled) {
        this.isReviewEnabled = isReviewEnabled;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public int getTa() {
        return ta;
    }

    public void setTa(int ta) {
        this.ta = ta;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getSuperRemarks() {
        return superRemarks;
    }

    public void setSuperRemarks(String superRemarks) {
        this.superRemarks = superRemarks;
    }

    @Override
    public String toString() {
        return "Bill{" +
                "id=" + id +
                ", day=" + day +
                ", month=" + month +
                ", year=" + year +
                ", nDA='" + nDA + '\'' +
                ", placesMorning='" + placesMorning + '\'' +
                ", placesEvening='" + placesEvening + '\'' +
                ", daAmount=" + daAmount +
                ", billAmount=" + billAmount +
                ", isHolidayWork='" + isHolidayWork + '\'' +
                ", isReviewEnabled='" + isReviewEnabled + '\'' +
                ", status='" + status + '\'' +
                ", isUploaded=" + isUploaded +
                ", remarks=" + remarks +
                ", superRemarks=" + superRemarks +
                '}';
    }
}
