package bd.com.aristo.edcr.models.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by altaf.sil on 12/14/17.
 */

public class ResponseTypeBill<T, U, V> {
    @SerializedName("Status")
    @Expose
    private String status;

    @SerializedName("Message")
    @Expose
    private String message;

    @SerializedName("Other")
    @Expose
    private List<T> otherList = new ArrayList<>();

    @SerializedName("TA")
    @Expose
    private List<U> ta = new ArrayList<>();

    @SerializedName("TAOther")
    @Expose
    private List<V> otherTAList = new ArrayList<>();

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

    public List<T> getOtherList() {
        return otherList;
    }

    public void setOtherList(List<T> otherList) {
        this.otherList = otherList;
    }

    public List<U> getTa() {
        return ta;
    }

    public void setTa(List<U> ta) {
        this.ta = ta;
    }

    public List<V> getOtherTAList() {
        return otherTAList;
    }

    public void setOtherTAList(List<V> otherTAList) {
        this.otherTAList = otherTAList;
    }

    @Override
    public String toString() {
        return "ResponseTypeBill{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", otherList=" + otherList +
                ", ta=" + ta +
                ", otherTAList=" + otherTAList +
                '}';
    }
}
