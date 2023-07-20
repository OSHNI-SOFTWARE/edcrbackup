package bd.com.aristo.edcr.modules.reports.model;

import io.realm.RealmObject;

/**
 * Created by monir.sobuj on 11/12/2018.
 */

public class UncoverRealm extends RealmObject {
    int day;
    String doctorId;
    int status;
    String statusCause;

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusCause() {
        return statusCause;
    }

    public void setStatusCause(String statusCause) {
        this.statusCause = statusCause;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }
}
