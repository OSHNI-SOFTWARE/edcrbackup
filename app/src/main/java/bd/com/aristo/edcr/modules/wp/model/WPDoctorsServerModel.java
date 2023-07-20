package bd.com.aristo.edcr.modules.wp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by altaf.sil on 3/18/18.
 */

public class WPDoctorsServerModel {

    @SerializedName("DoctorID")
    @Expose
    private String doctorID;

    @SerializedName("DoctorName")
    @Expose
    private String doctorName;

    @SerializedName("Quantity")
    @Expose
    private int quantity;

    @SerializedName("AnSL")
    @Expose
    private long id;

    @SerializedName("InstiCode")
    @Expose
    private String instCode;

    @SerializedName("ShiftName")
    @Expose
    private String shift;

    public String getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(String doctorID) {
        this.doctorID = doctorID;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getInstCode() {
        return instCode;
    }

    public void setInstCode(String instCode) {
        this.instCode = instCode;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    @Override
    public String toString() {
        return "WPDoctorsServerModel{" +
                "doctorID='" + doctorID + '\'' +
                ", doctorName='" + doctorName + '\'' +
                ", quantity=" + quantity +
                ", Shift='" + shift + '\'' +
                ", id='" + id + '\'' +
                ", Institute='" + instCode + '\'' +
                '}';
    }
}
