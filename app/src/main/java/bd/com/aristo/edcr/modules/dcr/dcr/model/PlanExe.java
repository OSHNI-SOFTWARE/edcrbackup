package bd.com.aristo.edcr.modules.dcr.dcr.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PlanExe implements Serializable {

    @SerializedName("Accompany")
    private String accompany;

    @SerializedName("DCRGift")
    private String dcrGift;

    @SerializedName("DCRSample")
    private String dcrSample;

    @SerializedName("DCRSelected")
    private String dcrStar;

    @SerializedName("Date")
    private String date;

    @SerializedName("DoctorName")
    private String doctorName;

    @SerializedName("PlanGift")
    private String planGift;

    @SerializedName("PlanSample")
    private String planSample;

    @SerializedName("PlanSelected")
    private String planStar;

    @SerializedName("Remark")
    private String remarks;

    @SerializedName("ShiftName")
    private String shift;

    public String getAccompany() {
        return accompany;
    }

    public void setAccompany(String accompany) {
        this.accompany = accompany;
    }

    public String getDcrGift() {
        return dcrGift;
    }

    public void setDcrGift(String dcrGift) {
        this.dcrGift = dcrGift;
    }

    public String getDcrSample() {
        return dcrSample;
    }

    public void setDcrSample(String dcrSample) {
        this.dcrSample = dcrSample;
    }

    public String getDcrStar() {
        return dcrStar;
    }

    public void setDcrStar(String dcrStar) {
        this.dcrStar = dcrStar;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getPlanGift() {
        return planGift;
    }

    public void setPlanGift(String planGift) {
        this.planGift = planGift;
    }

    public String getPlanSample() {
        return planSample;
    }

    public void setPlanSample(String planSample) {
        this.planSample = planSample;
    }

    public String getPlanStar() {
        return planStar;
    }

    public void setPlanStar(String planStar) {
        this.planStar = planStar;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    @Override
    public String toString() {
        return "PlanExe{" +
                "accompany='" + accompany + '\'' +
                ", dcrGift='" + dcrGift + '\'' +
                ", dcrSample='" + dcrSample + '\'' +
                ", dcrStar='" + dcrStar + '\'' +
                ", date='" + date + '\'' +
                ", doctorName='" + doctorName + '\'' +
                ", planGift='" + planGift + '\'' +
                ", planSample='" + planSample + '\'' +
                ", planStar='" + planStar + '\'' +
                ", remarks='" + remarks + '\'' +
                ", shift='" + shift + '\'' +
                '}';
    }
}
