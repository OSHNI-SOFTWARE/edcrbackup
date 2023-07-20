package bd.com.aristo.edcr.modules.reports.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DCRAccompany implements Serializable {

    @SerializedName("MPOCode")
    private String mpoCode;
    @SerializedName("MPOName")
    private String mpoName;
    @SerializedName("Designation")
    private String  designation;
    @SerializedName("AccompanyDate")
    private String visitDate;
    @SerializedName("Accompany")
    private String accompanyCode;
    @SerializedName("Morning")
    private String morningCount;
    @SerializedName("Evening")
    private String eveningCount;
    @SerializedName("Total")
    private String total;


    public String getMpoCode() {
        return mpoCode;
    }

    public void setMpoCode(String mpoCode) {
        this.mpoCode = mpoCode;
    }

    public String getMpoName() {
        return mpoName;
    }

    public void setMpoName(String mpoName) {
        this.mpoName = mpoName;
    }

    public String getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(String visitDate) {
        this.visitDate = visitDate;
    }

    public String getAccompanyCode() {
        return accompanyCode;
    }

    public void setAccompanyCode(String accompanyCode) {
        this.accompanyCode = accompanyCode;
    }

    public String getMorningCount() {
        return morningCount;
    }

    public void setMorningCount(String morningCount) {
        this.morningCount = morningCount;
    }

    public String getEveningCount() {
        return eveningCount;
    }

    public void setEveningCount(String eveningCount) {
        this.eveningCount = eveningCount;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }
}
