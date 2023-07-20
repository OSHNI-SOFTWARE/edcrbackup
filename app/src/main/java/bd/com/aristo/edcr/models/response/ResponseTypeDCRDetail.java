package bd.com.aristo.edcr.models.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by altaf.sil on 12/14/17.
 */

public class ResponseTypeDCRDetail<T, U, V> {
    @SerializedName("Status")
    @Expose
    private String status;

    @SerializedName("Message")
    @Expose
    private String message;

    @SerializedName("DCRList")
    @Expose
    private List<T> dcrList = new ArrayList<>();

    @SerializedName("NewDCRList")
    @Expose
    private List<U> newDcrList = new ArrayList<>();

    @SerializedName("UnCoveredDCRList")
    @Expose
    private List<V> missedDcrList = new ArrayList<>();

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<T> getDcrList() {
        return dcrList;
    }

    public void setDcrList(List<T> dcrList) {
        this.dcrList = dcrList;
    }

    public List<U> getNewDcrList() {
        return newDcrList;
    }

    public void setNewDcrList(List<U> newDcrList) {
        this.newDcrList = newDcrList;
    }

    public List<V> getMissedDcrList() {
        return missedDcrList;
    }

    public void setMissedDcrList(List<V> missedDcrList) {
        this.missedDcrList = missedDcrList;
    }

    @Override
    public String toString() {
        return "ResponseTypeDCRDetail{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", dcrList=" + dcrList +
                ", newDcrList=" + newDcrList +
                ", missedDcrList=" + missedDcrList +
                '}';
    }
}
