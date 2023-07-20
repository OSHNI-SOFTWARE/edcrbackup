package bd.com.aristo.edcr.networking;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by monir.sobuj on 7/18/2017.
 */

public class ResponseWrapper<T> {

    @SerializedName("List")
    List<T> data;
    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }



}
