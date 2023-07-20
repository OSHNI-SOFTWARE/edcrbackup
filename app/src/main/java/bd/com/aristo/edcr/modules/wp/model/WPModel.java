package bd.com.aristo.edcr.modules.wp.model;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;

/**
 * Created by Tariqul.Islam on 6/13/17.
 */

public class WPModel extends RealmObject {

    private String name;
    private int day;
    private int year;
    private int month;
    private String doctorID;
    private String productID;
    private String instCode;
    private int count;
    private boolean isMorning;
    private int flag; // 0 for Selective, 1 for Sample, 2 for Gift
    private boolean isUploaded;

    @Ignore
    public static String COL_PRODUCT_ID = "productID", COL_DOCTOR_ID = "doctorID", COL_MONTH = "month",
            COL_DAY = "day", COL_YEAR = "year", COL_GIFT_ID = "giftID", COL_SAMPLE_ID = "sampleID", COL_COUNT = "count",
            COL_NAME = "name", COL_INST_CODE = "instCode",
            COL_IS_MORNING = "isMorning", COL_FLAG = "flag", COL_IS_UPLOADED = "isUploaded";


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

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isMorning() {
        return isMorning;
    }

    public void setMorning(boolean morning) {
        isMorning = morning;
    }
    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getInstCode() {
        return instCode;
    }

    public void setInstCode(String instCode) {
        this.instCode = instCode;
    }

    public boolean isUploaded() {
        return this.isUploaded;
    }

    public void setUploaded(final boolean uploaded) {
        this.isUploaded = uploaded;
    }

    @Override
    public String toString() {
        return "WPModel{" +
                ", productID='" + productID + '\'' +
                ", doctorID='" + doctorID + '\'' +
                ", day='" + day + '\'' +
                ", month='" + month + '\'' +
                ", year='" + year + '\'' +
                ", Institute='" + instCode + '\'' +
                ", count='" + count + '\'' +
                ", isMorning='" + isMorning + '\'' +
                ", flag='" + flag + '\'' +
                ", isUploaded='" + isUploaded + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WPModel wpModel = (WPModel) o;

        if (isMorning != wpModel.isMorning) return false;
        return doctorID.equals(wpModel.doctorID);
    }

    @Override
    public int hashCode() {
        int result = doctorID.hashCode();
        result = 31 * result + (isMorning ? 1 : 0);
        return result;
    }
}
