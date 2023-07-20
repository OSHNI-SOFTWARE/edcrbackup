package bd.com.aristo.edcr.modules.reports.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by monir.sobuj on 10/10/2018.
 */

public class DVRMarketDoctor {

    @SerializedName("DoctorID")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
