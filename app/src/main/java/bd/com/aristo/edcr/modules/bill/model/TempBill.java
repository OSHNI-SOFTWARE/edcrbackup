package bd.com.aristo.edcr.modules.bill.model;

/**
 * Created by monir.sobuj on 5/29/2018.
 */

public class TempBill {

    long id;
    int day;
    int month;
    int year;
    String nDA;
    String placesMorning;
    String placesEvening;
    int daAmount;
    int billAmount;
    int ta;
    int distance;
    boolean isApproved;
    boolean isUploaded;
    boolean isMillage;
    boolean isHoliday;
    boolean isReview;
    String remarks;
    String superRemarks;

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

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }

    public boolean isMillage() {
        return isMillage;
    }

    public void setMillage(boolean millage) {
        isMillage = millage;
    }

    public boolean isHoliday() {
        return isHoliday;
    }

    public void setHoliday(boolean holiday) {
        isHoliday = holiday;
    }

    public boolean isReview() {
        return isReview;
    }

    public void setReview(boolean review) {
        isReview = review;
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
        return "TempBill{" +
                "id=" + id +
                ", day=" + day +
                ", month=" + month +
                ", year=" + year +
                ", nDA='" + nDA + '\'' +
                ", placesMorning='" + placesMorning + '\'' +
                ", placesEvening='" + placesEvening + '\'' +
                ", daAmount=" + daAmount +
                ", billAmount=" + billAmount +
                ", isUploaded=" + isUploaded +
                ", isMillage=" + isMillage +
                ", isHoliday=" + isHoliday +
                ", isReview=" + isReview +
                ", isApproved=" + isApproved +
                ", ta=" + ta +
                ", distance=" + distance +
                ", remarks=" + remarks +
                ", superRemarks=" + superRemarks +
                '}';
    }
}
