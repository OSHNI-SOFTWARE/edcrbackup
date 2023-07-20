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

public class DANatureModel extends RealmObject {

    @SerializedName("Rate")
    @Expose
    private float rate;

    @SerializedName("ShortName")
    @Expose
    private String name;
    private Date effectDate;
    @Ignore
    @SerializedName("EffectDate")
    @Expose
    private String effectFrom;


    @Ignore
    public static final String COL_EFFECT_DATE = "effectDate";
    //variables no need to store
    //variables needed for adapter
    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        return "DANatureModel{" +
                "rate=" + rate +
                ", name='" + name + '\'' +
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
