package bd.com.aristo.edcr.modules.wp.model;

/**
 * Created by monir.sobuj on 01/11/17.
 */

public class WPDoctorsModelForSend  {

    private String ShiftName;
    private String AnSL;
    private String InstiCode;
    private String Quantity;
    private String DoctorID;

    public String getDoctorID() {
        return DoctorID;
    }

    public void setDoctorID(String doctorID) {
        DoctorID = doctorID;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getShiftName() {
        return ShiftName;
    }

    public void setShiftName(String shiftName) {
        ShiftName = shiftName;
    }

    public String getAnSL() {
        return AnSL;
    }

    public void setAnSL(String anSL) {
        AnSL = anSL;
    }

    public String getInstiCode() {
        return InstiCode;
    }

    public void setInstiCode(String instiCode) {
        InstiCode = instiCode;
    }

    @Override
    public String toString() {
        return "WPDoctorsModelForSend{" +
                "DoctorID='" + DoctorID + '\'' +
                ", Quantity='" + Quantity + '\'' +
                ", Shift='" + ShiftName + '\'' +
                ", id='" + AnSL + '\'' +
                ", Institute='" + InstiCode + '\'' +
                '}';
    }
}
