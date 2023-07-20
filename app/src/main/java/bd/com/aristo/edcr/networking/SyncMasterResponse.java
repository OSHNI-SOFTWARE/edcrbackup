package bd.com.aristo.edcr.networking;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by altaf.sil on 12/14/17.
 */

public class SyncMasterResponse {
    @SerializedName("Status")
    @Expose
    private String status;

    @SerializedName("Message")
    @Expose
    private String message;

    @SerializedName("MorningList")
    @Expose
    private List<Code> morningLoc;

    @SerializedName("EveningList")
    @Expose
    private List<Code> eveningLoc;

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

    public List<Code> getMorningLoc() {
        return morningLoc;
    }

    public void setMorningLoc(List<Code> morningLoc) {
        this.morningLoc = morningLoc;
    }

    public List<Code> getEveningLoc() {
        return eveningLoc;
    }

    public void setEveningLoc(List<Code> eveningLoc) {
        this.eveningLoc = eveningLoc;
    }

    @Override
    public String toString() {
        return "SyncMasterResponse{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", morningLoc=" + morningLoc +
                ", eveningLoc=" + eveningLoc +
                '}';
    }

    public class Code {
        @SerializedName("InstituteCode")
        @Expose
        private String code;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }
}
