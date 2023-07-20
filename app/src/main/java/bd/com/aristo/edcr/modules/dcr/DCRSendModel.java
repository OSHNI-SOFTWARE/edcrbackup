package bd.com.aristo.edcr.modules.dcr;

import java.util.List;

import bd.com.aristo.edcr.modules.dcr.dcr.model.DCRModelForSend;

/**
 * Created by monir.sobuj on 9/20/2018.
 */

public class DCRSendModel {

    String userId;
    String accompanyIds;
    String createDate;
    String shift;
    String doctorId;
    String status;
    String statusCause;
    String remarks;
    String teamLeader;
    String contactNo;
    String teamVolume;
    String ward;
    int dcrSubType;
    List<DCRModelForSend> sampleList;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAccompanyIds() {
        return accompanyIds;
    }

    public void setAccompanyIds(String accompanyIds) {
        this.accompanyIds = accompanyIds;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
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

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getTeamLeader() {
        return teamLeader;
    }

    public void setTeamLeader(String teamLeader) {
        this.teamLeader = teamLeader;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getTeamVolume() {
        return teamVolume;
    }

    public void setTeamVolume(String teamVolume) {
        this.teamVolume = teamVolume;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public int getDcrSubType() {
        return this.dcrSubType;
    }

    public void setDcrSubType(final int dcrSubType) {
        this.dcrSubType = dcrSubType;
    }

    public List<DCRModelForSend> getSampleList() {
        return sampleList;
    }

    public void setSampleList(List<DCRModelForSend> sampleList) {
        this.sampleList = sampleList;
    }

    @Override
    public String toString() {
        return "DCRSendModel{" +
                "userId='" + userId + '\'' +
                ", accompanyIds='" + accompanyIds + '\'' +
                ", createDate='" + createDate + '\'' +
                ", shift='" + shift + '\'' +
                ", doctorId='" + doctorId + '\'' +
                ", status='" + status + '\'' +
                ", statusCause='" + statusCause + '\'' +
                ", remarks='" + remarks + '\'' +
                ", teamLeader='" + teamLeader + '\'' +
                ", contactNo='" + contactNo + '\'' +
                ", teamVolume='" + teamVolume + '\'' +
                ", sampleList='" + sampleList + '\'' +
                '}';
    }
}
