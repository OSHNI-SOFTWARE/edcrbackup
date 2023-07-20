package bd.com.aristo.edcr.modules.dvr.model;



import io.realm.RealmObject;
import io.realm.annotations.Ignore;

/**
 * Created by monir.sobuj on 01/11/17.
 */

public class DVRDoctorRealm extends RealmObject {


    private String DoctorID;
    private long dvrLocalId;
    private boolean isEditable;

    public long getDvrLocalId() {
        return dvrLocalId;
    }

    public void setDvrLocalId(long dvrLocalId) {
        this.dvrLocalId = dvrLocalId;
    }


    public String getDoctorID() {
        return DoctorID;
    }

    public void setDoctorID(String doctorID) {
        DoctorID = doctorID;
    }

    public boolean isEditable() {
        return this.isEditable;
    }

    public void setEditable(final boolean editable) {
        this.isEditable = editable;
    }

    //COLUMN NAMES
    @Ignore
    public static final String COL_IS_EDITABLE = "isEditable",
            COL_DOCTOR_ID = "DoctorID",
            COL_DVR_LOCAL_ID = "dvrLocalId";


    @Override
    public String toString() {
        return "DVRDoctorRealm{" +
                "DoctorID='" + DoctorID + '\'' +
                ", dvrLocalId=" + dvrLocalId +
                '}';
    }
}
