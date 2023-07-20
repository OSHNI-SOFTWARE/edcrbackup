package bd.com.aristo.edcr.modules.reports.ss.model;

import java.util.List;

import bd.com.aristo.edcr.models.Doctors;

/**
 * Created by altaf.sil on 1/28/18.
 */

public class SSProductModel {
    private String productName;
    private int rcvQty;
    private int planQty;
    private int exeQty;
    private int balQty;
    private List<Doctors> doctorsList;


    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getRcvQty() {
        return rcvQty;
    }

    public void setRcvQty(int rcvQty) {
        this.rcvQty = rcvQty;
    }

    public int getPlanQty() {
        return planQty;
    }

    public void setPlanQty(int planQty) {
        this.planQty = planQty;
    }

    public int getBalQty() {
        return balQty;
    }

    public void setBalQty(int balQty) {
        this.balQty = balQty;
    }

    public List<Doctors> getDoctorsList() {
        return doctorsList;
    }

    public void setDoctorsList(List<Doctors> doctorsList) {
        this.doctorsList = doctorsList;
    }

    public int getExeQty() {
        return exeQty;
    }

    public void setExeQty(int exeQty) {
        this.exeQty = exeQty;
    }
}
