package bd.com.aristo.edcr.modules.reports.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by monir.sobuj on 11/12/2018.
 */

public class UncoverDetails {
    @SerializedName("DoctorID")
    @Expose()
    String doctorId;
    @SerializedName("Status")
    @Expose()
    String status;
    @SerializedName("StatusCause")
    @Expose()
    String statusCause;

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

    @Override
    public String toString() {
        return "UncoverDetails{" +
                "doctorId='" + doctorId + '\'' +
                ", status='" + status + '\'' +
                ", statusCause='" + statusCause + '\'' +
                '}';
    }
}
