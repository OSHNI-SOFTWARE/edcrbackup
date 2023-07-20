package bd.com.aristo.edcr.modules.reports.ss.model;

import com.google.gson.annotations.SerializedName;

public class NewDoctorDCR {

    @SerializedName("DoctorID")
    private String doctorID;
    @SerializedName("ItemType")
    private String iyemType;
    @SerializedName("ProductCode")
    private String productCode;
    @SerializedName("ProductName")
    private String productName;
    @SerializedName("PackSize")
    private String packSize;
    @SerializedName("Quantity")
    private int quantity;
    @SerializedName("SetDate")
    private String date;
    @SerializedName("TeamVolume")
    private int noOfInterns;
    private String doctorName;

    public String getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(String doctorID) {
        this.doctorID = doctorID;
    }

    public String getIyemType() {
        return iyemType;
    }

    public void setIyemType(String iyemType) {
        this.iyemType = iyemType;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getNoOfInterns() {
        return noOfInterns;
    }

    public void setNoOfInterns(int noOfInterns) {
        this.noOfInterns = noOfInterns;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getPackSize() {
        return packSize;
    }

    public void setPackSize(String packSize) {
        this.packSize = packSize;
    }
}
