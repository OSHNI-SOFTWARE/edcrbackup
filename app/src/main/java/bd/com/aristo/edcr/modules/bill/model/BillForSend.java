package bd.com.aristo.edcr.modules.bill.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by monir.sobuj on 5/29/2018.
 */

public class BillForSend {
    @SerializedName("MasterSL")
    @Expose
    String serverId;

    @SerializedName("AnSL")
    @Expose
    String id;
    @SerializedName("AllowanceNature")
    @Expose
    String nDA;
    @SerializedName("MorningPlace")
    @Expose
    String placesMorning;
    @SerializedName("EveningPlace")
    @Expose
    String placesEvening;
    @SerializedName("Amount")
    @Expose
    String daAmount;
    @SerializedName("IsHoliday")
    @Expose
    String isHoliday; // "Yes" or "No"
    @SerializedName("Remarks")
    String remarks; // "Yes" or "No"

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getnDA() {
        return nDA;
    }

    public void setnDA(String nDA) {
        this.nDA = nDA;
    }

    public String getPlacesMorning() {
        return placesMorning;
    }

    public void setPlacesMorning(String placesMorning) {
        this.placesMorning = placesMorning;
    }

    public String getPlacesEvening() {
        return placesEvening;
    }

    public void setPlacesEvening(String placesEvening) {
        this.placesEvening = placesEvening;
    }

    public String getDaAmount() {
        return daAmount;
    }

    public void setDaAmount(String daAmount) {
        this.daAmount = daAmount;
    }

    public String getIsHoliday() {
        return isHoliday;
    }

    public void setIsHoliday(String isHoliday) {
        this.isHoliday = isHoliday;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public String toString() {
        return "BillForSend{" +
                "serverId='" + serverId + '\'' +
                ", id='" + id + '\'' +
                ", nDA='" + nDA + '\'' +
                ", placesMorning='" + placesMorning + '\'' +
                ", placesEvening='" + placesEvening + '\'' +
                ", daAmount='" + daAmount + '\'' +
                ", isHoliday='" + isHoliday + '\'' +
                ", remarks='" + remarks + '\'' +
                '}';
    }
}
