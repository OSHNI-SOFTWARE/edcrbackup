package bd.com.aristo.edcr.modules.wp.model;

/**
 * Created by monir.sobuj on 05/05/21.
 */

public class WPForSend {

    private String ProductCode;
    private String DoctorID;
    private String ShiftName;
    private String Quantity;
    private String InstiCode;
    private String MarketCode;

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

    public String getMarketCode() {
        return this.MarketCode;
    }

    public void setMarketCode(final String marketCode) {
        this.MarketCode = marketCode;
    }

    @Override
    public String toString() {
        return "WPForSend{" +
                "ProductCode='" + ProductCode + '\'' +
                ", DoctorID='" + DoctorID + '\'' +
                ", ShiftName='" + ShiftName + '\'' +
                ", Quantity='" + Quantity + '\'' +
                ", InstiCode='" + InstiCode + '\'' +
                ", MarketCode='" + MarketCode + '\'' +
                '}';
    }
}
