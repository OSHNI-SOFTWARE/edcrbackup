package bd.com.aristo.edcr.modules.reports.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by monir.sobuj on 11/12/2018.
 */

public class Uncover {

    @SerializedName("DayNumber")
    @Expose()
    int day;
    @SerializedName("DetailList")
    @Expose()
    List<UncoverDetails> detailsList;

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public List<UncoverDetails> getDetailsList() {
        return detailsList;
    }

    public void setDetailsList(List<UncoverDetails> detailsList) {
        this.detailsList = detailsList;
    }
}
