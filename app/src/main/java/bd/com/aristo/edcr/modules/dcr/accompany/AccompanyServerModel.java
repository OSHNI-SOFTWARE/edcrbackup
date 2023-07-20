package bd.com.aristo.edcr.modules.dcr.accompany;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Altaf on 11/02/18.
 */

public class AccompanyServerModel{

    @SerializedName("MPGroup")
    private String mpoGroup;

    @SerializedName("MPOCode")
    private String mpoCode;

    @SerializedName("MPOName")
    private String mpoName;

    @SerializedName("MarketCode")
    private String marketCode;

    @SerializedName("MarketName")
    private String marketName;

    @SerializedName("Designation")
    private String designation;

    public String getMpoGroup() {
        return mpoGroup;
    }

    public void setMpoGroup(String mpoGroup) {
        this.mpoGroup = mpoGroup;
    }

    public String getMpoCode() {
        return mpoCode;
    }

    public void setMpoCode(String mpoCode) {
        this.mpoCode = mpoCode;
    }

    public String getMpoName() {
        return mpoName;
    }

    public void setMpoName(String mpoName) {
        this.mpoName = mpoName;
    }

    public String getMarketCode() {
        return marketCode;
    }

    public void setMarketCode(String marketCode) {
        this.marketCode = marketCode;
    }

    public String getMarketName() {
        return marketName;
    }

    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }
}
