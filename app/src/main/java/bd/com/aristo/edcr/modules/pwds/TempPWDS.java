package bd.com.aristo.edcr.modules.pwds;

/**
 * Created by monir.sobuj on 6/13/17.
 */

public class TempPWDS {

    private String productID;
    private int month;
    private int year;
    private String doctorID;
    private boolean isApproved;
    private boolean isDeleted;

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(String doctorID) {
        this.doctorID = doctorID;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }
    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }


    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    @Override
    public String toString() {
        return "PWDSModel{" +
                ", productID='" + productID + '\'' +
                ", month=" + month +
                ", year=" + year +
                ", doctorID='" + doctorID + '\'' +
                ", isApproved=" + isApproved +
                ", isDeleted=" + isDeleted +
                '}';
    }
}
