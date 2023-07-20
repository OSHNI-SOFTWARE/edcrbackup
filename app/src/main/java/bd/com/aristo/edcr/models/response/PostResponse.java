package bd.com.aristo.edcr.models.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by altaf.sil on 12/14/17.
 */

public class PostResponse {
    @SerializedName("Status")
    @Expose
    private String status;

    @SerializedName("Message")
    @Expose
    private String message;



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


}
