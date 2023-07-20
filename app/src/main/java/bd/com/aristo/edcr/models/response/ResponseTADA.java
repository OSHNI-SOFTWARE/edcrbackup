package bd.com.aristo.edcr.models.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import bd.com.aristo.edcr.models.db.DANatureModel;
import bd.com.aristo.edcr.models.db.TARateModel;


public class ResponseTADA {
    @SerializedName("Status")
    @Expose
    private String status;

    @SerializedName("Message")
    @Expose
    private String message;

    @SerializedName("DAList")
    @Expose
    private List<DANatureModel> daNatureModels = new ArrayList<>();

    @SerializedName("TAList")
    @Expose
    private List<TARateModel> taRateModels = new ArrayList<>();


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

    public List<DANatureModel> getDaNatureModels() {
        return daNatureModels;
    }

    public void setDaNatureModels(List<DANatureModel> daNatureModels) {
        this.daNatureModels = daNatureModels;
    }

    public List<TARateModel> getTaRateModels() {
        return taRateModels;
    }

    public void setTaRateModels(List<TARateModel> taRateModels) {
        this.taRateModels = taRateModels;
    }
}
