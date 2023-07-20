package bd.com.aristo.edcr.modules.reports.others;

import androidx.annotation.Nullable;

/**
 * Created by monir.sobuj on 01/11/17.
 */

public class MarketDVRModel {

    int day;
    @Nullable
    private String[] morning, evening;


    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String[] getMorning() {
        return morning;
    }

    public void setMorning(String[] morning) {
        this.morning = morning;
    }

    public String[] getEvening() {
        return evening;
    }

    public void setEvening(String[] evening) {
        this.evening = evening;
    }
}
