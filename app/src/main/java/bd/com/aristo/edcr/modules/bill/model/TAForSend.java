package bd.com.aristo.edcr.modules.bill.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by monir.sobuj on 5/29/2018.
 */

public class TAForSend {
    @SerializedName("RegionCode")
    String region;
    @SerializedName("Distance")
    String distance;
    @SerializedName("Amount")
    String taAmount;

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getTaAmount() {
        return taAmount;
    }

    public void setTaAmount(String taAmount) {
        this.taAmount = taAmount;
    }

    @Override
    public String toString() {
        return "TAForSend{" +
                "region='" + region + '\'' +
                ", distance='" + distance + '\'' +
                ", taAmount='" + taAmount + '\'' +
                '}';
    }
}
