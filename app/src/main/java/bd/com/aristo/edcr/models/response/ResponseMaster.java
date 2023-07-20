package bd.com.aristo.edcr.models.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by altaf.sil on 12/14/17.
 */

public class ResponseMaster<T> {
    @SerializedName("Status")
    @Expose
    private String status;

    @SerializedName("Message")
    @Expose
    private String message;

    @SerializedName("MasterList")
    @Expose
    private List<T> dataModelList = new ArrayList<>();


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

    public List<T> getDataModelList() {
        return dataModelList;
    }

    public void setDataModelList(List<T> dataModelList) {
        this.dataModelList = dataModelList;
    }


}
