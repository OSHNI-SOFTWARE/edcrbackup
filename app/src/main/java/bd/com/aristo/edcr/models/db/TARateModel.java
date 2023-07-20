package bd.com.aristo.edcr.models.db;

import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;

/**
 * Created by monir.sobuj on 6/8/17.
 */

public class TARateModel extends RealmObject {

    @SerializedName("FuelPrice")
    @Expose
    private float fuelPrice;

    @SerializedName("MileageLimit")
    @Expose
    private float mileageLimit;

    @SerializedName("MileageRate")
    @Expose
    private float mileageRate;

    @SerializedName("RegionRate")
    @Expose
    private float rate;

    @SerializedName("RegionType")
    @Expose
    private String type;

    @SerializedName("RegionCode")
    @Expose
    private String regionCode;

    private Date effectDate;
    @Ignore
    @SerializedName("EffectDate")
    @Expose
    private String effectFrom;

    @Ignore
    public static final String COL_DATE = "date", COL_EFFECT_DATE = "effectDate";


    public float getFuelPrice() {
        return fuelPrice;
    }

    public void setFuelPrice(float fuelPrice) {
        this.fuelPrice = fuelPrice;
    }

    public float getMileageLimit() {
        return mileageLimit;
    }

    public void setMileageLimit(float mileageLimit) {
        this.mileageLimit = mileageLimit;
    }

    public float getMileageRate() {
        return mileageRate;
    }

    public void setMileageRate(float mileageRate) {
        this.mileageRate = mileageRate;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public Date getEffectDate() {
        return effectDate;
    }

    public void setEffectDate(Date effectDate) {
        this.effectDate = effectDate;
    }

    public String getEffectFrom() {
        return effectFrom;
    }

    public void setEffectFrom(String effectFrom) {
        this.effectFrom = effectFrom;
    }

    @Override
    public String toString() {
        return "TARateModel{" +
                "fuelPrice=" + fuelPrice +
                ", mileageLimit=" + mileageLimit +
                ", mileageRate=" + mileageRate +
                ", rate=" + rate +
                ", type='" + type + '\'' +
                ", regionCode='" + regionCode + '\'' +
                ", effectFrom='" + effectFrom + '\'' +
                '}';
    }

    public Date toDate(String dateString){
        Date date = null;
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        try {
            date = formatter.parse(dateString);
            Log.e("Print result: ", String.valueOf(date));

        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        return date;
    }
}
