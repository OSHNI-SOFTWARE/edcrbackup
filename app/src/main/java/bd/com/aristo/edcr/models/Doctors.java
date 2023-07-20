package bd.com.aristo.edcr.models;

import com.google.gson.annotations.SerializedName;

import io.realm.annotations.Ignore;

/**
 * Created by monir.sobuj on 01/11/17.
 */

public class Doctors {

    private long id;

    @SerializedName("DoctorID")
    private String dId;

    @SerializedName("DoctorName")
    private String name;

    @SerializedName("DgreeName")
    private String degree;

    @SerializedName("InstitutionName")
    private String instName;

    @SerializedName("InstitutionCode")
    private String instCode;

    @SerializedName("Phone")
    private String addr;

    @SerializedName("SpecializationName")
    private String special;


    private int countItem;

    public String getInstName() {
        return instName;
    }

    public void setInstName(String instName) {
        this.instName = instName;
    }

    public String getInstCode() {
        return instCode;
    }

    public void setInstCode(String instCode) {
        this.instCode = instCode;
    }


    public String getDId() {
        return dId;
    }

    public void setDId(String dId) {
        this.dId = dId;
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


    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getSpecial() {
        return special;
    }

    public void setSpecial(String special) {
        this.special = special;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    //store column names here dId, id, name, degree, instName, instCode, address, special, shift
    @Ignore
    public static String COL_DID = "dId", COL_INST_CODE = "instCode", COL_ID = "id", COL_SHIFT = "shift"
            , COL_NAME = "name", COL_INST_NAME = "instName", COL_DEGREE = "degree", COL_ADDR = "addr", COL_SPECIAL = "special";
    @Override
    public String toString() {
        return "DoctorsModel{" +
                "id='" + dId + '\'' +
                ", name='" + name + '\'' +
                ", degree='" + degree + '\'' +
                ", inst='" + instName + '\'' +
                ", addr='" + addr + '\'' +
                ", special='" + special + '\'' +
                '}';
    }

    public int getCountItem() {
        return countItem;
    }

    public void setCountItem(int countItem) {
        this.countItem = countItem;
    }
}
