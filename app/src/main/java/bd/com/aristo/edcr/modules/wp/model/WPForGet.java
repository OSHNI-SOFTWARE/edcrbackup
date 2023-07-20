package bd.com.aristo.edcr.modules.wp.model;

/**
 * Created by monir.sobuj on 05/05/21.
 */

public class WPForGet {

    private String ProductName;
    private String ProductCode;
    private String DoctorID;
    private String ShiftName;
    private String Quantity;
    private String InstiCode;
    private String ItemType;
    private String Date;

    public String getProductName() {
        return this.ProductName;
    }

    public void setProductName(final String productName) {
        this.ProductName = productName;
    }

    public String getProductCode() {
        return this.ProductCode;
    }

    public void setProductCode(final String productCode) {
        this.ProductCode = productCode;
    }

    public String getDoctorID() {
        return this.DoctorID;
    }

    public void setDoctorID(final String doctorID) {
        this.DoctorID = doctorID;
    }

    public String getShiftName() {
        return this.ShiftName;
    }

    public void setShiftName(final String shiftName) {
        this.ShiftName = shiftName;
    }

    public String getQuantity() {
        return this.Quantity;
    }

    public void setQuantity(final String quantity) {
        this.Quantity = quantity;
    }

    public String getInstiCode() {
        return this.InstiCode;
    }

    public void setInstiCode(final String instiCode) {
        this.InstiCode = instiCode;
    }

    public String getItemType() {
        return this.ItemType;
    }

    public void setItemType(final String itemType) {
        this.ItemType = itemType;
    }

    public String getDate() {
        return this.Date;
    }

    public void setDate(final String date) {
        this.Date = date;
    }

    @Override
    public String toString() {
        return "WPForGet{" +
                "ProductName='" + ProductName + '\'' +
                ", ProductCode='" + ProductCode + '\'' +
                ", DoctorID='" + DoctorID + '\'' +
                ", ShiftName='" + ShiftName + '\'' +
                ", Quantity='" + Quantity + '\'' +
                ", InstiCode='" + InstiCode + '\'' +
                ", ItemType='" + ItemType + '\'' +
                ", Date='" + Date + '\'' +
                '}';
    }
}
