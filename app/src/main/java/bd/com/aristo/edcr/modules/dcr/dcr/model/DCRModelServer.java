package bd.com.aristo.edcr.modules.dcr.dcr.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;


/**
 * Created by Tariqul.Islam on 6/13/17.
 */

public class DCRModelServer {

    @SerializedName("DoctorID")
    private String dID;
    @SerializedName("MPOColleague")
    private String accompanyID;
    @SerializedName("Remark")
    private String remarks;
    @SerializedName("CreateDate")
    private String createDate;
    @SerializedName("SetDate")
    private String sendDate;
    @SerializedName("Status")
    private String status;
    @SerializedName("StatusCause")
    private String statusCause;
    @SerializedName("ShiftName")
    private String shift;
    @SerializedName("DCRType")
    private String dcrType;

    @SerializedName("DetailList")
    private List<DCRProductModelServer> productModelServerList;


    public String getdID() {
        return dID;
    }

    public void setdID(String dID) {
        this.dID = dID;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getSendDate() {
        return sendDate;
    }

    public void setSendDate(String sendDate) {
        this.sendDate = sendDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusCause() {
        return statusCause;
    }

    public void setStatusCause(String statusCause) {
        this.statusCause = statusCause;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }


    public String getAccompanyID() {
        return accompanyID;
    }

    public void setAccompanyID(String accompanyID) {
        this.accompanyID = accompanyID;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getDcrType() {
        return dcrType;
    }

    public void setDcrType(String dcrType) {
        this.dcrType = dcrType;
    }

    public List<DCRProductModelServer> getProductModelServerList() {
        return productModelServerList;
    }

    public void setProductModelServerList(List<DCRProductModelServer> productModelServerList) {
        this.productModelServerList = productModelServerList;
    }

    @Override
    public String toString() {
        return "DCRModel{" +
                ", dID='" + dID + '\'' +
                ", accompanyID='" + accompanyID + '\'' +
                ", remarks='" + remarks + '\'' +
                ", createDate='" + createDate + '\'' +
                ", sendDate='" + sendDate + '\'' +
                ", status='" + status + '\'' +
                ", statusCause='" + statusCause + '\'' +
                ", shift='" + shift + '\'' +
                '}';
    }
}
