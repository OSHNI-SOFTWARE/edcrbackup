package bd.com.aristo.edcr.modules.dvr.model;

import com.google.gson.annotations.SerializedName;


/**
 * Created by monir.sobuj on 01/11/17.
 */

public class GetDVRDoctor {


    @SerializedName("DoctorID")
    private String DoctorID;


    public String getDoctorID() {
        return DoctorID;
    }

    public void setDoctorID(String doctorID) {
        DoctorID = doctorID;
    }




}
