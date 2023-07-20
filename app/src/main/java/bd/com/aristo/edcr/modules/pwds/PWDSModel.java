package bd.com.aristo.edcr.modules.pwds;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Tariqul.Islam on 6/13/17.
 */

public class PWDSModel extends RealmObject {

    @PrimaryKey
    private long id;
    private String productID;
    private int month;
    private int year;
    private String doctorID;
    private boolean isApproved;
    private boolean isUploaded;


    @Ignore
    public static String COL_ID = "id", COL_PRODUCT_ID = "productID", COL_DOCTOR_ID = "doctorID", COL_MONTH = "month", COL_YEAR = "year",
            COL_IS_APPROVED = "isApproved", COL_IS_UPLOADED = "isUploaded";

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public boolean isUploaded() {
        return isUploaded;
    }

    public void setUploaded(boolean uploaded) {
        isUploaded = uploaded;
    }

    @Override
    public String toString() {
        return "PWDSModel{" +
                "id=" + id +
                ", productID='" + productID + '\'' +
                ", month=" + month +
                ", year=" + year +
                ", doctorID='" + doctorID + '\'' +
                ", isApproved=" + isApproved +
                ", isUploaded=" + isUploaded +
                '}';
    }
}
