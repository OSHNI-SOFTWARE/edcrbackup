package bd.com.aristo.edcr.modules.gwds;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by altaf.sil on 1/4/18.
 */

public class GWDServerDoctorModel {
    @SerializedName("DoctorID")
    @Expose
    private String DoctorID;
    @SerializedName("DetailStatus")
    @Expose
    private String approvalStatus;

    public String getDoctorID() {
        return DoctorID;
    }

    public void setDoctorID(String doctorID) {
        this.DoctorID = doctorID;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }
}
