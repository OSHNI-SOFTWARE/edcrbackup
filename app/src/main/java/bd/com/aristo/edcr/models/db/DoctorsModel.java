package bd.com.aristo.edcr.models.db;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;

/**
 * Created by monir.sobuj on 01/11/17.
 */

public class DoctorsModel extends RealmObject {

    @SerializedName("DoctorID")
    private String id;

    @SerializedName("DoctorName")
    private String name;

    @SerializedName("Degree")
    private String degree;

    @SerializedName("Address")
    private String address;

    @SerializedName("Specialization")
    private String special;

    @SerializedName("MonthNumber")
    int month;

    @SerializedName("Year")
    int year;

    @SerializedName("MorningLocation")
    String morningLoc;

    @SerializedName("EveningLocation")
    String eveningLoc;

    @Ignore
    public static String COL_ID = "id",
            COL_MORNING_LOC = "morningLoc",
            COL_EVENING_LOC = "eveningLoc",
            COL_NAME = "name",
            COL_MONTH = "month",
            COL_YEAR = "year";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSpecial() {
        return special;
    }

    public void setSpecial(String special) {
        this.special = special;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getMorningLoc() {
        return morningLoc;
    }

    public void setMorningLoc(String morningLoc) {
        this.morningLoc = morningLoc;
    }

    public String getEveningLoc() {
        return eveningLoc;
    }

    public void setEveningLoc(String eveningLoc) {
        this.eveningLoc = eveningLoc;
    }

    @Override
    public String toString() {
        return "DoctorsModel{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", degree='" + degree + '\'' +
                ", address='" + address + '\'' +
                ", special='" + special + '\'' +
                ", month='" + month + '\'' +
                ", year='" + year + '\'' +
                '}';
    }
}
